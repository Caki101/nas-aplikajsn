package com.backend.Backend.controllers;

import com.backend.Backend.dataTypes.Smestaj;
import com.backend.Backend.dataTypes.Tiket;
import com.backend.Backend.dataTypes.TiketDTO;
import com.backend.Backend.dataTypes.User;
import com.backend.Backend.repositories.SmestajRepo;
import com.backend.Backend.repositories.TiketiRepo;
import com.backend.Backend.repositories.UsersRepo;
import com.backend.Backend.security.JwtUtil;
import com.backend.Backend.security.UserSecurity;
import com.backend.Backend.systemFiling.StorageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api")
public class RestfulController {
    private final TiketiRepo tiketiRepo;
    private final SmestajRepo smestajRepo;
    private final UsersRepo usersRepo;

    private final StorageService storageService;
    private final UserSecurity userSecurity;

    Map<HttpSession, Integer> sessions;
    Map<HttpSession, Timestamp> timeout;

    @Autowired
    public RestfulController(TiketiRepo tiketiRepo, SmestajRepo smestajRepo, UsersRepo usersRepo, StorageService storageService, UserSecurity userSecurity) {
        this.tiketiRepo = tiketiRepo;
        this.smestajRepo = smestajRepo;
        this.usersRepo = usersRepo;
        this.storageService = storageService;
        this.userSecurity = userSecurity;

        sessions = new HashMap<>();
        timeout = new HashMap<>();
    }

    // ***** GET ALL ***** \\
    @GetMapping("/allS")
    public List<Smestaj> findAllS() {
        List<Smestaj> smestaj = new ArrayList<>();

        smestajRepo.findAll().forEach(smestaj::add);

        return smestaj;
    }

    @GetMapping("/p/allT")
    public List<Tiket> findAllT() {
        List<Tiket> tiketi = new ArrayList<>();

        tiketiRepo.findAll().forEach(tiketi::add);

        return tiketi;
    }

    /**
     * Get mapping for all names of image files of a Smestaj with the given smestaj_id.
     *
     * @param smestaj_id Smestaj id to get images by.
     * @return array of file names for images of given Smestaj
     */
    @GetMapping("/images_{smestaj_id}")
    public ResponseEntity<?> images(@PathVariable int smestaj_id) {

        return ResponseEntity.ok(storageService.getAllFilenames(smestaj_id));
    }

    // ***** FILTERED GET ***** \\
    /** Method for receiving all tickets filtered by given parameters. Received data will be an array
     * with first 10 tickets ordered descending and other 10 tickets ordered by ascending order.
     * <br>
     * <br>
     * Page parameter is offsetting which tickets should be selected, it helps give exactly the tickets
     * that are supposed to be on the given page.
     *
     * @return Array list of Tiket objects.
     * @param what By what is the method filtering (e.g., drzava, grad, sezona..)
     * @param which Value to search by (e.g., what = 'drzava', which = 'Finska'..)
     * @param orderBy Order received data by attribute (e.g., cena, polazak..)
     * @param page A page number user is on.
     */
    @GetMapping("/stf_{what}_{which}_{orderBy}_{page}")
    public List<Tiket> findSTF(@PathVariable String what,
                               @PathVariable String which,
                               @PathVariable String orderBy,
                               @PathVariable Integer page) {
        what = what.toLowerCase();
        orderBy = orderBy.toLowerCase();

        return tiketiRepo.findAllTiketiFilter(what, which, orderBy, page);
    }

