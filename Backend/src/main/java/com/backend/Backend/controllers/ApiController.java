package com.backend.Backend.controllers;

import com.backend.Backend.comps.ConfirmationListener;
import com.backend.Backend.dataTypes.*;
import com.backend.Backend.repositories.*;
import com.backend.Backend.search.Trie;
import com.backend.Backend.security.SecurityData;
import com.backend.Backend.security.TimeoutSessions ;
import com.backend.Backend.services.ConfirmationService;
import com.backend.Backend.services.MailgunService;
import com.backend.Backend.security.JwtUtil;
import com.backend.Backend.security.UserSecurity;
import com.backend.Backend.systemFiling.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    private Trie search_trie;

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

        init();
    }

    private void init() {
        search_trie = new Trie();

        tiketiRepo.findAll().forEach(tiket -> {
            List<String> words = Arrays.stream(tiket.searchable().split(" ")).toList();
            Trie root = search_trie;

            for(String word : words) {
                for (char c : word.toCharArray()) {
                    root = root.getChildren().computeIfAbsent(c, _ -> new Trie());
                    root.getIds().add(tiket.getId());
                }
                root = search_trie;
            }
        });
    }

    // ***** GET ALL ***** \\
    @GetMapping("/allS")
    public List<Smestaj> findAllS() {
        List<Smestaj> smestaji = new ArrayList<>();

        smestajRepo.findAll().forEach(smestaji::add);

        return smestaji;
    }

    @GetMapping("/allT")
    public List<Tiket> findAllT() {
        return new ArrayList<>(tiketiRepo.findAllAvailable());
    }

    @GetMapping("/allU")
    public List<User> findAllU() {
        List<User> users = new ArrayList<>();

        usersRepo.findAll().forEach(users::add);

        return users;
    }

    /**
     * Get mapping for all names of image files of a Smestaj with the given smestaj_id.
     *
     * @param smestaj_id Smestaj id to get images by.
     * @return array of file names for images of given Smestaj
     */
    @GetMapping("/images_{smestaj_id}")
    public ResponseEntity<?> images(@PathVariable Long smestaj_id) {
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

        UserFD userFD = new UserFD();
        userFD.setUsername(user.getUsername());

        // disabled for testing purposes
        //String token = JwtUtil.generateToken(user_request.getEmail());
        //userFD.setJwtToken(token);

        return ResponseEntity.ok(userFD);
    }

    @GetMapping("/best_offers")
    public ResponseEntity<?> getBestOffers() {
        List<Tiket> best_offers = tiketiRepo.findBestOffers(5);
        List<BestOffer> bos = new ArrayList<>();

        for (Tiket tiket : best_offers) {
            bos.add(new BestOffer(
                    tiket.getSmestaj().getDrzava(),
                    tiket.getSmestaj().getIme_smestaja(),
                    "http://" + SecurityData.ORIGIN + "/public-api/first_image_" + tiket.getSmestaj().getId()
                ));
        }

        return ResponseEntity.ok().body(bos);
    }

    @GetMapping("/destinations")
    public ResponseEntity<?> getDestinations() {
        Map<String, Map<String, List<String>>> destinations = new HashMap<>();

        Iterable<Tiket> tiketi = tiketiRepo.findAll();

        tiketi.forEach(tiket -> {
            destinations.computeIfAbsent(tiket
                            .getSmestaj()
                            .getDrzava(), _ -> new HashMap<>());

            destinations.get(tiket
                            .getSmestaj()
                            .getDrzava())
                    .computeIfAbsent(tiket
                            .getSmestaj()
                            .getGrad(), _ -> new ArrayList<>());

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            destinations.get(tiket
                        .getSmestaj()
                        .getDrzava())
                    .get(tiket.getSmestaj().getGrad())
                        .add(sdf.format(tiket.getPolazak()));
        });

        return ResponseEntity.ok(destinations);
    }

    // ***** SAVING ENTITIES *****\\
    /**
     * Method for saving new user into database and sending an email for verification of identity.
     *
     * @param user accepts json object of the same structure as {@link User} class.
     * @return code 401 if the user by provided email already exists;<br>
     * code 400 if user didn't verify identity in time;<br>
     * otherwise 200.
     */
    @Operation(summary = "User sign up")
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
                        + "'>verify your account here</a></div>",
                "info"
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
                                + "'>Reset password</a>",
                        "info"
                );

                return ResponseEntity.ok().build();
            }
            else return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.badRequest().build();
    }

    // ***** SEARCH SUGGESTIONS ***** \\
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String query) {
        if (query.isEmpty()) return null;

        Set<Long> ids = returnSearchIds(query);

        if (ids == null || ids.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(tiketiRepo.findAllByIds(ids));
    }

    private Set<Long> returnSearchIds(String query) {
        List<String> search_words = Arrays.stream(query.split(" ")).toList();

        Trie root;
        Set<Long> ids = new HashSet<>();

        for (String word : search_words) {
            word = word.toLowerCase();
            root = search_trie;

            for (char c : word.toCharArray()) {
                root = root.getChildren().get(c);
                if (root == null) return ids;
            }

            if (ids.isEmpty()){
                ids.addAll(root.getIds());
                continue;
            }
            ids = root.getIds().stream().filter(ids::contains).collect(Collectors.toCollection(HashSet::new));
        }

        return ids;
    }

    // ***** UPLOAD FILES ***** \\


    // ***** DEVELOPER / TESTING ENDPOINTS ***** \\
    // so - save one
    // sa - save all

    @PostMapping("/soTiketi")
    public ResponseEntity<?> saveOneT(@RequestBody Tiket tiket) {
        Tiket saved_tiket = tiketiRepo.save(tiket);

        return ResponseEntity.created(
                URI.create("http://" + SecurityData.ORIGIN + "/api/tiket_" + saved_tiket.getId())
        ).build();
    }

    @PostMapping("/saTiketi")
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

    @PostMapping("/soSmestaji")
    public ResponseEntity<?> saveOneS(@RequestBody Smestaj smestaj) {
        return ResponseEntity.ok(smestajRepo.save(smestaj));
    }

    @PostMapping("/saSmestaji")
    public ResponseEntity<?> saveAllS(@RequestBody List<Smestaj> smestaji) {
        return ResponseEntity.ok(smestajRepo.saveAll(smestaji));
    }

    @PostMapping("/soBought")
    public ResponseEntity<?> saveBought(@RequestBody KupljeniTiketi kupljeni_tiket) {
        if (kupljeni_tiket == null) return ResponseEntity.badRequest().build();
        if (kupljeni_tiket.getTiket().getId() == null || kupljeni_tiket.getUser().getId() == null)
            return ResponseEntity.badRequest().build();

        Optional<Tiket> tkt = tiketiRepo.findFirstById(kupljeni_tiket.getTiket().getId());
        if (tkt.isPresent()) {
            if (tkt.get().getBroj_tiketa() == 0) return ResponseEntity.status(406).body("No more available tickets");
        }
        else return ResponseEntity.status(406).body("No such ticket exists");

        tiketiRepo.buyTiket(kupljeni_tiket.getTiket().getId());

        return ResponseEntity.ok(kupljeniTiketiRepo.save(kupljeni_tiket));
    }

    @PostMapping("/saBought")
    public ResponseEntity<?> saveAllBought(@RequestBody List<KupljeniTiketi> kupljeni_tiketi) {
        List<Tiket> tiketi = new ArrayList<>();

        kupljeni_tiketi.forEach(tiket -> {
            Optional<Tiket> tkt = tiketiRepo.findById(tiket.getTiket().getId());
            if (tkt.isPresent()) {
                if (tkt.get().getBroj_tiketa() == 0) return;
            }
            else return;

            tiketiRepo.buyTiket(tiket.getTiket().getId());
            tiketi.add(tiketiRepo.findById(tiket.getTiket().getId()).orElse(new Tiket()));
            kupljeniTiketiRepo.save(tiket);
        });

        return ResponseEntity.ok(tiketi);
    }

    @PostMapping("/satddso") // save all tiket developer data saving objects
    public ResponseEntity<?> satddso(@RequestBody List<TiketDDSO> tiketDDSO) {
        List<Tiket> tiketi = new ArrayList<>();
        tiketDDSO.forEach(tiket -> {
            Smestaj smestaj = smestajRepo.saveIfNotExists(tiket.getSmestaj());
            tiket.setSmestaj(smestaj);
            Tiket tkt = new Tiket(tiket);
            tiketiRepo.save(tkt);
            tiketi.add(tkt);
        });
        return ResponseEntity.ok(tiketi);
    }

    @PostMapping("/promote")
    public ResponseEntity<?> promote(@RequestBody User user) {
        user = usersRepo.findFirstByUsername(user.getUsername());

        boolean inserted = usersRepo.promote(user.getId());
        if (!inserted) return ResponseEntity.ok("User is already admin");

        return ResponseEntity.ok("Promoted");
    }

    @PostMapping("/admin_login")
    public ResponseEntity<?> adminLogin(@RequestBody User user, HttpServletRequest request) {
        User usr = usersRepo.findFirstByUsername(user.getUsername());
        if (usr == null) return ResponseEntity.notFound().build();
        usr.setPassword(user.getPassword());

        ResponseEntity<?> response = userLogin(usr, request);
        if (response.getStatusCode().is4xxClientError()) return response;

        if (usersRepo.checkAdmin(usr.getId())) return ResponseEntity.ok().build();

        return ResponseEntity.status(401).build();
    }

    @GetMapping("/admin/all{table}")
    public ResponseEntity<?> adminAll(@PathVariable(name = "table") String table,
                                      @RequestParam(defaultValue = "") String search,
                                      @PageableDefault(sort = "id") Pageable pageable) {
        switch (table) {
            case "S":
                return ResponseEntity.ok(smestajRepo.getAdminAll(pageable));
            case "U":
                return ResponseEntity.ok(usersRepo.getAdminAll(pageable));
            case "T":
                Long[] search_results = new Long[0];
                if (!search.isEmpty()) {
                    search_results = returnSearchIds(search).toArray(new Long[0]);
                    if (search_results.length == 0) search_results = new Long[]{(long)-1};
                }
                return ResponseEntity.ok(tiketiRepo.getAdminAll(pageable,search_results));
            case "KT":
                return ResponseEntity.ok(kupljeniTiketiRepo.findAll(pageable));
            default:
                return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/admin/top5tikets")
    public ResponseEntity<?> adminTop5Tikets() {
        List<List<Long>> count = new ArrayList<>();
        List<List<?>> return_array = new ArrayList<>();

        for (Object[] row : kupljeniTiketiRepo.top5Tikets())
            count.add(List.of(((Number)row[0]).longValue(), ((Number)row[1]).longValue()));

        Set<Long> ids = new HashSet<>();
        count.forEach(l -> ids.add(l.getFirst()));

        List<Tiket> tiketi = tiketiRepo.adminFindAllByIds(ids);

        for (int i = 0; i < tiketi.size(); i++) {
            return_array.add(List.of(tiketi.get(i),count.get(i).getLast()));
        }

        return ResponseEntity.ok(return_array);
    }

    @GetMapping("/admin/top5smestajs")
    public ResponseEntity<?> adminTop5Smestajs() {
        List<List<Long>> count = new ArrayList<>();
        List<List<?>> return_array = new ArrayList<>();

        for (Object[] row : smestajRepo.top5Smestajs())
            count.add(List.of(((Number)row[0]).longValue(), ((Number)row[1]).longValue()));

        Set<Long> ids = new HashSet<>();
        count.forEach(l -> ids.add(l.getFirst()));

        List<Smestaj> smestaji = smestajRepo.adminFindAllByIds(ids);

        for (int i = 0; i < smestaji.size(); i++) {
            Smestaj smestaj = new Smestaj();
            for (Smestaj s : smestaji){
                if (Objects.equals(s.getId(), count.get(i).getFirst())){
                    smestaj = s;
                    break;
                }
            }
            return_array.add(List.of(smestaj,count.get(i).getLast()));
        }

        return ResponseEntity.ok(return_array);
    }

    @DeleteMapping("/do-tiketi")
    public ResponseEntity<?> doTiket(@RequestBody Tiket tiket) {
        tiketiRepo.delete(tiket);

        if (tiketiRepo.findById(tiket.getId()).isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/do-smestaji")
    public ResponseEntity<?> doSmestaj(@RequestBody Smestaj smestaj) {
        smestajRepo.delete(smestaj);

        if (smestajRepo.findById(smestaj.getId()).isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/do-korisnici")
    public ResponseEntity<?> doUser(@RequestBody User user) {
        usersRepo.delete(user);

        if (usersRepo.findById(user.getId()).isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.badRequest().build();
    }

    @PutMapping("/uo-tiketi")
    public ResponseEntity<?> uoTiketi(@RequestBody Tiket tiket) {
        String last_object = tiketiRepo.findById(tiket.getId()).toString();

        tiketiRepo.save(tiket);

        if (!tiketiRepo.findById(tiket.getId()).toString().equals(last_object)) {
            return ResponseEntity.noContent().build();
        }
        else return ResponseEntity.badRequest().build();
    }

    @PutMapping("/uo-smestaji")
    public ResponseEntity<?> uoSmestaji(@RequestBody Smestaj smestaj) {
        String last_object = smestajRepo.findById(smestaj.getId()).toString();

        smestajRepo.save(smestaj);

        if (!smestajRepo.findById(smestaj.getId()).toString().equals(last_object)) {
            return ResponseEntity.noContent().build();
        }
        else return ResponseEntity.badRequest().build();
    }
}
