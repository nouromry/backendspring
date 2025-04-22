package tn.enicarthage.repositories;

import tn.enicarthage.models.Binome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BinomeSoutenanceRepository extends JpaRepository<Binome, Integer> {
    
    // Find binomes with a specific student
    @Query("SELECT b FROM Binome b WHERE b.etud1.id = :etudiantId OR b.etud2.id = :etudiantId")
    List<Binome> findByEtudiant(@Param("etudiantId") Integer etudiantId);
    
    // Find binomes without a soutenance assigned
    @Query("SELECT b FROM Binome b WHERE b.soutenance IS NULL")
    List<Binome> findBinomesWithoutSoutenance();
    
    // Find binomes by average grade range
    @Query("SELECT b FROM Binome b WHERE b.moyenneBinome BETWEEN :minGrade AND :maxGrade")
    List<Binome> findByAverageGradeRange(@Param("minGrade") BigDecimal minGrade, @Param("maxGrade") BigDecimal maxGrade);
    
    // Find binomes with specific project choice
    @Query("SELECT b FROM Binome b JOIN b.choixProjets c WHERE c.projet.id = :projetId")
    List<Binome> findByProjetChoice(@Param("projetId") Integer projetId);
}