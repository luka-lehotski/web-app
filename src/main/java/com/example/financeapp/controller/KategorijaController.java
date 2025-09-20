package com.example.financeapp.controller;

import com.example.financeapp.service.KategorijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kategorija")
public class KategorijaController {

    @Autowired
    private KategorijaService kategorijaService;

    // We will add methods for category management here

}
