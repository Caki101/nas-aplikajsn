package com.backend.Backend.controllers;

import com.backend.Backend.comps.ConfirmationListener;
import com.backend.Backend.dataTypes.*;
import com.backend.Backend.repositories.*;
import com.backend.Backend.security.SecurityData;
import com.backend.Backend.security.TimeoutSessions ;
import com.backend.Backend.services.ConfirmationService;
import com.backend.Backend.services.MailgunService;
import com.backend.Backend.security.JwtUtil;
import com.backend.Backend.security.UserSecurity;
import com.backend.Backend.systemFiling.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final TiketiRepo tiketiRepo;
    private final SmestajRepo smestajRepo;
    private final UsersRepo usersRepo;
    private final NewsletterRepo newsletterRepo;
    private final KupljeniTiketiRepo kupljeniTiketiRepo;

    private final StorageService storageService;
    private final MailgunService mailgunService;
    private final ConfirmationService confirmationService;

    private final ConfirmationListener  confirmationListener;

    @Autowired
    public ApiController(TiketiRepo tiketiRepo,
                         SmestajRepo smestajRepo,
                         UsersRepo usersRepo,
                         StorageService storageService,
                         MailgunService mailgunService,
                         ConfirmationService confirmationService,
                         ConfirmationListener confirmationListener,
                         NewsletterRepo newsletterRepo,
                         KupljeniTiketiRepo kupljeniTiketiRepo) {
        this.tiketiRepo = tiketiRepo;
        this.smestajRepo = smestajRepo;
        this.usersRepo = usersRepo;
        this.newsletterRepo = newsletterRepo;
        this.kupljeniTiketiRepo = kupljeniTiketiRepo;

        this.storageService = storageService;
        this.mailgunService = mailgunService;
        this.confirmationService = confirmationService;

        this.confirmationListener = confirmationListener;
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
                                       HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();

        if (TimeoutSessions.isTimedOut(ip)){
            if (System.currentTimeMillis()-TimeoutSessions.getTimeout(ip).getTime()>5000){
                TimeoutSessions.removeTimeout(ip);
            }
            else return ResponseEntity.status(429).body("Try again later");
        }

        User user = usersRepo.findFirstByEmail(user_request.getEmail());

        boolean email_valid = user!=null;
        boolean password_valid = email_valid?UserSecurity.verifyPassword(user.getPassword(), user.getPassword()):false;

        if (!email_valid || !password_valid) {
            if (TimeoutSessions.containsSession(ip)) {
                if (TimeoutSessions.getTries(ip) == 3) {
                    TimeoutSessions.removeSession(ip);
                    TimeoutSessions.addTimeout(ip);

                    return ResponseEntity.status(429).body("Try again later");
                }

                TimeoutSessions.addTry(ip);
            }
            else TimeoutSessions.addSession(ip);
        }

        if (!email_valid) {
            return ResponseEntity.status(401).body("Wrong email");
        }

        if (!UserSecurity.verifyPassword(user_request.getPassword(), user.getPassword()))
            return ResponseEntity.status(401).body("Wrong password");

        TimeoutSessions.removeSession(ip);
        TimeoutSessions.removeTimeout(ip);

        // disabled for testing purposes
        //String token = JwtUtil.generateToken(user_request.getEmail());

        UserFD userFD = new UserFD();
        userFD.setUsername(user.getUsername());
        //userFD.setJwtToken(token);

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

    @PostMapping("/soBought")
    public ResponseEntity<?> saveBought(@RequestBody KupljeniTiketi kupljeni_tiket) {
        if (kupljeni_tiket == null) return ResponseEntity.badRequest().build();
        if (kupljeni_tiket.getId_tiket() == null || kupljeni_tiket.getId_user() == null)
            return ResponseEntity.badRequest().build();

        Optional<Tiket> tkt = tiketiRepo.findFirstById(kupljeni_tiket.getId_tiket());
        if (tkt.isPresent()) {
            if (tkt.get().getBroj_tiketa() == 0) return ResponseEntity.status(406).body("No more available tickets");
        }
        else return ResponseEntity.status(406).body("No such ticket exists");

        tiketiRepo.buyTiket(kupljeni_tiket.getId_tiket());

        return ResponseEntity.ok(kupljeniTiketiRepo.save(kupljeni_tiket));
    }

    @PostMapping("/saBought")
    public ResponseEntity<?> saveAllBought(@RequestBody List<KupljeniTiketi> kupljeni_tiketi) {
        List<Tiket> tiketi = new ArrayList<>();

        kupljeni_tiketi.forEach(tiket -> {
            Optional<Tiket> tkt = tiketiRepo.findById(tiket.getId_tiket());
            if (tkt.isPresent()) {
                if (tkt.get().getBroj_tiketa() == 0) return;
            }
            else return;

            tiketiRepo.buyTiket(tiket.getId_tiket());
            tiketi.add(tiketiRepo.findById(tiket.getId_tiket()).orElse(new Tiket()));
            kupljeniTiketiRepo.save(tiket);
        });

        return ResponseEntity.ok(tiketi);
    }
}
