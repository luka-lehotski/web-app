package com.example.financeapp.controller;

import com.example.financeapp.dto.KorisnikUpdateRequest;
import com.example.financeapp.dto.PasswordChangeRequest;
import com.example.financeapp.model.Korisnik;
import com.example.financeapp.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class KorisnikController {

    @Autowired
    private KorisnikService korisnikService;

    // Pregled profila
    @GetMapping("/{id}/profil")
    public ResponseEntity<Korisnik> getProfil(@PathVariable Long id) {
        Korisnik korisnik = korisnikService.findKorisnikById(id);
        return ResponseEntity.ok(korisnik);
    }

    // Izmena profila
    @PatchMapping("/{id}/profil")
    public ResponseEntity<Korisnik> updateProfil(@PathVariable Long id, @RequestBody KorisnikUpdateRequest request) {
        Korisnik azuriraniKorisnik = korisnikService.updateKorisnik(id, request);
        return ResponseEntity.ok(azuriraniKorisnik);
    }

    // Promena lozinke
    @PatchMapping("/{id}/profil/lozinka")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
        try {
            korisnikService.changePassword(id, request);
            return ResponseEntity.ok("Lozinka je uspešno promenjena.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Provera da li je profil kompletan
    @GetMapping("/{id}/profil/kompletan")
    public ResponseEntity<Boolean> isProfilKompletan(@PathVariable Long id) {
        Korisnik korisnik = korisnikService.findKorisnikById(id);
        // Logika preuzeta iz primera, prilagođena našem modelu
        boolean kompletan = korisnik.getIme() != null && !korisnik.getIme().isBlank()
                && korisnik.getPrezime() != null && !korisnik.getPrezime().isBlank()
                && korisnik.getDatumRodjenja() != null
                && korisnik.getValuta() != null;
        return ResponseEntity.ok(kompletan);
    }
}
