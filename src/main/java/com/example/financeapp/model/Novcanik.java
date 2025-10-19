package com.example.financeapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Novcanik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private BigDecimal pocetnoStanje;
    private BigDecimal trenutnoStanje;

    @ManyToOne
    private Valuta valuta;

    private Date datumKreiranja;

    @ManyToOne
    private Korisnik korisnik;

    private boolean stedni;
    private boolean arhiviran;

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

    public BigDecimal getPocetnoStanje() {
        return pocetnoStanje;
    }

    public void setPocetnoStanje(BigDecimal pocetnoStanje) {
        this.pocetnoStanje = pocetnoStanje;
    }

    public BigDecimal getTrenutnoStanje() {
        return trenutnoStanje;
    }

    public void setTrenutnoStanje(BigDecimal trenutnoStanje) {
        this.trenutnoStanje = trenutnoStanje;
    }

    public Valuta getValuta() {
        return valuta;
    }

    public void setValuta(Valuta valuta) {
        this.valuta = valuta;
    }

    public Date getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(Date datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public boolean isStedni() {
        return stedni;
    }

    public void setStedni(boolean stedni) {
        this.stedni = stedni;
    }

    public boolean isArhiviran() {
        return arhiviran;
    }

    public void setArhiviran(boolean arhiviran) {
        this.arhiviran = arhiviran;
    }
}
