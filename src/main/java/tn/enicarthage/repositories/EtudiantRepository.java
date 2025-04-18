package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
