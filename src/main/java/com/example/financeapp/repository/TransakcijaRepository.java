package com.example.financeapp.repository;

import com.example.financeapp.model.Transakcija;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransakcijaRepository extends JpaRepository<Transakcija, Long> {
}
