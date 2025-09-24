package com.example.financeapp.controller;

import com.example.financeapp.model.Kategorija;
import com.example.financeapp.service.KategorijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kategorija")
public class KategorijaController {

    @Autowired
    private KategorijaService kategorijaService;

    @PostMapping("/{korisnikId}")
    public Kategorija createCustomKategorija(@RequestBody Kategorija kategorija, @PathVariable Long korisnikId) {
        return kategorijaService.createCustomKategorija(kategorija, korisnikId);
    }

    @GetMapping("/{korisnikId}/all")
    public List<Kategorija> getKategorijeForKorisnik(@PathVariable Long korisnikId) {
        return kategorijaService.getKategorijeForKorisnik(korisnikId);
    }

}
