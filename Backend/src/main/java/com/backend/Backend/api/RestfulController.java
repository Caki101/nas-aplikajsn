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

    @GetMapping("/svi_tiketi_po_gradu_{grad}")
    public List<Tiket> findSviTiketiPo(@PathVariable String grad) {
        return tiketiRepo.findAllByGrad(grad);
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
