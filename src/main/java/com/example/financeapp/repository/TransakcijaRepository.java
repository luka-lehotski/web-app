package com.example.financeapp.repository;

import com.example.financeapp.model.Transakcija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TransakcijaRepository extends JpaRepository<Transakcija, Long> {

    List<Transakcija> findAllByNovcanikId(Long novcanikId);

    List<Transakcija> findAllByPonavljajuca(boolean ponavljajuca);

    @Query("SELECT t FROM Transakcija t WHERE t.korisnik.id = :korisnikId " +
           "AND (:kategorijaId IS NULL OR t.kategorija.id = :kategorijaId) " +
           "AND (:startDate IS NULL OR t.datumTransakcije >= :startDate) " +
           "AND (:endDate IS NULL OR t.datumTransakcije <= :endDate) " +
           "AND (:minIznos IS NULL OR t.iznos >= :minIznos) " +
           "AND (:maxIznos IS NULL OR t.iznos <= :maxIznos)")
    List<Transakcija> findByKorisnikIdAndFilters(
            @Param("korisnikId") Long korisnikId,
            @Param("kategorijaId") Long kategorijaId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("minIznos") Double minIznos,
            @Param("maxIznos") Double maxIznos
    );
}
