package com.example.financeapp.controller;

import com.example.financeapp.config.JwtUtil;
import com.example.financeapp.model.Korisnik;
import com.example.financeapp.repository.KorisnikRepository;
import com.example.financeapp.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private KorisnikRepository korisnikRepository;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private JwtUtil jwtUtil;

    // DTOs definisani kao unutrašnje klase
    public static class RegisterRequest {
        public String korisnickoIme;
        public String mejlAdresa;
        public String lozinka;
    }

    public static class LoginRequest {
        public String usernameOrEmail;
        public String lozinka;
    }

    public record AuthResponse(String token, String uloga) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest r) {
        if (r.korisnickoIme == null || r.korisnickoIme.length() < 3) {
            return ResponseEntity.badRequest().body("Korisničko ime mora imati najmanje 3 karaktera.");
        }
        if (korisnikRepository.findByKorisnickoImeIgnoreCase(r.korisnickoIme).isPresent()) {
            return ResponseEntity.badRequest().body("Korisničko ime već postoji.");
        }
        if (korisnikRepository.findByMejlAdresaIgnoreCase(r.mejlAdresa).isPresent()) {
            return ResponseEntity.badRequest().body("Email adresa već postoji.");
        }
        if (r.lozinka == null || r.lozinka.length() < 8) {
            return ResponseEntity.badRequest().body("Lozinka mora imati najmanje 8 karaktera.");
        }

        Korisnik k = new Korisnik();
        k.setKorisnickoIme(r.korisnickoIme);
        k.setMejlAdresa(r.mejlAdresa);
        k.setLozinka(passwordService.hash(r.lozinka));
        k.setUloga(Korisnik.Uloga.KORISNIK); // Svi su korisnici po defaultu
        k.setDatumRegistracije(new Date());
        k.setBlokiran(false);

        Korisnik sacuvanKorisnik = korisnikRepository.save(k);

        String token = jwtUtil.generate(sacuvanKorisnik.getId(), sacuvanKorisnik.getUloga().name());
        return ResponseEntity.ok(new AuthResponse(token, sacuvanKorisnik.getUloga().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest r) {
        Korisnik korisnik = korisnikRepository.findByKorisnickoImeIgnoreCase(r.usernameOrEmail)
                .or(() -> korisnikRepository.findByMejlAdresaIgnoreCase(r.usernameOrEmail))
                .orElse(null);

        if (korisnik == null || !passwordService.matches(r.lozinka, korisnik.getLozinka())) {
            return ResponseEntity.status(401).body("Pogrešni kredencijali.");
        }
        if (korisnik.isBlokiran()) {
            return ResponseEntity.status(403).body("Nalog je blokiran.");
        }

        String token = jwtUtil.generate(korisnik.getId(), korisnik.getUloga().name());
        return ResponseEntity.ok(new AuthResponse(token, korisnik.getUloga().name()));
    }
}
