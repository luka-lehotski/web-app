package com.example.financeapp.controller;

import com.example.financeapp.model.CiljStednje;
import com.example.financeapp.service.CiljStednjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cilj-stednje")
public class CiljStednjeController {

    @Autowired
    private CiljStednjeService ciljStednjeService;

    @PostMapping("/{korisnikId}/{novcanikId}")
    public ResponseEntity<?> createCiljStednje(@RequestBody CiljStednje ciljStednje,
                                               @PathVariable Long korisnikId,
                                               @PathVariable Long novcanikId) {
        try {
            CiljStednje noviCilj = ciljStednjeService.createCiljStednje(ciljStednje, korisnikId, novcanikId);
            return ResponseEntity.ok(noviCilj);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{korisnikId}")
    public List<CiljStednje> getGoalsByKorisnikId(@PathVariable Long korisnikId) {
        return ciljStednjeService.getGoalsByKorisnikId(korisnikId);
    }
}
