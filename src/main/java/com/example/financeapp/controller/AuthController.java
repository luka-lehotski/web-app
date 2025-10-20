package com.example.financeapp.controller;

import com.example.financeapp.config.JwtUtil;
import com.example.financeapp.model.Korisnik;
import com.example.financeapp.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private KorisnikRepository korisnikRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    // DTOs
    public static class RegisterRequest { public String korisnickoIme; public String mejlAdresa; public String lozinka; }
    public static class LoginRequest { public String usernameOrEmail; public String lozinka; }
    public record AuthResponse(String token, String uloga, Long userId) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest r) {
        // ... (register metoda ostaje ista)
        if (r.korisnickoIme == null || r.korisnickoIme.length() < 3) { return ResponseEntity.badRequest().body("Korisničko ime mora imati najmanje 3 karaktera."); }
        if (korisnikRepository.findByKorisnickoImeIgnoreCase(r.korisnickoIme).isPresent()) { return ResponseEntity.badRequest().body("Korisničko ime već postoji."); }
        if (korisnikRepository.findByMejlAdresaIgnoreCase(r.mejlAdresa).isPresent()) { return ResponseEntity.badRequest().body("Email adresa već postoji."); }
        if (r.lozinka == null || r.lozinka.length() < 8) { return ResponseEntity.badRequest().body("Lozinka mora imati najmanje 8 karaktera."); }
        Korisnik k = new Korisnik();
        k.setKorisnickoIme(r.korisnickoIme);
        k.setMejlAdresa(r.mejlAdresa);
        k.setLozinka(passwordEncoder.encode(r.lozinka));
        k.setUloga(Korisnik.Uloga.KORISNIK);
        k.setDatumRegistracije(new Date());
        k.setBlokiran(false);
        Korisnik sacuvanKorisnik = korisnikRepository.save(k);
        String token = jwtUtil.generate(sacuvanKorisnik.getId(), sacuvanKorisnik.getUloga().name());
        return ResponseEntity.ok(new AuthResponse(token, sacuvanKorisnik.getUloga().name(), sacuvanKorisnik.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest r) {
        System.out.println("\n--- POKUŠAJ PRIJAVE ---");
        System.out.println("Tražim korisnika: " + r.usernameOrEmail);

        Korisnik korisnik = korisnikRepository.findByKorisnickoImeIgnoreCase(r.usernameOrEmail)
                .or(() -> korisnikRepository.findByMejlAdresaIgnoreCase(r.usernameOrEmail))
                .orElse(null);

        if (korisnik == null) {
            System.out.println("REZULTAT: Korisnik NIJE pronađen u bazi.");
            return ResponseEntity.status(401).body("Pogrešni kredencijali.");
        }

        System.out.println("REZULTAT: Korisnik pronađen: " + korisnik.getKorisnickoIme());
        System.out.println("Lozinka iz zahteva (raw): " + r.lozinka);
        System.out.println("Lozinka iz baze (hash): " + korisnik.getLozinka());

        boolean daLiSeLozinkePoklapaju = passwordEncoder.matches(r.lozinka, korisnik.getLozinka());
        System.out.println("REZULTAT PROVERE (passwordEncoder.matches): " + daLiSeLozinkePoklapaju);

        if (!daLiSeLozinkePoklapaju) {
            System.out.println("--- KRAJ POKUŠAJA (Neuspešno) ---\n");
            return ResponseEntity.status(401).body("Pogrešni kredencijali.");
        }
        if (korisnik.isBlokiran()) {
            return ResponseEntity.status(403).body("Nalog je blokiran.");
        }

        String token = jwtUtil.generate(korisnik.getId(), korisnik.getUloga().name());
        System.out.println("--- KRAJ POKUŠAJA (Uspešno) ---\n");
        return ResponseEntity.ok(new AuthResponse(token, korisnik.getUloga().name(), korisnik.getId()));
    }
}
