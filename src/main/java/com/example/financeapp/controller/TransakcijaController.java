package com.example.financeapp.controller;

import com.example.financeapp.model.Transakcija;
import com.example.financeapp.service.TransakcijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transakcija")
public class TransakcijaController {

    @Autowired
    private TransakcijaService transakcijaService;

    @PostMapping("/{korisnikId}/{novcanikId}/{kategorijaId}")
    public Transakcija createTransakcija(@RequestBody Transakcija transakcija,
                                       @PathVariable Long korisnikId,
                                       @PathVariable Long novcanikId,
                                       @PathVariable Long kategorijaId) {
        return transakcijaService.createTransakcija(transakcija, korisnikId, novcanikId, kategorijaId);
    }

    @GetMapping("/novcanik/{novcanikId}")
    public List<Transakcija> getTransakcijeByNovcanikId(@PathVariable Long novcanikId) {
        return transakcijaService.getTransakcijeByNovcanikId(novcanikId);
    }

    @GetMapping("/{korisnikId}/filter")
    public List<Transakcija> getFilteredTransactions(
            @PathVariable Long korisnikId,
            @RequestParam(required = false) Long kategorijaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false) Double minIznos,
            @RequestParam(required = false) Double maxIznos) {
        return transakcijaService.getFilteredTransactions(korisnikId, kategorijaId, startDate, endDate, minIznos, maxIznos);
    }
}
