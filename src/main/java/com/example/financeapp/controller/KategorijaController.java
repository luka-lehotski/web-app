package com.example.financeapp.controller;

import com.example.financeapp.model.Kategorija;
import com.example.financeapp.service.KategorijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kategorije") // Promenjeno u množinu
public class KategorijaController {

    @Autowired
    private KategorijaService kategorijaService;

    // Dodavanje sopstvene kategorije
    @PostMapping("/{korisnikId}")
    public ResponseEntity<Kategorija> createCustomKategorija(@RequestBody Kategorija kategorija, @PathVariable Long korisnikId) {
        Kategorija kreirana = kategorijaService.createCustomKategorija(kategorija, korisnikId);
        return ResponseEntity.ok(kreirana);
    }

    // Pregled svih kategorija za korisnika (njegovih i globalnih)
    @GetMapping("/{korisnikId}")
    public ResponseEntity<List<Kategorija>> getKategorijeForKorisnik(@PathVariable Long korisnikId) {
        List<Kategorija> kategorije = kategorijaService.getKategorijeForKorisnik(korisnikId);
        return ResponseEntity.ok(kategorije);
    }

    // Brisanje sopstvene kategorije
    @DeleteMapping("/{korisnikId}/{kategorijaId}")
    public ResponseEntity<Void> deleteSopstvenuKategoriju(@PathVariable Long korisnikId, @PathVariable Long kategorijaId) {
        try {
            kategorijaService.deleteSopstvenuKategoriju(korisnikId, kategorijaId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Vraća 400 Bad Request ako brisanje nije dozvoljeno (npr. nije vlasnik)
            return ResponseEntity.badRequest().build();
        }
    }

    // Izmena (prepisivanje) predefinisane kategorije
    @PutMapping("/{korisnikId}/predefinisane/{predefinisanaKategorijaId}")
    public ResponseEntity<Kategorija> overridePredefinisanuKategoriju(@PathVariable Long korisnikId,
                                                                      @PathVariable Long predefinisanaKategorijaId,
                                                                      @RequestBody Kategorija podaci) {
        try {
            Kategorija prepisana = kategorijaService.overridePredefinisanuKategoriju(korisnikId, predefinisanaKategorijaId, podaci);
            return ResponseEntity.ok(prepisana);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
