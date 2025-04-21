package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.models.Soutenance;

@Repository
public interface SoutenanceRepository extends JpaRepository<Soutenance, Integer> {
    
}