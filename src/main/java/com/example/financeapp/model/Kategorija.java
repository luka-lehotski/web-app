package com.example.financeapp.model;

import jakarta.persistence.*;

@Entity
public class Kategorija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;

    @Enumerated(EnumType.STRING)
    private Tip tip;

    private boolean predefinisan;

    @ManyToOne
    private Korisnik korisnik;

    public enum Tip {
        PRIHOD,
        TROSAK
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

    public Tip getTip() {
        return tip;
    }

    public void setTip(Tip tip) {
        this.tip = tip;
    }

    public boolean isPredefinisan() {
        return predefinisan;
    }

    public void setPredefinisan(boolean predefinisan) {
        this.predefinisan = predefinisan;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