    @GetMapping("/tiket_{id}")
    public ResponseEntity<Tiket> findTiket(@PathVariable Long id) {
        Optional<Tiket> tiket = tiketiRepo.findById(id);

        return tiket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/image_{filename}")
    @ResponseBody
    public ResponseEntity<?> image(@PathVariable String filename) {
        Resource img = storageService.load(filename);

        if (img == null) return ResponseEntity.notFound().build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }

    @GetMapping("/first_image_{smestaj_id}")
    @ResponseBody
    public ResponseEntity<?> firstImage(@PathVariable Integer smestaj_id) {
        Resource img = storageService.loadFirst(smestaj_id);

        if (img == null) return ResponseEntity.notFound().build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }

    @PostMapping("/userLogin")
    public ResponseEntity<?> userLogin(@RequestBody User user_request,
                          HttpSession session) {
        if (timeout.containsKey(session)){
            if (System.currentTimeMillis()-timeout.get(session).getTime()>5000){
                timeout.remove(session);
            }
            else return ResponseEntity.status(429).body("Try again later");
        }

        User user = usersRepo.findFirstByEmail(user_request.getEmail());

        boolean email_valid = user!=null;
        boolean password_valid = email_valid?userSecurity.verifyPassword(user.getPassword(), user.getPassword()):false;

        if (!email_valid || !password_valid) {
            if (sessions.containsKey(session)) {
                if (sessions.get(session) == 3) {
                    sessions.remove(session);
                    timeout.put(session, new Timestamp(System.currentTimeMillis()));

                    return ResponseEntity.status(429).body("Try again later");
                }

                sessions.put(session, sessions.get(session)+1);
            }
            else sessions.put(session, 1);
        }

        if (!email_valid) {
            return ResponseEntity.status(401).body("Wrong email");
        }

        if (!userSecurity.verifyPassword(user_request.getPassword(), user.getPassword()))
            return ResponseEntity.status(401).body("Wrong password");

        sessions.remove(session);
        timeout.remove(session);

        //String token = JwtUtil.generateToken(user_request.getEmail());

        return ResponseEntity.ok().build();
    }

    // trying out jwt token generation
    @GetMapping("/temp1_{user}")
    public ResponseEntity<?> temp1(@PathVariable String user) {
        String token;
        token = JwtUtil.generateToken(user);

        return ResponseEntity.ok(token);
    }

    // ***** SAVING ENTITIES *****\\
    @PostMapping("/saveOneT")
    @ResponseBody
    public ResponseEntity<?> saveOneT(@RequestBody Tiket tiket) {
        return ResponseEntity.ok(tiketiRepo.save(tiket));
    }

    @PostMapping("/saveAllT")
    @ResponseBody
    public ResponseEntity<?> saveAllT(@RequestBody List<TiketDTO> tiketi) {
        List<Tiket> tiketi_list = new ArrayList<>();
        tiketi.forEach(tiket -> {
            Tiket tkt = new Tiket();

            Smestaj sm = new Smestaj();
            sm.setId(tiket.getSmestaj_id());
            tkt.setSmestaj(sm);

            tkt.setSezona(tiket.getSezona());
            tkt.setCena(tiket.getCena());
            tkt.setTrajanje_odmora(tiket.getTrajanje_odmora());
            tkt.setBroj_osoba(tiket.getBroj_osoba());
            tkt.setBroj_tiketa(tiket.getBroj_tiketa());
            tkt.setPrevoz(tiket.getPrevoz());
            tkt.setPolazak(tiket.getPolazak());

            tiketi_list.add(tkt);
        });
        return ResponseEntity.ok(tiketiRepo.saveAll(tiketi_list));
    }

    @PostMapping("/saveOneS")
    @ResponseBody
    public ResponseEntity<?> saveOneS(@RequestBody Smestaj smestaj) {
        return ResponseEntity.ok(smestajRepo.save(smestaj));
    }

    @PostMapping("/saveAllS")
    @ResponseBody
    public ResponseEntity<?> saveAllS(@RequestBody List<Smestaj> smestaji) {
        return ResponseEntity.ok(smestajRepo.saveAll(smestaji));
    }

    @PostMapping("/userSignUp")
    @ResponseBody
    public ResponseEntity<?> userSignUp(@RequestBody User user) {
        if (usersRepo.existsUserByEmail(user.getEmail()))
            return ResponseEntity
                    .status(401)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("User with that email already exists!");

        String encrypted_password = userSecurity.encryptPassword(user.getPassword());
        user.setPassword(encrypted_password);

        usersRepo.save(user);

        return ResponseEntity.ok().build();
    }


    // ***** UPLOAD FILES ***** \\
}
