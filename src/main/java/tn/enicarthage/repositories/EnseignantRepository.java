package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.enicarthage.models.Enseignant;

import java.util.List;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Integer> {
    List<Enseignant> findBySpecialite(String specialite);
}
