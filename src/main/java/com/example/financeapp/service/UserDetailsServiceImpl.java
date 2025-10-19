package com.example.financeapp.service;

import com.example.financeapp.model.Korisnik;
import com.example.financeapp.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Korisnik korisnik = korisnikRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronađen sa ID: " + userId));

        return new User(
                korisnik.getId().toString(),
                korisnik.getLozinka(),
                Collections.singletonList(new SimpleGrantedAuthority(korisnik.getUloga().name()))
        );
    }
}
