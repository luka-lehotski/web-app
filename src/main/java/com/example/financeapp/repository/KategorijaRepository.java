package com.example.financeapp.repository;

import com.example.financeapp.model.Kategorija;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KategorijaRepository extends JpaRepository<Kategorija, Long> {
}
