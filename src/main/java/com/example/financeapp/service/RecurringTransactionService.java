package com.example.financeapp.service;

import com.example.financeapp.model.Transakcija;
import com.example.financeapp.repository.TransakcijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class RecurringTransactionService {

    @Autowired
    private TransakcijaRepository transakcijaRepository;

    @Autowired
    private TransakcijaService transakcijaService; // To reuse the creation logic

    // This will run every day at 1 AM
    @Scheduled(cron = "0 0 1 * * ?")
    public void generateRecurringTransactions() {
        List<Transakcija> recurringTransactions = transakcijaRepository.findAllByPonavljajuca(true);

        for (Transakcija template : recurringTransactions) {
            if (isDue(template)) {
                Transakcija newTransaction = new Transakcija();
                newTransaction.setNaziv(template.getNaziv());
                newTransaction.setIznos(template.getIznos());
                newTransaction.setTip(template.getTip());
                newTransaction.setPonavljajuca(false); // The new one is not a template

                transakcijaService.createTransakcija(newTransaction, template.getKorisnik().getId(), template.getNovcanik().getId(), template.getKategorija().getId());

                // Update the last transaction date on the template
                template.setDatumTransakcije(java.sql.Date.valueOf(LocalDate.now()));
                transakcijaRepository.save(template);
            }
        }
    }

    private boolean isDue(Transakcija transakcija) {
        LocalDate lastTransactionDate = transakcija.getDatumTransakcije().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();

        switch (transakcija.getUcestalost()) {
            case DNEVNO:
                return lastTransactionDate.isBefore(today);
            case NEDELJNO:
                return lastTransactionDate.plusWeeks(1).isBefore(today) || lastTransactionDate.plusWeeks(1).isEqual(today);
            case MESECNO:
                return lastTransactionDate.plusMonths(1).isBefore(today) || lastTransactionDate.plusMonths(1).isEqual(today);
            case GODISNJE:
                return lastTransactionDate.plusYears(1).isBefore(today) || lastTransactionDate.plusYears(1).isEqual(today);
            default:
                return false;
        }
    }
}
