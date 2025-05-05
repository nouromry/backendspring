package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.models.Enseignant;

import java.util.List;
import java.util.Optional;


import java.util.List;

public interface EnseignantRepository extends JpaRepository<Enseignant, Integer> {
    Enseignant findByEmail(String email);

    List<Enseignant> findBySpecialite(String specialite);
    List<Enseignant> findByNomContainingIgnoreCase(String nom);
   
    
    
}
