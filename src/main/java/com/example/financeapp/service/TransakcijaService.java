package com.example.financeapp.service;

import com.example.financeapp.model.*;
import com.example.financeapp.repository.KategorijaRepository;
import com.example.financeapp.repository.KorisnikRepository;
import com.example.financeapp.repository.NovcanikRepository;
import com.example.financeapp.repository.TransakcijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TransakcijaService {

    @Autowired
    private TransakcijaRepository transakcijaRepository;

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private NovcanikRepository novcanikRepository;

    @Autowired
    private KategorijaRepository kategorijaRepository;

    @Transactional
    public Transakcija createTransakcija(Transakcija transakcija, Long korisnikId, Long novcanikId, Long kategorijaId) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId).orElseThrow(() -> new RuntimeException("Korisnik not found"));
        Novcanik novcanik = novcanikRepository.findById(novcanikId).orElseThrow(() -> new RuntimeException("Novcanik not found"));
        Kategorija kategorija = kategorijaRepository.findById(kategorijaId).orElseThrow(() -> new RuntimeException("Kategorija not found"));

        transakcija.setKorisnik(korisnik);
        transakcija.setNovcanik(novcanik);
        transakcija.setKategorija(kategorija);
        transakcija.setDatumTransakcije(new Date());

        // Update wallet balance
        if (transakcija.getTip() == Kategorija.Tip.PRIHOD) {
            novcanik.setTrenutnoStanje(novcanik.getTrenutnoStanje() + transakcija.getIznos());
        } else if (transakcija.getTip() == Kategorija.Tip.TROSAK) {
            novcanik.setTrenutnoStanje(novcanik.getTrenutnoStanje() - transakcija.getIznos());
        }
        novcanikRepository.save(novcanik);

        return transakcijaRepository.save(transakcija);
    }

    public List<Transakcija> getTransakcijeByNovcanikId(Long novcanikId) {
        return transakcijaRepository.findAllByNovcanikId(novcanikId);
    }

    public List<Transakcija> getFilteredTransactions(Long korisnikId, Long kategorijaId, Date startDate, Date endDate, Double minIznos, Double maxIznos) {
        return transakcijaRepository.findByKorisnikIdAndFilters(korisnikId, kategorijaId, startDate, endDate, minIznos, maxIznos);
    }
}
