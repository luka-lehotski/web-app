package com.example.financeapp.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class CiljStednje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private Double zeljeniIznos;
    private Date rok;

    @ManyToOne
    private Novcanik novcanik; // The savings wallet for this goal

    @ManyToOne
    private Korisnik korisnik;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Double getZeljeniIznos() {
        return zeljeniIznos;
    }

    public void setZeljeniIznos(Double zeljeniIznos) {
        this.zeljeniIznos = zeljeniIznos;
    }

    public Date getRok() {
        return rok;
    }

    public void setRok(Date rok) {
        this.rok = rok;
    }

    public Novcanik getNovcanik() {
        return novcanik;
    }

    public void setNovcanik(Novcanik novcanik) {
        this.novcanik = novcanik;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
