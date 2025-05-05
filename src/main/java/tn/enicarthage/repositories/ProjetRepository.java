package tn.enicarthage.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.enicarthage.models.Binome;
import tn.enicarthage.models.Enseignant;
import tn.enicarthage.models.Etudiant;
import tn.enicarthage.models.Projet;

import java.util.List;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Integer> {
    List<Projet> findByEnseignantId(Integer enseignantId);
    List<Projet> findByEtat(String etat);
    List<Projet> findByTitreContainingIgnoreCaseOrEnseignant_NomContainingIgnoreCase(String titre, String enseignantNom);
    List<Projet> findByFiliere(String filiere);
    List<Projet> findByEnseignant(Enseignant enseignant);
    @Query("SELECT p FROM Projet p LEFT JOIN FETCH p.binomeAffecte b LEFT JOIN FETCH b.etud1 LEFT JOIN FETCH b.etud2 WHERE p.enseignant = :enseignant")
    List<Projet> findByEnseignantWithBinome(@Param("enseignant") Enseignant enseignant);
    @Query("SELECT p FROM Projet p " +
    	       "LEFT JOIN FETCH p.binomeAffecte b " +
    	       "LEFT JOIN FETCH b.etud1 e1 " +
    	       "LEFT JOIN FETCH b.etud2 e2 " +
    	       "LEFT JOIN FETCH p.enseignant e " +
    	       "WHERE p.enseignant.id = :enseignantId")
    	List<Projet> findByEnseignantIdWithBinome(@Param("enseignantId") Integer enseignantId);
    @Query("SELECT p FROM Projet p JOIN FETCH p.binomeAffecte WHERE p.enseignant.id = :enseignantId AND p.etat = 'VALIDE'")
    List<Projet> findByEnseignantIdAndEtatValideWithBinome(@Param("enseignantId") Integer enseignantId);
    @Query("SELECT DISTINCT p FROM Projet p " +
            "LEFT JOIN FETCH p.binomeAffecte b " +
            "LEFT JOIN FETCH b.etud1 e1 " +
            "LEFT JOIN FETCH b.etud2 e2 " +
            "WHERE p.enseignant.id = :enseignantId " +
            "AND p.etat = 'VALIDE'")  // Changed this line
     List<Projet> findValidProjectsWithBinomeDetailsByEnseignantId(@Param("enseignantId") Integer enseignantId);
    List<Projet> findByBinomeAffecteId(Integer binomeId);
    
    // Add these new methods
    List<Projet> findByBinomeAffecte(Binome binome);
    
    List<Projet> findByEtudiantCreateur(Etudiant etudiant);
}
