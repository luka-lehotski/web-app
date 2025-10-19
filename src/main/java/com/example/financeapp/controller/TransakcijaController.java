package com.example.financeapp.controller;

import com.example.financeapp.dto.TransferRequest;
import com.example.financeapp.dto.statistika.Period;
import com.example.financeapp.model.Transakcija;
import com.example.financeapp.service.PonavljajucaTransakcijaService;
import com.example.financeapp.service.TransakcijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transakcije")
public class TransakcijaController {

    @Autowired
    private TransakcijaService transakcijaService;

    @Autowired
    private PonavljajucaTransakcijaService ponavljajucaTransakcijaService;

    // ... (postojeći endpointi ostaju isti)
    @PostMapping("/{korisnikId}/{novcanikId}/{kategorijaId}")
    public Transakcija createTransakcija(@RequestBody Transakcija transakcija, @PathVariable Long korisnikId, @PathVariable Long novcanikId, @PathVariable Long kategorijaId) { return transakcijaService.createTransakcija(transakcija, korisnikId, novcanikId, kategorijaId); }
    @PostMapping("/{korisnikId}/transfer")
    public ResponseEntity<Void> transfer(@PathVariable Long korisnikId, @RequestBody TransferRequest req) { /* ... */ return ResponseEntity.noContent().build(); }
    @GetMapping("/{korisnikId}/novcanik/{novcanikId}/stranica")
    public ResponseEntity<List<Transakcija>> getTransakcijePoStranicama(@PathVariable Long korisnikId, @PathVariable Long novcanikId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) { return ResponseEntity.ok(transakcijaService.transakcijePoStranicama(novcanikId, korisnikId, page, size)); }
    @GetMapping("/{korisnikId}/novcanik/{novcanikId}/pregled")
    public ResponseEntity<List<Transakcija>> pregledPoPeriodu(@PathVariable Long korisnikId, @PathVariable Long novcanikId, @RequestParam Period period, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referenca) { return ResponseEntity.ok(transakcijaService.pregledPoPeriodu(novcanikId, korisnikId, period, referenca)); }
    @GetMapping("/novcanik/{novcanikId}")
    public List<Transakcija> getTransakcijeByNovcanikId(@PathVariable Long novcanikId) { return transakcijaService.getTransakcijeByNovcanikId(novcanikId); }
    @GetMapping("/{korisnikId}/filter")
    public List<Transakcija> getFilteredTransactions(@PathVariable Long korisnikId, @RequestParam(required = false) Long novcanikId, @RequestParam(required = false) Long kategorijaId, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate, @RequestParam(required = false) BigDecimal minIznos, @RequestParam(required = false) BigDecimal maxIznos) { return transakcijaService.getFilteredTransactions(korisnikId, novcanikId, kategorijaId, startDate, endDate, minIznos, maxIznos); }

    // --- Endpointi za ponavljajuće transakcije ---

    // Kreiranje šablona
    @PostMapping("/{korisnikId}/ponavljajuce")
    public ResponseEntity<Transakcija> createSablon(@PathVariable Long korisnikId, @RequestBody Transakcija zahtev) {
        Transakcija sablon = ponavljajucaTransakcijaService.kreirajSablon(zahtev, korisnikId);
        return ResponseEntity.ok(sablon);
    }

    // Lista svih šablona za korisnika
    @GetMapping("/{korisnikId}/ponavljajuce")
    public ResponseEntity<List<Transakcija>> getSabloni(@PathVariable Long korisnikId) {
        List<Transakcija> sabloni = ponavljajucaTransakcijaService.listaSablona(korisnikId);
        return ResponseEntity.ok(sabloni);
    }

    // Uključivanje šablona
    @PutMapping("/{korisnikId}/ponavljajuce/{sablonId}/ukljuci")
    public ResponseEntity<Transakcija> ukljuciSablon(@PathVariable Long korisnikId, @PathVariable Long sablonId) {
        try {
            Transakcija sablon = ponavljajucaTransakcijaService.ukljuci(sablonId, korisnikId);
            return ResponseEntity.ok(sablon);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Isključivanje šablona
    @PutMapping("/{korisnikId}/ponavljajuce/{sablonId}/iskljuci")
    public ResponseEntity<Transakcija> iskljuciSablon(@PathVariable Long korisnikId, @PathVariable Long sablonId) {
        try {
            Transakcija sablon = ponavljajucaTransakcijaService.iskljuci(sablonId, korisnikId);
            return ResponseEntity.ok(sablon);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
