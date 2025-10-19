package com.example.financeapp.dto.statistika;

import java.math.BigDecimal;

public class PoKategorijama {
    private String kategorijaNaziv;
    private BigDecimal ukupanIznos;

    public PoKategorijama(String kategorijaNaziv, BigDecimal ukupanIznos) {
        this.kategorijaNaziv = kategorijaNaziv;
        this.ukupanIznos = ukupanIznos;
    }

    // Getters and Setters
    public String getKategorijaNaziv() { return kategorijaNaziv; }
    public void setKategorijaNaziv(String kategorijaNaziv) { this.kategorijaNaziv = kategorijaNaziv; }
    public BigDecimal getUkupanIznos() { return ukupanIznos; }
    public void setUkupanIznos(BigDecimal ukupanIznos) { this.ukupanIznos = ukupanIznos; }
}
