package com.backend.Backend.api;

import com.backend.Backend.dataTypes.Smestaj;
import com.backend.Backend.dataTypes.Tiket;
import com.backend.Backend.dataTypes.TiketDTO;
import com.backend.Backend.dataTypes.User;
import com.backend.Backend.repositories.SmestajRepo;
import com.backend.Backend.repositories.TiketiRepo;
import com.backend.Backend.repositories.UsersRepo;
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

    @Autowired
    public RestfulController(TiketiRepo tiketiRepo, SmestajRepo smestajRepo, UsersRepo usersRepo, StorageService storageService, UserSecurity userSecurity) {
        this.tiketiRepo = tiketiRepo;
        this.smestajRepo = smestajRepo;
        this.usersRepo = usersRepo;
        this.storageService = storageService;
        this.userSecurity = userSecurity;
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

//    @GetMapping("/userLogin")
//    public Boolean userLogin(@RequestParam String email,
//                          @RequestParam String password,
//                          HttpSession session) {
//        Integer exist = sessions.putIfAbsent(session, 0);
//        if (exist == null) {}
//
//        return true;
//    }

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
        String encrypted_password = userSecurity.encryptPassword(user.getPassword());
        user.setPassword(encrypted_password);

        usersRepo.save(user);

        return ResponseEntity.ok().build();
    }


    // ***** UPLOAD FILES ***** \\
}
