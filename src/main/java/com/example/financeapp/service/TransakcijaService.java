package com.example.financeapp.service;

import com.example.financeapp.dto.statistika.Period;
import com.example.financeapp.model.*;
import com.example.financeapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
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
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private NovcanikService novcanikService;

    // ... (createTransakcija i transfer ostaju isti)
    @Transactional
    public Transakcija createTransakcija(Transakcija transakcija, Long korisnikId, Long novcanikId, Long kategorijaId) { /* ... */ return null; }
    @Transactional
    public void transfer(Long fromNovcanikId, Long toNovcanikId, BigDecimal iznos, Long korisnikId, Date datum) { /* ... */ }

    public List<Transakcija> getTransakcijeByNovcanikId(Long novcanikId) { return transakcijaRepository.findAllByNovcanikId(novcanikId); }

    // Metoda je ažurirana da prihvata i novcanikId
    public List<Transakcija> getFilteredTransactions(Long korisnikId, Long novcanikId, Long kategorijaId, Date startDate, Date endDate, BigDecimal minIznos, BigDecimal maxIznos) {
        return transakcijaRepository.findByKorisnikIdAndFilters(korisnikId, novcanikId, kategorijaId, startDate, endDate, minIznos, maxIznos);
    }

    public List<Transakcija> transakcijePoStranicama(Long novcanikId, Long korisnikId, int page, int size) {
        novcanikService.findNovcanikByIdAndKorisnikId(novcanikId, korisnikId);
        Pageable pageable = PageRequest.of(page, size);
        return transakcijaRepository.findAllByNovcanikIdAndKorisnikId(novcanikId, korisnikId, pageable);
    }

    // Nova metoda za pregled po periodu
    public List<Transakcija> pregledPoPeriodu(Long novcanikId, Long korisnikId, Period period, LocalDate referenca) {
        // Provera vlasništva
        novcanikService.findNovcanikByIdAndKorisnikId(novcanikId, korisnikId);

        if (referenca == null) {
            referenca = LocalDate.now();
        }

        LocalDate pocetak;
        LocalDate kraj;

        switch (period) {
            case DAN:
                pocetak = referenca;
                kraj = referenca;
                break;
            case NEDELJA:
                pocetak = referenca.with(java.time.DayOfWeek.MONDAY);
                kraj = referenca.with(java.time.DayOfWeek.SUNDAY);
                break;
            case MESEC:
                pocetak = referenca.with(TemporalAdjusters.firstDayOfMonth());
                kraj = referenca.with(TemporalAdjusters.lastDayOfMonth());
                break;
            case GODINA:
                pocetak = referenca.with(TemporalAdjusters.firstDayOfYear());
                kraj = referenca.with(TemporalAdjusters.lastDayOfYear());
                break;
            default:
                pocetak = null;
                kraj = null;
                break;
        }

        Date startDate = (pocetak != null) ? Date.from(pocetak.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
        Date endDate = (kraj != null) ? Date.from(kraj.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()) : null;

        return getFilteredTransactions(korisnikId, novcanikId, null, startDate, endDate, null, null);
    }
}
