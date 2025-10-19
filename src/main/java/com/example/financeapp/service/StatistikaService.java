package com.example.financeapp.service;

import com.example.financeapp.dto.statistika.Period;
import com.example.financeapp.dto.statistika.PoKategorijama;
import com.example.financeapp.dto.statistika.Sazetak;
import com.example.financeapp.model.Kategorija;
import com.example.financeapp.model.Transakcija;
import com.example.financeapp.repository.TransakcijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Service
public class StatistikaService {

    @Autowired
    private TransakcijaRepository transakcijaRepository;

    public Sazetak pregledZaPeriod(Long korisnikId, Period period, LocalDate referenca, Long kategorijaId, BigDecimal minIznos, BigDecimal maxIznos, Kategorija.Tip tip) {
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
            case SVE:
            default:
                pocetak = null;
                kraj = null;
                break;
        }

        Date startDate = (pocetak != null) ? Date.from(pocetak.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
        Date endDate = (kraj != null) ? Date.from(kraj.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()) : null;

        BigDecimal prihodi = BigDecimal.ZERO;
        BigDecimal rashodi = BigDecimal.ZERO;

        if (tip == null || tip == Kategorija.Tip.PRIHOD) {
            prihodi = transakcijaRepository.sumIznosByTipAndKorisnikIdAndFilters(korisnikId, Kategorija.Tip.PRIHOD, startDate, endDate, kategorijaId, minIznos, maxIznos);
        }
        if (tip == null || tip == Kategorija.Tip.TROSAK) {
            rashodi = transakcijaRepository.sumIznosByTipAndKorisnikIdAndFilters(korisnikId, Kategorija.Tip.TROSAK, startDate, endDate, kategorijaId, minIznos, maxIznos);
        }

        return new Sazetak(prihodi, rashodi);
    }

    public List<PoKategorijama> grafPoKategorijama(Long korisnikId, LocalDate pocetak, LocalDate kraj, Kategorija.Tip tip, Long kategorijaId, BigDecimal minIznos, BigDecimal maxIznos) {
        Date startDate = (pocetak != null) ? Date.from(pocetak.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
        Date endDate = (kraj != null) ? Date.from(kraj.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()) : null;
        return transakcijaRepository.sumIznosGroupByKategorija(korisnikId, startDate, endDate, tip, kategorijaId, minIznos, maxIznos);
    }

    public List<Transakcija> topTroskovi(Long korisnikId, LocalDate pocetak, LocalDate kraj, Long kategorijaId, BigDecimal minIznos, BigDecimal maxIznos, int limit) {
        Date startDate = (pocetak != null) ? Date.from(pocetak.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
        Date endDate = (kraj != null) ? Date.from(kraj.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()) : null;
        Pageable pageable = PageRequest.of(0, limit);
        return transakcijaRepository.findTopTroskovi(korisnikId, startDate, endDate, kategorijaId, minIznos, maxIznos, pageable);
    }
}
