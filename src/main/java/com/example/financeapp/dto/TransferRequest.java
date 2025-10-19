package com.example.financeapp.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class TransferRequest {
    public Long fromNovcanikId;
    public Long toNovcanikId;
    public BigDecimal iznos; // Iznos u valuti izvornog novčanika

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public Date datum;

    // Getters and Setters
    public Long getFromNovcanikId() { return fromNovcanikId; }
    public void setFromNovcanikId(Long fromNovcanikId) { this.fromNovcanikId = fromNovcanikId; }
    public Long getToNovcanikId() { return toNovcanikId; }
    public void setToNovcanikId(Long toNovcanikId) { this.toNovcanikId = toNovcanikId; }
    public BigDecimal getIznos() { return iznos; }
    public void setIznos(BigDecimal iznos) { this.iznos = iznos; }
    public Date getDatum() { return datum; }
    public void setDatum(Date datum) { this.datum = datum; }
}
