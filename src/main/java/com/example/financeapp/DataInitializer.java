package com.example.financeapp;

import com.example.financeapp.model.Kategorija;
import com.example.financeapp.model.Korisnik;
import com.example.financeapp.model.Valuta;
import com.example.financeapp.repository.KategorijaRepository;
import com.example.financeapp.repository.KorisnikRepository;
import com.example.financeapp.repository.ValutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private KategorijaRepository kategorijaRepository;

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private ValutaRepository valutaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (korisnikRepository.count() == 0) {
            System.out.println("Creating test user...");
            Korisnik testUser = new Korisnik();
            testUser.setKorisnickoIme("testuser");
            testUser.setIme("Test");
            testUser.setPrezime("User");
            testUser.setMejlAdresa("test@example.com");
            testUser.setLozinka("password");
            testUser.setUloga(Korisnik.Uloga.KORISNIK);
            testUser.setDatumRegistracije(new Date());
            korisnikRepository.save(testUser);
            System.out.println("Test user created with id=1");
        }


        if (valutaRepository.count() == 0) {
            System.out.println("Creating test currency...");
            Valuta eur = new Valuta();
            eur.setNaziv("EUR");
            eur.setVrednost(BigDecimal.ONE); // Correctly using BigDecimal
            valutaRepository.save(eur);
            System.out.println("Test currency EUR created with id=1");
        }


        if (kategorijaRepository.countByKorisnikIsNull() == 0) {
            System.out.println("Creating predefined categories...");

            Kategorija hrana = new Kategorija();
            hrana.setNaziv("Hrana");
            hrana.setTip(Kategorija.Tip.TROSAK);
            hrana.setPredefinisan(true);

            Kategorija plata = new Kategorija();
            plata.setNaziv("Plata");
            plata.setTip(Kategorija.Tip.PRIHOD);
            plata.setPredefinisan(true);

            kategorijaRepository.saveAll(List.of(hrana, plata));

            System.out.println("Predefined categories created.");
        }
    }
}
