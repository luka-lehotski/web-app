package com.example.financeapp.service;

import com.example.financeapp.dto.KorisnikUpdateRequest;
import com.example.financeapp.dto.PasswordChangeRequest;
import com.example.financeapp.model.Korisnik;
import com.example.financeapp.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KorisnikService {

    @Autowired
    private KorisnikRepository korisnikRepository;

    public long countRegisteredUsers() {
        return korisnikRepository.count();
    }

    public Korisnik findKorisnikById(Long id) {
        return korisnikRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen sa id: " + id));
    }

    public Korisnik updateKorisnik(Long id, KorisnikUpdateRequest request) {
        Korisnik korisnik = findKorisnikById(id);

        if (request.getIme() != null && !request.getIme().isBlank()) {
            korisnik.setIme(request.getIme());
        }
        if (request.getPrezime() != null && !request.getPrezime().isBlank()) {
            korisnik.setPrezime(request.getPrezime());
        }
        if (request.getDatumRodjenja() != null) {
            korisnik.setDatumRodjenja(request.getDatumRodjenja());
        }
        if (request.getValuta() != null) {
            korisnik.setValuta(request.getValuta());
        }

        return korisnikRepository.save(korisnik);
    }

    public void changePassword(Long id, PasswordChangeRequest request) {
        Korisnik korisnik = findKorisnikById(id);

        // U realnoj aplikaciji, ovde bi se koristio PasswordEncoder za proveru lozinke
        if (!korisnik.getLozinka().equals(request.getOldPassword())) {
            throw new RuntimeException("Stara lozinka nije ispravna.");
        }

        // I ovde bi se nova lozinka enkodirala pre čuvanja
        korisnik.setLozinka(request.getNewPassword());
        korisnikRepository.save(korisnik);
    }
}
