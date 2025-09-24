package com.example.financeapp.repository;

import com.example.financeapp.model.CiljStednje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CiljStednjeRepository extends JpaRepository<CiljStednje, Long> {

    List<CiljStednje> findAllByKorisnikId(Long korisnikId);
}
