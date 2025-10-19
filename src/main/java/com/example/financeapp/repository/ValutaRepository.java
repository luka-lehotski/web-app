package com.example.financeapp.repository;

import com.example.financeapp.model.Valuta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValutaRepository extends JpaRepository<Valuta, Long> {
    Optional<Valuta> findByNaziv(String naziv);
}
