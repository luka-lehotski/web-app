package com.example.financeapp.controller;

import com.example.financeapp.model.Valuta;
import com.example.financeapp.repository.ValutaRepository;
import com.example.financeapp.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private ValutaRepository valutaRepository;

    // DTO za odgovor
    public record CountResponse(long count) {}

    @GetMapping("/users/count")
    public ResponseEntity<CountResponse> getBrojKorisnika() {
        long count = korisnikService.countRegisteredUsers();
        return ResponseEntity.ok(new CountResponse(count));
    }

    @GetMapping("/currencies")
    public ResponseEntity<List<Valuta>> getAllCurrencies() {
        List<Valuta> currencies = valutaRepository.findAll();
        return ResponseEntity.ok(currencies);
    }
}
