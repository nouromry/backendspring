




package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.enicarthage.models.Binome;
import tn.enicarthage.models.Etudiant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {
    Optional<Etudiant> findByMatricule(String matricule);
    List<Etudiant> findByFiliere(String filiere);
    List<Etudiant> findByMoyenneGeneralGreaterThanEqual(BigDecimal moyenne);
    boolean existsByMatricule(String matricule);
    Etudiant findByEmail(String email);
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Binome b WHERE b.etud1.id = :etudiantId OR b.etud2.id = :etudiantId")
    boolean isPartOfBinome(@Param("etudiantId") Integer etudiantId);
    
    // Get binome for a student
    @Query("SELECT b FROM Binome b WHERE b.etud1.id = :etudiantId OR b.etud2.id = :etudiantId")
    Optional<Binome> findBinomeByEtudiantId(@Param("etudiantId") Integer etudiantId);
}