package tn.enicarthage.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.enicarthage.models.Enseignant;
import tn.enicarthage.models.Projet;

import java.util.List;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Integer> {
    List<Projet> findByEnseignantId(Integer enseignantId);
    List<Projet> findByEtat(String etat);
    List<Projet> findByTitreContainingIgnoreCaseOrEnseignant_NomContainingIgnoreCase(String titre, String enseignantNom);
    List<Projet> findByFiliere(String filiere);
}