package com.example.financeapp.service;

import com.example.financeapp.model.Korisnik;
import com.example.financeapp.model.Novcanik;
import com.example.financeapp.repository.KorisnikRepository;
import com.example.financeapp.repository.NovcanikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NovcanikService {

    @Autowired
    private NovcanikRepository novcanikRepository;

    @Autowired
    private KorisnikRepository korisnikRepository;

    public Novcanik createNovcanik(Novcanik novcanik, Long korisnikId) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId).orElseThrow(() -> new RuntimeException("Korisnik not found"));
        novcanik.setKorisnik(korisnik);
        novcanik.setDatumKreiranja(new Date());
        return novcanikRepository.save(novcanik);
    }

    public List<Novcanik> getNovcaniciByKorisnikId(Long korisnikId) {
        return novcanikRepository.findAllByKorisnikId(korisnikId);
    }

    public Novcanik updateNovcanik(Long novcanikId, Novcanik novcanikDetails) {
        Novcanik novcanik = novcanikRepository.findById(novcanikId).orElseThrow(() -> new RuntimeException("Novcanik not found"));

        novcanik.setNaziv(novcanikDetails.getNaziv());
        novcanik.setPocetnoStanje(novcanikDetails.getPocetnoStanje());
        novcanik.setStedni(novcanikDetails.isStedni());
        novcanik.setArhiviran(novcanikDetails.isArhiviran());

        return novcanikRepository.save(novcanik);
    }

    public void deleteNovcanik(Long novcanikId) {
        novcanikRepository.deleteById(novcanikId);
    }

}
