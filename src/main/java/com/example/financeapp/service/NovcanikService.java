package com.example.financeapp.service;

import com.example.financeapp.model.Novcanik;
import com.example.financeapp.repository.NovcanikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NovcanikService {

    @Autowired
    private NovcanikRepository novcanikRepository;

    // We will add methods for wallet management here

}
