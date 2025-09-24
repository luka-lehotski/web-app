package com.example.financeapp.service;

import com.example.financeapp.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KorisnikService {

    @Autowired
    private KorisnikRepository korisnikRepository;

    public long countRegisteredUsers() {
        return korisnikRepository.count();
    }
}
