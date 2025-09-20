package com.example.financeapp.controller;

import com.example.financeapp.service.CiljStednjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cilj-stednje")
public class CiljStednjeController {

    @Autowired
    private CiljStednjeService ciljStednjeService;

    // We will add methods for savings goal management here

}
