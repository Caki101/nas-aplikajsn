package com.backend.Backend.controllers;

import com.backend.Backend.comps.ConfirmationListener;
import com.backend.Backend.dataTypes.*;
import com.backend.Backend.repositories.NewsletterRepo;
import com.backend.Backend.security.SecurityData;
import com.backend.Backend.services.ConfirmationService;
import com.backend.Backend.services.MailgunService;
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
public class ApiController {
    private final TiketiRepo tiketiRepo;
    private final SmestajRepo smestajRepo;
    private final UsersRepo usersRepo;

    private final StorageService storageService;

    private final MailgunService mailgunService;
    private final ConfirmationService confirmationService;

    private final ConfirmationListener  confirmationListener;
    private final NewsletterRepo newsletterRepo;

    Map<HttpSession, Integer> sessions;
    Map<HttpSession, Timestamp> timeout;

    @Autowired
    public ApiController(TiketiRepo tiketiRepo,
                         SmestajRepo smestajRepo,
                         UsersRepo usersRepo,
                         StorageService storageService,
                         MailgunService mailgunService,
                         ConfirmationService confirmationService,
                         ConfirmationListener confirmationListener,
                         NewsletterRepo newsletterRepo) {
        this.tiketiRepo = tiketiRepo;
        this.smestajRepo = smestajRepo;
        this.usersRepo = usersRepo;
        this.newsletterRepo = newsletterRepo;

        this.storageService = storageService;
        this.mailgunService = mailgunService;
        this.confirmationService = confirmationService;

        this.confirmationListener = confirmationListener;

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

    @GetMapping("/allT")
    public List<Tiket> findAllT() {
        List<Tiket> tiketi = new ArrayList<>();

        tiketi.addAll(tiketiRepo.findAllAvailable());

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
        boolean password_valid = email_valid?UserSecurity.verifyPassword(user.getPassword(), user.getPassword()):false;

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

        if (!UserSecurity.verifyPassword(user_request.getPassword(), user.getPassword()))
            return ResponseEntity.status(401).body("Wrong password");

        sessions.remove(session);
        timeout.remove(session);

        String token = JwtUtil.generateToken(user_request.getEmail());

        UserFD userFD = new UserFD();
        userFD.setUsername(user.getUsername());
        userFD.setJwtToken(token);

        return ResponseEntity.ok(userFD);
    }

    // ***** SAVING ENTITIES *****\\
    @PostMapping("/userSignUp")
    @ResponseBody
    public ResponseEntity<?> userSignUp(@RequestBody User user) {
        if (usersRepo.existsUserByEmail(user.getEmail()))
            return ResponseEntity
                    .status(401)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("User with that email already exists!");

        String encrypted_password = UserSecurity.encryptPassword(user.getPassword());
        user.setPassword(encrypted_password);
        usersRepo.save(user);

        String token = confirmationService.generateToken(user.getEmail());

        mailgunService.sendEmail(
                user.getEmail(),
                "Verify Account",
                "<div><a href='http://"
                        + SecurityData.ORIGIN
                        + "/public-api/verify_account/"
                        + token
                        + "'>verify your account here</a></div>"
        );

        boolean confirmed = confirmationListener.waitForConfirmation(token,60_000);
        if (confirmed){
            usersRepo.verified(user.getEmail());
            return ResponseEntity.ok("Confirmed");
        }

        return ResponseEntity.badRequest().body("Timed out");
    }

    /**
     * Adds new newsletter subscriber.
     *
     * @param nls newsletter subscriber
     * @return status of request
     */
    @PostMapping("/add_nls")
    public ResponseEntity<?> addNLS(@RequestBody NewsletterSubscriber nls) {
        if (nls == null) return ResponseEntity.badRequest().build();
        if (!newsletterRepo.existsByEmail(nls.getEmail())) newsletterRepo.save(nls);
        return ResponseEntity.ok().build();
    }

    // ***** FORGOT PASSWORD ***** \\
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody User user) {
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            User usr = usersRepo.findFirstByEmail(user.getEmail());
            if (usr != null) {
                String token = confirmationService.generateToken(user.getEmail());
                mailgunService.sendEmail(
                        user.getEmail(),
                        "Forgotten password",
                        "<a href='http://"
                                + SecurityData.ORIGIN
                                + "/pages/reset_password/"
                                + token
                                + "?email="
                                + user.getEmail()
                                + "'>Reset password</a>"
                );

                return ResponseEntity.ok().build();
            }
            else return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.badRequest().build();
    }

    // ***** UPLOAD FILES ***** \\


    // ***** DEVELOPER / TESTING ENDPOINTS ***** \\
    // so - save one
    // sa - save all

    @PostMapping("/soTiket")
    public ResponseEntity<?> saveOneT(@RequestBody Tiket tiket) {
        return ResponseEntity.ok(tiketiRepo.save(tiket));
    }

    @PostMapping("/saTiket")
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

    @PostMapping("/soSmestaj")
    public ResponseEntity<?> saveOneS(@RequestBody Smestaj smestaj) {
        return ResponseEntity.ok(smestajRepo.save(smestaj));
    }

    @PostMapping("/saSmestaj")
    public ResponseEntity<?> saveAllS(@RequestBody List<Smestaj> smestaji) {
        return ResponseEntity.ok(smestajRepo.saveAll(smestaji));
    }
}
