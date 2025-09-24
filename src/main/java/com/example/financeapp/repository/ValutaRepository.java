package com.example.financeapp.repository;

import com.example.financeapp.model.Valuta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValutaRepository extends JpaRepository<Valuta, Long> {
}
