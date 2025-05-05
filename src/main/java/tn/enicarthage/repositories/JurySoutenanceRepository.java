package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.enicarthage.models.Enseignant;
import tn.enicarthage.models.JurySoutenance;

import java.util.List;

@Repository
public interface JurySoutenanceRepository extends JpaRepository<JurySoutenance, JurySoutenance.JurySoutenanceId> {
    List<JurySoutenance> findByEnseignant(Enseignant enseignant);
    @Query("SELECT js FROM JurySoutenance js " +
            "JOIN FETCH js.soutenance s " +
            "JOIN FETCH s.binome b " +
            "JOIN FETCH b.etud1 e1 " +
            "JOIN FETCH b.etud2 e2 " +
            "WHERE js.enseignant.id = :enseignantId")
     List<JurySoutenance> findWithSoutenanceDetailsByEnseignantId(@Param("enseignantId") Integer enseignantId);
}