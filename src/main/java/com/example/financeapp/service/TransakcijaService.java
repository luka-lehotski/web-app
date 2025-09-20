package com.example.financeapp.service;

import com.example.financeapp.repository.TransakcijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransakcijaService {

    @Autowired
    private TransakcijaRepository transakcijaRepository;

    // We will add methods for transaction management here

}
