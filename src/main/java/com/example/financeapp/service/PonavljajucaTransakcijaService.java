package com.example.financeapp.service;

import com.example.financeapp.model.Korisnik;
import com.example.financeapp.model.Transakcija;
import com.example.financeapp.repository.KorisnikRepository;
import com.example.financeapp.repository.TransakcijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PonavljajucaTransakcijaService {

    @Autowired
    private TransakcijaRepository transakcijaRepository;

    @Autowired
    private KorisnikRepository korisnikRepository;

    public Transakcija kreirajSablon(Transakcija zahtev, Long korisnikId) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        zahtev.setKorisnik(korisnik);
        zahtev.setPonavljajuca(true); // Ovo je šablon
        zahtev.setAktivna(true);      // Aktivan po defaultu

        // Osiguravamo da nema ID da ne bi došlo do ažuriranja postojećeg
        zahtev.setId(null);

        return transakcijaRepository.save(zahtev);
    }

    public Transakcija ukljuci(Long sablonId, Long korisnikId) {
        Transakcija sablon = findSablonByIdAndKorisnikId(sablonId, korisnikId);
        sablon.setAktivna(true);
        return transakcijaRepository.save(sablon);
    }

    public Transakcija iskljuci(Long sablonId, Long korisnikId) {
        Transakcija sablon = findSablonByIdAndKorisnikId(sablonId, korisnikId);
        sablon.setAktivna(false);
        return transakcijaRepository.save(sablon);
    }

    public List<Transakcija> listaSablona(Long korisnikId) {
        return transakcijaRepository.findAllByKorisnikIdAndPonavljajuca(korisnikId, true);
    }

    private Transakcija findSablonByIdAndKorisnikId(Long sablonId, Long korisnikId) {
        Transakcija sablon = transakcijaRepository.findById(sablonId)
                .orElseThrow(() -> new RuntimeException("Šablon nije pronađen."));

        if (!sablon.isPonavljajuca() || !sablon.getKorisnik().getId().equals(korisnikId)) {
            throw new SecurityException("Nemate dozvolu za izmenu ovog šablona.");
        }
        return sablon;
    }
}
