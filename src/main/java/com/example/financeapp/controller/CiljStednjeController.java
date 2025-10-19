package com.example.financeapp.controller;

import com.example.financeapp.model.CiljStednje;
import com.example.financeapp.service.CiljStednjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ciljevi") // Promenjeno u množinu
public class CiljStednjeController {

    @Autowired
    private CiljStednjeService ciljStednjeService;

    // Lista svih ciljeva korisnika
    @GetMapping("/{korisnikId}")
    public ResponseEntity<List<CiljStednje>> getGoalsByKorisnikId(@PathVariable Long korisnikId) {
        List<CiljStednje> ciljevi = ciljStednjeService.getGoalsByKorisnikId(korisnikId);
        return ResponseEntity.ok(ciljevi);
    }

    // Jedan cilj (samo ako pripada korisniku)
    @GetMapping("/{korisnikId}/{ciljId}")
    public ResponseEntity<CiljStednje> getCiljDetaljno(@PathVariable Long korisnikId, @PathVariable Long ciljId) {
        try {
            CiljStednje cilj = ciljStednjeService.findCiljByIdAndKorisnikId(ciljId, korisnikId);
            return ResponseEntity.ok(cilj);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Procenat napretka (backend za grafik)
    @GetMapping("/{korisnikId}/{ciljId}/napredak")
    public ResponseEntity<Map<String, Object>> getNapredak(@PathVariable Long korisnikId, @PathVariable Long ciljId) {
        try {
            double procenat = ciljStednjeService.calculateNapredak(ciljId, korisnikId);
            return ResponseEntity.ok(Map.of(
                    "ciljId", ciljId,
                    "procenat", procenat
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Kreiranje cilja
    @PostMapping("/{korisnikId}")
    public ResponseEntity<CiljStednje> createCiljStednje(
            @PathVariable Long korisnikId,
            @RequestParam Long novcanikId,
            @RequestParam String naziv,
            @RequestParam BigDecimal zeljeniIznos,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date rok) {
        try {
            CiljStednje noviCilj = ciljStednjeService.createCiljStednje(korisnikId, novcanikId, naziv, zeljeniIznos, rok);
            return ResponseEntity.ok(noviCilj);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
