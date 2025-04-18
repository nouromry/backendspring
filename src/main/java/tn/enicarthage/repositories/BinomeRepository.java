package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.enicarthage.models.Binome;
import tn.enicarthage.models.Etudiant;

import java.util.Optional;

@Repository
public interface BinomeRepository extends JpaRepository<Binome, Integer> {
    Optional<Binome> findByEtud1OrEtud2(Etudiant etud1, Etudiant etud2);
}
