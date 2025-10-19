package com.example.financeapp.repository;

import com.example.financeapp.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
    Optional<Korisnik> findByKorisnickoImeIgnoreCase(String korisnickoIme);
    Optional<Korisnik> findByMejlAdresaIgnoreCase(String mejlAdresa);
}
