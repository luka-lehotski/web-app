package com.example.financeapp.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Transakcija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private Double iznos;

    @Enumerated(EnumType.STRING)
    private Kategorija.Tip tip;

    @ManyToOne
    private Kategorija kategorija;

    private Date datumTransakcije;
    private boolean ponavljajuca;

    @Enumerated(EnumType.STRING)
    private Ucestalost ucestalost;

    @ManyToOne
    private Novcanik novcanik;

    @ManyToOne
    private Korisnik korisnik;

    public enum Ucestalost {
        DNEVNO,
        NEDELJNO,
        MESECNO,
        GODISNJE
    }

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

    public Double getIznos() {
        return iznos;
    }

    public void setIznos(Double iznos) {
        this.iznos = iznos;
    }

    public Kategorija.Tip getTip() {
        return tip;
    }

    public void setTip(Kategorija.Tip tip) {
        this.tip = tip;
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

    public Date getDatumTransakcije() {
        return datumTransakcije;
    }

    public void setDatumTransakcije(Date datumTransakcije) {
        this.datumTransakcije = datumTransakcije;
    }

    public boolean isPonavljajuca() {
        return ponavljajuca;
    }

    public void setPonavljajuca(boolean ponavljajuca) {
        this.ponavljajuca = ponavljajuca;
    }

    public Ucestalost getUcestalost() {
        return ucestalost;
    }

    public void setUcestalost(Ucestalost ucestalost) {
        this.ucestalost = ucestalost;
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
