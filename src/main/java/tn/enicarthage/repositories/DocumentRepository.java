package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.models.Document;
import tn.enicarthage.models.Document.Type;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findByType(Document.Type type);
    List<Document> findByBinomeId(Integer binomeId);
    List<Document> findByProjetId(Integer projetId);
}