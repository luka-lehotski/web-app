package com.example.financeapp.service;

import com.example.financeapp.model.Kategorija;
import com.example.financeapp.repository.KategorijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KategorijaService {

    @Autowired
    private KategorijaRepository kategorijaRepository;

    // We will add methods for category management here

}
