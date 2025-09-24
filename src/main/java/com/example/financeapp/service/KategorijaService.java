package com.example.financeapp.service;

import com.example.financeapp.model.Kategorija;
import com.example.financeapp.model.Korisnik;
import com.example.financeapp.repository.KategorijaRepository;
import com.example.financeapp.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KategorijaService {

    @Autowired
    private KategorijaRepository kategorijaRepository;

    @Autowired
    private KorisnikRepository korisnikRepository;

    public Kategorija createCustomKategorija(Kategorija kategorija, Long korisnikId) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new RuntimeException("Korisnik not found"));
        kategorija.setKorisnik(korisnik);
        kategorija.setPredefinisan(false);
        return kategorijaRepository.save(kategorija);
    }

    public List<Kategorija> getKategorijeForKorisnik(Long korisnikId) {
        return kategorijaRepository.findByKorisnikIdOrKorisnikIsNull(korisnikId);
    }

}
