package com.example.financeapp.controller;

import com.example.financeapp.service.TransakcijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transakcija")
public class TransakcijaController {

    @Autowired
    private TransakcijaService transakcijaService;

    // We will add methods for transaction management here

}
