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
    

    
    @Query("SELECT c FROM Commentaire c JOIN c.projet p " +
            "JOIN p.binomeAffecte b " +
            "WHERE (b.etud1.id = :etudiantId OR b.etud2.id = :etudiantId) " +
            "ORDER BY c.dateCommentaire DESC")
     List<Commentaire> findCommentsForEtudiantProjects(@Param("etudiantId") Integer etudiantId);
     
     // Get comments created by a specific student
     @Query("SELECT c FROM Commentaire c " +
            "JOIN c.auteur a " +
            "WHERE TYPE(a) = Etudiant AND a.id = :etudiantId " +
            "ORDER BY c.dateCommentaire DESC")
     List<Commentaire> findCommentsByEtudiant(@Param("etudiantId") Integer etudiantId);
     
     // Get the latest comment for each project that a student is involved with
     @Query("SELECT c FROM Commentaire c " +
            "WHERE c.dateCommentaire = (" +
            "    SELECT MAX(c2.dateCommentaire) FROM Commentaire c2 " +
            "    WHERE c2.projet = c.projet" +
            ") " +
            "AND c.projet.id IN (" +
            "    SELECT b.projetAffecte.id FROM Binome b " +
            "    WHERE b.etud1.id = :etudiantId OR b.etud2.id = :etudiantId" +
            ")")
     List<Commentaire> findLatestCommentsForEtudiantProjects(@Param("etudiantId") Integer etudiantId);
     
     // Get unread comments for a student (comments after student's last comment)
     @Query("SELECT c FROM Commentaire c " +
            "WHERE c.projet.id IN (" +
            "    SELECT b.projetAffecte.id FROM Binome b " +
            "    WHERE b.etud1.id = :etudiantId OR b.etud2.id = :etudiantId" +
            ") " +
            "AND c.dateCommentaire > (" +
            "    SELECT COALESCE(MAX(c2.dateCommentaire), '1900-01-01') FROM Commentaire c2 " +
            "    WHERE c2.projet = c.projet " +
            "    AND TYPE(c2.auteur) = Etudiant " +
            "    AND c2.auteur.id = :etudiantId" +
            ") " +
            "AND TYPE(c.auteur) = Enseignant " +
            "ORDER BY c.dateCommentaire DESC")
     List<Commentaire> findUnreadCommentsForEtudiant(@Param("etudiantId") Integer etudiantId);
    
}