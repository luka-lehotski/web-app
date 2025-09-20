package com.example.financeapp;

import com.example.financeapp.model.Kategorija;
import com.example.financeapp.repository.KategorijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private KategorijaRepository kategorijaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if predefined categories already exist
        if (kategorijaRepository.countByKorisnikIsNull() == 0) {
            System.out.println("Creating predefined categories...");

            Kategorija hrana = new Kategorija();
            hrana.setNaziv("Hrana");
            hrana.setTip(Kategorija.Tip.TROSAK);
            hrana.setPredefinisan(true);

            Kategorija zabava = new Kategorija();
            zabava.setNaziv("Zabava");
            zabava.setTip(Kategorija.Tip.TROSAK);
            zabava.setPredefinisan(true);

            Kategorija domacinstvo = new Kategorija();
            domacinstvo.setNaziv("Domaćinstvo");
            domacinstvo.setTip(Kategorija.Tip.TROSAK);
            domacinstvo.setPredefinisan(true);

            Kategorija plata = new Kategorija();
            plata.setNaziv("Plata");
            plata.setTip(Kategorija.Tip.PRIHOD);
            plata.setPredefinisan(true);

            kategorijaRepository.saveAll(List.of(hrana, zabava, domacinstvo, plata));

            System.out.println("Predefined categories created.");
        }
    }
}
