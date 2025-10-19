package com.example.financeapp.controller;

import com.example.financeapp.dto.NovcanikRequest;
import com.example.financeapp.dto.statistika.Period;
import com.example.financeapp.dto.statistika.PoKategorijama;
import com.example.financeapp.dto.statistika.Sazetak;
import com.example.financeapp.model.Kategorija;
import com.example.financeapp.model.Novcanik;
import com.example.financeapp.model.Transakcija;
import com.example.financeapp.service.NovcanikService;
import com.example.financeapp.service.StatistikaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/novcanici")
public class NovcanikController {

    @Autowired
    private NovcanikService novcanikService;

    @Autowired
    private StatistikaService statistikaService;

    // CRUD Endpoints
    @GetMapping("/{korisnikId}")
    public ResponseEntity<List<Novcanik>> getAllForKorisnik(@PathVariable Long korisnikId) {
        return ResponseEntity.ok(novcanikService.findAllByKorisnikId(korisnikId));
    }

    @GetMapping("/{korisnikId}/{id}")
    public ResponseEntity<Novcanik> getOneForKorisnik(@PathVariable Long korisnikId, @PathVariable Long id) {
        return ResponseEntity.ok(novcanikService.findNovcanikByIdAndKorisnikId(id, korisnikId));
    }

    @PostMapping("/{korisnikId}")
    public ResponseEntity<Novcanik> createNovcanik(@PathVariable Long korisnikId, @RequestBody NovcanikRequest request) {
        return ResponseEntity.ok(novcanikService.createNovcanikWithValutaKod(request, korisnikId));
    }

    @PutMapping("/{korisnikId}/{id}")
    public ResponseEntity<Novcanik> updateNovcanik(@PathVariable Long korisnikId, @PathVariable Long id, @RequestBody NovcanikRequest request) {
        return ResponseEntity.ok(novcanikService.updateNovcanik(id, korisnikId, request));
    }

    @DeleteMapping("/{korisnikId}/{id}")
    public ResponseEntity<Void> deleteNovcanik(@PathVariable Long korisnikId, @PathVariable Long id) {
        novcanikService.deleteNovcanikForKorisnik(id, korisnikId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{korisnikId}/{id}/arhiviraj")
    public ResponseEntity<Novcanik> archiveNovcanik(@PathVariable Long korisnikId, @PathVariable Long id) {
        return ResponseEntity.ok(novcanikService.archiveNovcanikForKorisnik(id, korisnikId));
    }

    // Statistika Endpoints
    @GetMapping("/{korisnikId}/statistike/pregled")
    public ResponseEntity<Sazetak> getPregledStatistike(
            @PathVariable Long korisnikId,
            @RequestParam Period period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referenca,
            @RequestParam(required = false) Long kategorijaId,
            @RequestParam(required = false) BigDecimal minIznos,
            @RequestParam(required = false) BigDecimal maxIznos,
            @RequestParam(required = false) Kategorija.Tip tip) {
        Sazetak sazetak = statistikaService.pregledZaPeriod(korisnikId, period, referenca, kategorijaId, minIznos, maxIznos, tip);
        return ResponseEntity.ok(sazetak);
    }

    @GetMapping("/{korisnikId}/statistike/kategorije")
    public ResponseEntity<List<PoKategorijama>> getStatistikaPoKategorijama(
            @PathVariable Long korisnikId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pocetak,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate kraj,
            @RequestParam(required = false) Kategorija.Tip tip,
            @RequestParam(required = false) Long kategorijaId,
            @RequestParam(required = false) BigDecimal minIznos,
            @RequestParam(required = false) BigDecimal maxIznos) {
        List<PoKategorijama> podaci = statistikaService.grafPoKategorijama(korisnikId, pocetak, kraj, tip, kategorijaId, minIznos, maxIznos);
        return ResponseEntity.ok(podaci);
    }

    @GetMapping("/{korisnikId}/statistike/top-troskovi")
    public ResponseEntity<List<Transakcija>> getTopTroskovi(
            @PathVariable Long korisnikId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pocetak,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate kraj,
            @RequestParam(required = false) Long kategorijaId,
            @RequestParam(required = false) BigDecimal minIznos,
            @RequestParam(required = false) BigDecimal maxIznos,
            @RequestParam(defaultValue = "10") int limit) {
        List<Transakcija> podaci = statistikaService.topTroskovi(korisnikId, pocetak, kraj, kategorijaId, minIznos, maxIznos, limit);
        return ResponseEntity.ok(podaci);
    }
}
