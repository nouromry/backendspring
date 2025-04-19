package tn.enicarthage.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "commentaire")
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenu;
    
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCommentaire;
    
    @ManyToOne
    @JoinColumn(name = "auteur_id", nullable = false)
    @JsonBackReference
    private User auteur;
    
    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    @JsonBackReference
    private Projet projet;
}