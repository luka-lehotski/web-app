package com.example.financeapp.service;

import com.example.financeapp.model.Transakcija;
import com.example.financeapp.repository.TransakcijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class ScheduledTransactionService {

    @Autowired
    private TransakcijaRepository transakcijaRepository;

    @Autowired
    private TransakcijaService transakcijaService;

    @Scheduled(cron = "0 0 1 * * ?") // Runs every day at 1 AM
    @Transactional
    public void generateRecurringTransactions() {
        List<Transakcija> recurringTemplates = transakcijaRepository.findAllByPonavljajuca(true);

        for (Transakcija template : recurringTemplates) {
            // Proverava da li je šablon aktivan i da li je vreme za novu transakciju
            if (template.isAktivna() && isDue(template)) {
                Transakcija newTransaction = new Transakcija();
                newTransaction.setNaziv(template.getNaziv());
                newTransaction.setIznos(template.getIznos());
                newTransaction.setTip(template.getTip());
                newTransaction.setKategorija(template.getKategorija());
                newTransaction.setPonavljajuca(false); // Generisana transakcija nije šablon

                transakcijaService.createTransakcija(
                    newTransaction,
                    template.getKorisnik().getId(),
                    template.getNovcanik().getId(),
                    template.getKategorija().getId()
                );

                // Ažurira datum poslednjeg izvršenja na šablonu
                template.setDatumTransakcije(new Date());
                transakcijaRepository.save(template);
            }
        }
    }

    private boolean isDue(Transakcija transakcija) {
        LocalDate today = LocalDate.now();

        // Provera da li je šablon istekao
        if (transakcija.getVaziDo() != null) {
            LocalDate vaziDoDate = transakcija.getVaziDo().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (today.isAfter(vaziDoDate)) {
                return false; // Šablon je istekao
            }
        }

        LocalDate lastExecutionDate = transakcija.getDatumTransakcije().toInstant()
                                           .atZone(ZoneId.systemDefault())
                                           .toLocalDate();

        LocalDate nextDueDate;
        switch (transakcija.getUcestalost()) {
            case DNEVNO: nextDueDate = lastExecutionDate.plusDays(1); break;
            case NEDELJNO: nextDueDate = lastExecutionDate.plusWeeks(1); break;
            case MESECNO: nextDueDate = lastExecutionDate.plusMonths(1); break;
            case GODISNJE: nextDueDate = lastExecutionDate.plusYears(1); break;
            default: return false;
        }

        // Proverava da li je sledeći datum danas ili u prošlosti
        return !nextDueDate.isAfter(today);
    }
}
