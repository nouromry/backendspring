package tn.enicarthage.repositories;

import tn.enicarthage.models.Commentaire;
import tn.enicarthage.models.Enseignant;
import tn.enicarthage.models.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CommentaireRepository extends JpaRepository<Commentaire, Integer> {
    List<Commentaire> findByProjetOrderByDateCommentaireDesc(Projet projet);
    
    @Query("SELECT c FROM Commentaire c JOIN c.projet p WHERE p.enseignant = :enseignant ORDER BY c.dateCommentaire DESC")
    List<Commentaire> findCommentsForEnseignantProjects(@Param("enseignant") Enseignant enseignant);
    @Query("SELECT c FROM Commentaire c JOIN FETCH c.auteur WHERE c.projet.id = :projetId ORDER BY c.dateCommentaire DESC")
    List<Commentaire> findByProjetIdWithAuthor(@Param("projetId") Integer projetId);
    
}