package com.example.financeapp;

import com.example.financeapp.model.Kategorija;
import com.example.financeapp.model.Korisnik;
import com.example.financeapp.model.Novcanik;
import com.example.financeapp.model.Valuta;
import com.example.financeapp.repository.KategorijaRepository;
import com.example.financeapp.repository.KorisnikRepository;
import com.example.financeapp.repository.NovcanikRepository;
import com.example.financeapp.repository.ValutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private NovcanikRepository novcanikRepository; // DODATO
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Kreiranje podataka samo ako je baza potpuno prazna
        if (korisnikRepository.count() == 0) {
            System.out.println("Creating test data...");

            // 1. Kreiraj Korisnika
            Korisnik testUser = new Korisnik();
            testUser.setKorisnickoIme("testuser");
            testUser.setIme("Test");
            testUser.setPrezime("User");
            testUser.setMejlAdresa("test@example.com");
            testUser.setLozinka(passwordEncoder.encode("password"));
            testUser.setUloga(Korisnik.Uloga.KORISNIK);
            testUser.setDatumRegistracije(new Date());
            testUser.setBlokiran(false);
            testUser = korisnikRepository.save(testUser);

            // 2. Kreiraj Valutu
            Valuta eur = new Valuta();
            eur.setNaziv("EUR");
            eur.setVrednost(BigDecimal.ONE);
            eur = valutaRepository.save(eur);

            // 3. Kreiraj Novčanik za korisnika
            Novcanik novcanik = new Novcanik();
            novcanik.setNaziv("Glavni račun");
            novcanik.setKorisnik(testUser);
            novcanik.setValuta(eur);
            novcanik.setPocetnoStanje(new BigDecimal("1000.00"));
            novcanik.setTrenutnoStanje(new BigDecimal("1000.00"));
            novcanik.setDatumKreiranja(new Date());
            novcanik.setStedni(false);
            novcanik.setArhiviran(false);
            novcanikRepository.save(novcanik);

            // 4. Kreiraj Kategorije
            Kategorija hrana = new Kategorija();
            hrana.setNaziv("Hrana");
            hrana.setTip(Kategorija.Tip.TROSAK);
            hrana.setPredefinisan(true);

            Kategorija plata = new Kategorija();
            plata.setNaziv("Plata");
            plata.setTip(Kategorija.Tip.PRIHOD);
            plata.setPredefinisan(true);

            kategorijaRepository.saveAll(List.of(hrana, plata));

            System.out.println("Test data created.");
        }
    }
}
