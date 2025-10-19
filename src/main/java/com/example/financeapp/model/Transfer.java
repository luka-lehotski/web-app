package com.example.financeapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Novcanik fromNovcanik;

    @ManyToOne(optional = false)
    private Novcanik toNovcanik;

    @Column(nullable = false)
    private BigDecimal iznos;

    private BigDecimal konverzioniKurs;

    @Column(nullable = false)
    private Date datum;

    @ManyToOne(optional = false)
    private Korisnik korisnik;

    // Veza ka transakcijama koje su nastale iz ovog transfera
    @OneToOne
    private Transakcija fromTransakcija;

    @OneToOne
    private Transakcija toTransakcija;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Novcanik getFromNovcanik() { return fromNovcanik; }
    public void setFromNovcanik(Novcanik fromNovcanik) { this.fromNovcanik = fromNovcanik; }
    public Novcanik getToNovcanik() { return toNovcanik; }
    public void setToNovcanik(Novcanik toNovcanik) { this.toNovcanik = toNovcanik; }
    public BigDecimal getIznos() { return iznos; }
    public void setIznos(BigDecimal iznos) { this.iznos = iznos; }
    public BigDecimal getKonverzioniKurs() { return konverzioniKurs; }
    public void setKonverzioniKurs(BigDecimal konverzioniKurs) { this.konverzioniKurs = konverzioniKurs; }
    public Date getDatum() { return datum; }
    public void setDatum(Date datum) { this.datum = datum; }
    public Korisnik getKorisnik() { return korisnik; }
    public void setKorisnik(Korisnik korisnik) { this.korisnik = korisnik; }
    public Transakcija getFromTransakcija() { return fromTransakcija; }
    public void setFromTransakcija(Transakcija fromTransakcija) { this.fromTransakcija = fromTransakcija; }
    public Transakcija getToTransakcija() { return toTransakcija; }
    public void setToTransakcija(Transakcija toTransakcija) { this.toTransakcija = toTransakcija; }
}
