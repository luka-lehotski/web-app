package com.example.financeapp.controller;

import com.example.financeapp.model.Novcanik;
import com.example.financeapp.service.NovcanikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/novcanik")
public class NovcanikController {

    @Autowired
    private NovcanikService novcanikService;

    @PostMapping("/{korisnikId}")
    public Novcanik createNovcanik(@RequestBody Novcanik novcanik, @PathVariable Long korisnikId) {
        return novcanikService.createNovcanik(novcanik, korisnikId);
    }

    @GetMapping("/{korisnikId}/all")
    public List<Novcanik> getNovcaniciByKorisnikId(@PathVariable Long korisnikId) {
        return novcanikService.getNovcaniciByKorisnikId(korisnikId);
    }

    @PutMapping("/{novcanikId}")
    public Novcanik updateNovcanik(@PathVariable Long novcanikId, @RequestBody Novcanik novcanikDetails) {
        return novcanikService.updateNovcanik(novcanikId, novcanikDetails);
    }

    @DeleteMapping("/{novcanikId}")
    public ResponseEntity<Void> deleteNovcanik(@PathVariable Long novcanikId) {
        novcanikService.deleteNovcanik(novcanikId);
        return ResponseEntity.noContent().build();
    }

}
