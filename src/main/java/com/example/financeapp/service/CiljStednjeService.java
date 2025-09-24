package com.example.financeapp.service;

import com.example.financeapp.model.CiljStednje;
import com.example.financeapp.model.Korisnik;
import com.example.financeapp.model.Novcanik;
import com.example.financeapp.repository.CiljStednjeRepository;
import com.example.financeapp.repository.KorisnikRepository;
import com.example.financeapp.repository.NovcanikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CiljStednjeService {

    @Autowired
    private CiljStednjeRepository ciljStednjeRepository;

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private NovcanikRepository novcanikRepository;

    public CiljStednje createCiljStednje(CiljStednje ciljStednje, Long korisnikId, Long novcanikId) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new RuntimeException("Korisnik not found"));

        Novcanik novcanik = novcanikRepository.findById(novcanikId)
                .orElseThrow(() -> new RuntimeException("Novcanik not found"));

        // Specification: User must select or create a savings wallet
        if (!novcanik.isStedni()) {
            throw new IllegalArgumentException("Odabrani novčanik nije štedni novčanik.");
        }

        ciljStednje.setKorisnik(korisnik);
        ciljStednje.setNovcanik(novcanik);

        return ciljStednjeRepository.save(ciljStednje);
    }

    public List<CiljStednje> getGoalsByKorisnikId(Long korisnikId) {
        return ciljStednjeRepository.findAllByKorisnikId(korisnikId);
    }
}
