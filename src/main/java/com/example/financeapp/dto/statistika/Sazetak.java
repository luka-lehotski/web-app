package com.example.financeapp.dto.statistika;

import java.math.BigDecimal;

public class Sazetak {
    private BigDecimal prihodi;
    private BigDecimal rashodi;
    private BigDecimal balans;

    public Sazetak(BigDecimal prihodi, BigDecimal rashodi) {
        this.prihodi = prihodi != null ? prihodi : BigDecimal.ZERO;
        this.rashodi = rashodi != null ? rashodi : BigDecimal.ZERO;
        this.balans = this.prihodi.subtract(this.rashodi);
    }

    // Getters and Setters
    public BigDecimal getPrihodi() { return prihodi; }
    public void setPrihodi(BigDecimal prihodi) { this.prihodi = prihodi; }
    public BigDecimal getRashodi() { return rashodi; }
    public void setRashodi(BigDecimal rashodi) { this.rashodi = rashodi; }
    public BigDecimal getBalans() { return balans; }
    public void setBalans(BigDecimal balans) { this.balans = balans; }
}
