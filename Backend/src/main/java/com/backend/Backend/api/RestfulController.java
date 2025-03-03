package com.backend.Backend.api;

import com.backend.Backend.dataTypes.Smestaj;
import com.backend.Backend.dataTypes.Tiket;
import com.backend.Backend.dataTypes.TiketDTO;
import com.backend.Backend.repositories.SmestajRepo;
import com.backend.Backend.repositories.TiketiRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestfulController {
    private final SmestajRepo smestajRepo;
    private final TiketiRepo tiketiRepo;

    @Autowired
    public RestfulController(SmestajRepo smestajRepo, TiketiRepo tiketiRepo) {
        this.smestajRepo = smestajRepo;
        this.tiketiRepo = tiketiRepo;
    }

    @GetMapping("/alls")
    public List<Smestaj> findAllS() {
        List<Smestaj> smestaj = new ArrayList<>();

        smestajRepo.findAll().forEach(smestaj::add);

        return smestaj;
    }

    @GetMapping("/allt")
    public List<Tiket> findAllT() {
        List<Tiket> tiketi = new ArrayList<>();

        tiketiRepo.findAll().forEach(tiketi::add);

        return tiketi;
    }

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

        Class<Tiket> tiket_class =  Tiket.class;
        List<String> fields = new ArrayList<>();
        Arrays.stream(tiket_class.getDeclaredFields()).toList().forEach(field -> fields.add(field.getName().toLowerCase()));

        if (fields.contains(what) && fields.contains(orderBy)) return tiketiRepo.findAllTiketiFilter(what, which, orderBy, page);

        return new ArrayList<>();
    }

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
            tkt.setDrzava(tiket.getDrzava());
            tkt.setGrad(tiket.getGrad());

            Smestaj sm = new Smestaj();
            sm.setId(tiket.getSmestaj_id());
            tkt.setSmestaj(sm);

            tkt.setSezona(tiket.getSezona());
            tkt.setPrevoz(tiket.getPrevoz());
            tkt.setBroj_osoba(tiket.getBroj_osoba());
            tkt.setBroj_tiketa(tiket.getBroj_tiketa());
            tkt.setTrajanje_odmora(tiket.getTrajanje_odmora());
            tkt.setCena(tiket.getCena());
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


}
