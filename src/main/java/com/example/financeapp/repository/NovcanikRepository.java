package com.example.financeapp.repository;

import com.example.financeapp.model.Novcanik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NovcanikRepository extends JpaRepository<Novcanik, Long> {

    List<Novcanik> findAllByKorisnikId(Long korisnikId);
}
