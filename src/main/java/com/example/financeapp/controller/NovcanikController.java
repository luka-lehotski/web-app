package com.example.financeapp.controller;

import com.example.financeapp.model.Novcanik;
import com.example.financeapp.service.NovcanikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/novcanik")
public class NovcanikController {

    @Autowired
    private NovcanikService novcanikService;

    // We will add methods for wallet management here

}
