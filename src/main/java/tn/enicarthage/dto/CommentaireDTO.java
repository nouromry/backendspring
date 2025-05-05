package tn.enicarthage.dto;

import tn.enicarthage.models.User;
import java.util.Date;

public class CommentaireDTO {
    private Integer id;
    private String contenu;
    private Date dateCommentaire;
    private AuteurDTO auteur;
    
    public CommentaireDTO(Integer id, String contenu, Date dateCommentaire, User auteur) {
        this.id = id;
        this.contenu = contenu;
        this.dateCommentaire = dateCommentaire;
        this.auteur = new AuteurDTO(auteur);
    }
    
    // Getters
    public Integer getId() { return id; }
    public String getContenu() { return contenu; }
    public Date getDateCommentaire() { return dateCommentaire; }
    public AuteurDTO getAuteur() { return auteur; }
    
    public static class AuteurDTO {
        private Integer id;
        private String nom;
        private String prenom;
        private String role;
        
        public AuteurDTO(User user) {
            this.id = user.getId();
            this.nom = user.getNom();
            this.prenom = user.getPrenom();
            this.role = user.getRole().name();
        }
        
        // Getters
        public Integer getId() { return id; }
        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public String getRole() { return role; }
    }
}