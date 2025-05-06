package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.models.Document;
import tn.enicarthage.models.Document.Type;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findByType(Document.Type type);
    List<Document> findByBinomeId(Integer binomeId);
    List<Document> findByProjetId(Integer projetId);
List<Document> findByBinomeIdAndProjetId(Integer binomeId, Integer projetId);
    
    // Find documents by student id (via binome)
    @Query("SELECT d FROM Document d WHERE d.binome.etud1.id = :etudiantId OR d.binome.etud2.id = :etudiantId")
    List<Document> findByEtudiantId(@Param("etudiantId") Integer etudiantId);
}