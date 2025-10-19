package com.example.financeapp.service;

import com.example.financeapp.model.CiljStednje;
import com.example.financeapp.model.Korisnik;
import com.example.financeapp.model.Novcanik;
import com.example.financeapp.repository.CiljStednjeRepository;
import com.example.financeapp.repository.KorisnikRepository;
import com.example.financeapp.repository.NovcanikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class CiljStednjeService {

    @Autowired
    private CiljStednjeRepository ciljStednjeRepository;

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private NovcanikRepository novcanikRepository;

    public CiljStednje createCiljStednje(Long korisnikId, Long novcanikId, String naziv, BigDecimal zeljeniIznos, Date rok) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        Novcanik novcanik = novcanikRepository.findById(novcanikId)
                .orElseThrow(() -> new RuntimeException("Novčanik nije pronađen."));

        if (!novcanik.isStedni()) {
            throw new IllegalArgumentException("Odabrani novčanik nije štedni novčanik.");
        }

        CiljStednje noviCilj = new CiljStednje();
        noviCilj.setKorisnik(korisnik);
        noviCilj.setNovcanik(novcanik);
        noviCilj.setNaziv(naziv);
        noviCilj.setZeljeniIznos(zeljeniIznos);
        noviCilj.setRok(rok);

        return ciljStednjeRepository.save(noviCilj);
    }

    public List<CiljStednje> getGoalsByKorisnikId(Long korisnikId) {
        return ciljStednjeRepository.findAllByKorisnikId(korisnikId);
    }

    public CiljStednje findCiljByIdAndKorisnikId(Long ciljId, Long korisnikId) {
        CiljStednje cilj = ciljStednjeRepository.findById(ciljId)
                .orElseThrow(() -> new RuntimeException("Cilj štednje nije pronađen."));

        if (!cilj.getKorisnik().getId().equals(korisnikId)) {
            throw new RuntimeException("Nemate dozvolu da vidite ovaj cilj.");
        }
        return cilj;
    }

    public double calculateNapredak(Long ciljId, Long korisnikId) {
        CiljStednje cilj = findCiljByIdAndKorisnikId(ciljId, korisnikId);

        BigDecimal trenutnoStanje = cilj.getNovcanik().getTrenutnoStanje();
        BigDecimal zeljeniIznos = cilj.getZeljeniIznos();

        if (zeljeniIznos == null || zeljeniIznos.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        BigDecimal procenat = trenutnoStanje.divide(zeljeniIznos, 4, RoundingMode.HALF_UP)
                                          .multiply(new BigDecimal("100"));

        return procenat.doubleValue();
    }
}
