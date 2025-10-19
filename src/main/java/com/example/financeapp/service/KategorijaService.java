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
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));
        kategorija.setKorisnik(korisnik);
        kategorija.setPredefinisan(false);
        return kategorijaRepository.save(kategorija);
    }

    public List<Kategorija> getKategorijeForKorisnik(Long korisnikId) {
        return kategorijaRepository.findByKorisnikIdOrKorisnikIsNull(korisnikId);
    }

    public void deleteSopstvenuKategoriju(Long korisnikId, Long kategorijaId) {
        Kategorija kategorija = kategorijaRepository.findById(kategorijaId)
                .orElseThrow(() -> new RuntimeException("Kategorija nije pronađena."));

        if (kategorija.isPredefinisan()) {
            throw new RuntimeException("Nije moguće obrisati predefinisanu kategoriju.");
        }

        if (!kategorija.getKorisnik().getId().equals(korisnikId)) {
            throw new RuntimeException("Nemate dozvolu da obrišete ovu kategoriju.");
        }

        kategorijaRepository.delete(kategorija);
    }

    public Kategorija overridePredefinisanuKategoriju(Long korisnikId, Long predefinisanaKategorijaId, Kategorija podaci) {
        Kategorija predefinisana = kategorijaRepository.findById(predefinisanaKategorijaId)
                .orElseThrow(() -> new RuntimeException("Predefinisana kategorija nije pronađena."));

        if (!predefinisana.isPredefinisan()) {
            throw new RuntimeException("Moguće je prepisati samo predefinisanu kategoriju.");
        }

        Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        // Kreiramo novu, korisničku kategoriju kao kopiju
        Kategorija novaKategorija = new Kategorija();
        novaKategorija.setNaziv(podaci.getNaziv());
        novaKategorija.setTip(podaci.getTip());
        novaKategorija.setKorisnik(korisnik);
        novaKategorija.setPredefinisan(false);

        return kategorijaRepository.save(novaKategorija);
    }
}
