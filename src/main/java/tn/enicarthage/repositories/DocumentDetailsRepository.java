package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.models.Document;

import java.util.Optional;

@Repository
public interface DocumentDetailsRepository extends JpaRepository<Document, Integer> {
    
    @Query("SELECT d FROM Document d " +
           "JOIN FETCH d.binome b " +
           "JOIN FETCH b.etud1 e1 " +
           "JOIN FETCH b.etud2 e2 " +
           "JOIN FETCH d.projet p " +
           "JOIN FETCH p.enseignant ens " +
           "WHERE d.id = :documentId")
    Optional<Document> findDocumentWithDetails(@Param("documentId") Integer documentId);
}