package com.example.financeapp.dto;

import java.math.BigDecimal;

public class NovcanikRequest {
    public String naziv;
    public BigDecimal pocetnoStanje;
    public String valutaKod; // npr. "EUR", "RSD"
    public boolean arhiviran;
    public boolean stedni;

    // Getters and Setters
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    public BigDecimal getPocetnoStanje() { return pocetnoStanje; }
    public void setPocetnoStanje(BigDecimal pocetnoStanje) { this.pocetnoStanje = pocetnoStanje; }
    public String getValutaKod() { return valutaKod; }
    public void setValutaKod(String valutaKod) { this.valutaKod = valutaKod; }
    public boolean isArhiviran() { return arhiviran; }
    public void setArhiviran(boolean arhiviran) { this.arhiviran = arhiviran; }
    public boolean isStedni() { return stedni; }
    public void setStedni(boolean stedni) { this.stedni = stedni; }
}
