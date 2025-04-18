package tn.enicarthage.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.enicarthage.models.Enseignant;
import tn.enicarthage.models.Projet;

import java.util.List;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Integer> {
    List<Projet> findByEnseignant(Enseignant enseignant);
}
