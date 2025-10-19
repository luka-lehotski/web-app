package com.example.financeapp.service;

import com.example.financeapp.dto.NovcanikRequest;
import com.example.financeapp.model.Korisnik;
import com.example.financeapp.model.Novcanik;
import com.example.financeapp.model.Valuta;
import com.example.financeapp.repository.KorisnikRepository;
import com.example.financeapp.repository.NovcanikRepository;
import com.example.financeapp.repository.ValutaRepository;
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

    @Autowired
    private ValutaRepository valutaRepository;

    public List<Novcanik> findAllByKorisnikId(Long korisnikId) {
        return novcanikRepository.findAllByKorisnikId(korisnikId);
    }

    public Novcanik findNovcanikByIdAndKorisnikId(Long novcanikId, Long korisnikId) {
        Novcanik novcanik = novcanikRepository.findById(novcanikId)
                .orElseThrow(() -> new RuntimeException("Novčanik nije pronađen."));

        if (!novcanik.getKorisnik().getId().equals(korisnikId)) {
            throw new RuntimeException("Nemate dozvolu za pristup ovom novčaniku.");
        }
        return novcanik;
    }

    public Novcanik createNovcanikWithValutaKod(NovcanikRequest request, Long korisnikId) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));
        Valuta valuta = valutaRepository.findByNaziv(request.getValutaKod())
                .orElseThrow(() -> new RuntimeException("Valuta nije pronađena."));

        Novcanik novcanik = new Novcanik();
        novcanik.setNaziv(request.getNaziv());
        novcanik.setPocetnoStanje(request.getPocetnoStanje());
        novcanik.setTrenutnoStanje(request.getPocetnoStanje()); // Početno i trenutno stanje su isti pri kreiranju
        novcanik.setValuta(valuta);
        novcanik.setKorisnik(korisnik);
        novcanik.setStedni(request.isStedni());
        novcanik.setArhiviran(false);
        novcanik.setDatumKreiranja(new Date());

        return novcanikRepository.save(novcanik);
    }

    public Novcanik updateNovcanik(Long novcanikId, Long korisnikId, NovcanikRequest noviPodaci) {
        Novcanik postojeciNovcanik = findNovcanikByIdAndKorisnikId(novcanikId, korisnikId);

        postojeciNovcanik.setNaziv(noviPodaci.getNaziv());
        postojeciNovcanik.setStedni(noviPodaci.isStedni());
        // Ostala polja se obično ne menjaju direktno (npr. stanje)

        return novcanikRepository.save(postojeciNovcanik);
    }

    public void deleteNovcanikForKorisnik(Long novcanikId, Long korisnikId) {
        // Prvo provera vlasništva
        Novcanik novcanik = findNovcanikByIdAndKorisnikId(novcanikId, korisnikId);
        // TODO: Proveriti da li novčanik ima transakcije pre brisanja
        novcanikRepository.delete(novcanik);
    }

    public Novcanik archiveNovcanikForKorisnik(Long novcanikId, Long korisnikId) {
        Novcanik novcanik = findNovcanikByIdAndKorisnikId(novcanikId, korisnikId);
        novcanik.setArhiviran(true);
        return novcanikRepository.save(novcanik);
    }
}
