package com.example.financeapp.repository;

import com.example.financeapp.model.Kategorija;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KategorijaRepository extends JpaRepository<Kategorija, Long> {

    List<Kategorija> findByKorisnikIdOrKorisnikIsNull(Long korisnikId);

    long countByKorisnikIsNull();
}
