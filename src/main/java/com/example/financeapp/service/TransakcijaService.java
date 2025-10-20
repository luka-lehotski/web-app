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

    @Transactional
    public Transakcija createTransakcija(Transakcija transakcija, Long korisnikId, Long novcanikId, Long kategorijaId) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        Novcanik novcanik = novcanikRepository.findById(novcanikId)
                .orElseThrow(() -> new RuntimeException("Novčanik nije pronađen."));

        if (!novcanik.getKorisnik().getId().equals(korisnikId)) {
            throw new RuntimeException("Nemate dozvolu za pristup ovom novčaniku.");
        }

        Kategorija kategorija = kategorijaRepository.findById(kategorijaId)
                .orElseThrow(() -> new RuntimeException("Kategorija nije pronađena."));

        transakcija.setKorisnik(korisnik);
        transakcija.setNovcanik(novcanik);
        transakcija.setKategorija(kategorija);

        if (transakcija.getDatumTransakcije() == null) {
            transakcija.setDatumTransakcije(new Date());
        }

        // Ažuriranje stanja novčanika
        BigDecimal trenutnoStanje = novcanik.getTrenutnoStanje();
        if (transakcija.getTip() == Kategorija.Tip.PRIHOD) {
            novcanik.setTrenutnoStanje(trenutnoStanje.add(transakcija.getIznos()));
        } else {
            novcanik.setTrenutnoStanje(trenutnoStanje.subtract(transakcija.getIznos()));
        }

        novcanikRepository.save(novcanik);
        return transakcijaRepository.save(transakcija);
    }

    @Transactional
    public void transfer(Long fromNovcanikId, Long toNovcanikId, BigDecimal iznos, Long korisnikId, Date datum) {
        Novcanik fromNovcanik = novcanikRepository.findById(fromNovcanikId)
                .orElseThrow(() -> new RuntimeException("Izvorni novčanik nije pronađen."));

        Novcanik toNovcanik = novcanikRepository.findById(toNovcanikId)
                .orElseThrow(() -> new RuntimeException("Odredišni novčanik nije pronađen."));

        if (!fromNovcanik.getKorisnik().getId().equals(korisnikId) ||
                !toNovcanik.getKorisnik().getId().equals(korisnikId)) {
            throw new RuntimeException("Nemate dozvolu za pristup jednom od novčanika.");
        }

        Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        // Konverzija valuta ako je potrebno
        BigDecimal konverzioniKurs = BigDecimal.ONE;
        BigDecimal iznosUOdredisnoj = iznos;

        if (!fromNovcanik.getValuta().getId().equals(toNovcanik.getValuta().getId())) {
            konverzioniKurs = toNovcanik.getValuta().getVrednost()
                    .divide(fromNovcanik.getValuta().getVrednost(), 4, RoundingMode.HALF_UP);
            iznosUOdredisnoj = iznos.multiply(konverzioniKurs);
        }

        // Ažuriranje stanja novčanika
        fromNovcanik.setTrenutnoStanje(fromNovcanik.getTrenutnoStanje().subtract(iznos));
        toNovcanik.setTrenutnoStanje(toNovcanik.getTrenutnoStanje().add(iznosUOdredisnoj));

        novcanikRepository.save(fromNovcanik);
        novcanikRepository.save(toNovcanik);

        // Kreiranje transfera
        Transfer transfer = new Transfer();
        transfer.setFromNovcanik(fromNovcanik);
        transfer.setToNovcanik(toNovcanik);
        transfer.setIznos(iznos);
        transfer.setKonverzioniKurs(konverzioniKurs);
        transfer.setDatum(datum != null ? datum : new Date());
        transfer.setKorisnik(korisnik);

        transferRepository.save(transfer);
    }

    public List<Transakcija> getTransakcijeByNovcanikId(Long novcanikId) {
        return transakcijaRepository.findAllByNovcanikId(novcanikId);
    }

    public List<Transakcija> getFilteredTransactions(Long korisnikId, Long novcanikId, Long kategorijaId,
                                                     Date startDate, Date endDate,
                                                     BigDecimal minIznos, BigDecimal maxIznos) {
        return transakcijaRepository.findByKorisnikIdAndFilters(korisnikId, novcanikId, kategorijaId,
                startDate, endDate, minIznos, maxIznos);
    }

    public List<Transakcija> transakcijePoStranicama(Long novcanikId, Long korisnikId, int page, int size) {
        novcanikService.findNovcanikByIdAndKorisnikId(novcanikId, korisnikId);
        Pageable pageable = PageRequest.of(page, size);
        return transakcijaRepository.findAllByNovcanikIdAndKorisnikId(novcanikId, korisnikId, pageable);
    }

    public List<Transakcija> pregledPoPeriodu(Long novcanikId, Long korisnikId, Period period, LocalDate referenca) {
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