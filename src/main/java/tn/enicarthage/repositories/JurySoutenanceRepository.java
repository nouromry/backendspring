package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.enicarthage.models.Enseignant;
import tn.enicarthage.models.JurySoutenance;

import java.util.List;

@Repository
public interface JurySoutenanceRepository extends JpaRepository<JurySoutenance, JurySoutenance.JurySoutenanceId> {
    List<JurySoutenance> findByEnseignant(Enseignant enseignant);
}