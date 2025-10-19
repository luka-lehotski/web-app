package com.example.financeapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Transakcija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private BigDecimal iznos;

    @Enumerated(EnumType.STRING)
    private Kategorija.Tip tip;

    @ManyToOne
    private Kategorija kategorija;

    private Date datumTransakcije;

    // Polja za ponavljajuće transakcije
    private boolean ponavljajuca; // Da li je ovo šablon?
    private boolean aktivna;      // Da li je šablon aktivan?
    private Date vaziDo;          // Datum do kog šablon važi

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

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    public BigDecimal getIznos() { return iznos; }
    public void setIznos(BigDecimal iznos) { this.iznos = iznos; }
    public Kategorija.Tip getTip() { return tip; }
    public void setTip(Kategorija.Tip tip) { this.tip = tip; }
    public Kategorija getKategorija() { return kategorija; }
    public void setKategorija(Kategorija kategorija) { this.kategorija = kategorija; }
    public Date getDatumTransakcije() { return datumTransakcije; }
    public void setDatumTransakcije(Date datumTransakcije) { this.datumTransakcije = datumTransakcije; }
    public boolean isPonavljajuca() { return ponavljajuca; }
    public void setPonavljajuca(boolean ponavljajuca) { this.ponavljajuca = ponavljajuca; }
    public boolean isAktivna() { return aktivna; }
    public void setAktivna(boolean aktivna) { this.aktivna = aktivna; }
    public Date getVaziDo() { return vaziDo; }
    public void setVaziDo(Date vaziDo) { this.vaziDo = vaziDo; }
    public Ucestalost getUcestalost() { return ucestalost; }
    public void setUcestalost(Ucestalost ucestalost) { this.ucestalost = ucestalost; }
    public Novcanik getNovcanik() { return novcanik; }
    public void setNovcanik(Novcanik novcanik) { this.novcanik = novcanik; }
    public Korisnik getKorisnik() { return korisnik; }
    public void setKorisnik(Korisnik korisnik) { this.korisnik = korisnik; }
}
