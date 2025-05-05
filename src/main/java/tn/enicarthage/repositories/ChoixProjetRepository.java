package tn.enicarthage.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.models.Binome;
import tn.enicarthage.models.ChoixProjet;

import java.util.List;

@Repository
public interface ChoixProjetRepository extends JpaRepository<ChoixProjet, ChoixProjet.ChoixProjetId> {
    List<ChoixProjet> findByBinomeOrderByPrioriteAsc(Binome binome);
    void deleteByBinome(Binome binome);
}