package com.example.financeapp.controller;

import com.example.financeapp.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class KorisnikController {

    @Autowired
    private KorisnikService korisnikService;

    @GetMapping("/count")
    public long countRegisteredUsers() {
        return korisnikService.countRegisteredUsers();
    }
}
