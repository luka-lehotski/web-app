package com.example.financeapp.repository;

import com.example.financeapp.dto.statistika.PoKategorijama;
import com.example.financeapp.model.Kategorija;
import com.example.financeapp.model.Transakcija;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TransakcijaRepository extends JpaRepository<Transakcija, Long> {

    List<Transakcija> findAllByNovcanikId(Long novcanikId);

    List<Transakcija> findAllByNovcanikIdAndKorisnikId(Long novcanikId, Long korisnikId, Pageable pageable);

    List<Transakcija> findAllByPonavljajuca(boolean ponavljajuca);

    // Nova metoda za listanje šablona po korisniku
    List<Transakcija> findAllByKorisnikIdAndPonavljajuca(Long korisnikId, boolean ponavljajuca);

    @Query("SELECT t FROM Transakcija t WHERE t.korisnik.id = :korisnikId " +
           "AND (:novcanikId IS NULL OR t.novcanik.id = :novcanikId) " +
           "AND (:kategorijaId IS NULL OR t.kategorija.id = :kategorijaId) " +
           "AND (:startDate IS NULL OR t.datumTransakcije >= :startDate) " +
           "AND (:endDate IS NULL OR t.datumTransakcije <= :endDate) " +
           "AND (:minIznos IS NULL OR t.iznos >= :minIznos) " +
           "AND (:maxIznos IS NULL OR t.iznos <= :maxIznos)")
    List<Transakcija> findByKorisnikIdAndFilters(
            @Param("korisnikId") Long korisnikId,
            @Param("novcanikId") Long novcanikId,
            @Param("kategorijaId") Long kategorijaId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("minIznos") BigDecimal minIznos,
            @Param("maxIznos") BigDecimal maxIznos
    );

    // ... (ostale statističke metode ostaju iste)
    @Query("SELECT SUM(t.iznos) FROM Transakcija t WHERE t.korisnik.id = :korisnikId " +
           "AND t.tip = :tip " +
           "AND (:startDate IS NULL OR t.datumTransakcije >= :startDate) " +
           "AND (:endDate IS NULL OR t.datumTransakcije <= :endDate) " +
           "AND (:kategorijaId IS NULL OR t.kategorija.id = :kategorijaId) " +
           "AND (:minIznos IS NULL OR t.iznos >= :minIznos) " +
           "AND (:maxIznos IS NULL OR t.iznos <= :maxIznos)")
    BigDecimal sumIznosByTipAndKorisnikIdAndFilters(
            @Param("korisnikId") Long korisnikId,
            @Param("tip") Kategorija.Tip tip,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("kategorijaId") Long kategorijaId,
            @Param("minIznos") BigDecimal minIznos,
            @Param("maxIznos") BigDecimal maxIznos
    );

    @Query("SELECT new com.example.financeapp.dto.statistika.PoKategorijama(t.kategorija.naziv, SUM(t.iznos)) " +
           "FROM Transakcija t WHERE t.korisnik.id = :korisnikId " +
           "AND (:tip IS NULL OR t.tip = :tip) " +
           "AND (:startDate IS NULL OR t.datumTransakcije >= :startDate) " +
           "AND (:endDate IS NULL OR t.datumTransakcije <= :endDate) " +
           "AND (:kategorijaId IS NULL OR t.kategorija.id = :kategorijaId) " +
           "AND (:minIznos IS NULL OR t.iznos >= :minIznos) " +
           "AND (:maxIznos IS NULL OR t.iznos <= :maxIznos) " +
           "GROUP BY t.kategorija.naziv")
    List<PoKategorijama> sumIznosGroupByKategorija(
            @Param("korisnikId") Long korisnikId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("tip") Kategorija.Tip tip,
            @Param("kategorijaId") Long kategorijaId,
            @Param("minIznos") BigDecimal minIznos,
            @Param("maxIznos") BigDecimal maxIznos
    );

    @Query("SELECT t FROM Transakcija t WHERE t.korisnik.id = :korisnikId AND t.tip = 'TROSAK' " +
           "AND (:startDate IS NULL OR t.datumTransakcije >= :startDate) " +
           "AND (:endDate IS NULL OR t.datumTransakcije <= :endDate) " +
           "AND (:kategorijaId IS NULL OR t.kategorija.id = :kategorijaId) " +
           "AND (:minIznos IS NULL OR t.iznos >= :minIznos) " +
           "AND (:maxIznos IS NULL OR t.iznos <= :maxIznos) " +
           "ORDER BY t.iznos DESC")
    List<Transakcija> findTopTroskovi(
            @Param("korisnikId") Long korisnikId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("kategorijaId") Long kategorijaId,
            @Param("minIznos") BigDecimal minIznos,
            @Param("maxIznos") BigDecimal maxIznos,
            Pageable pageable
    );
}
