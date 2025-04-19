package tn.enicarthage.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "projet")
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 100)
    private String titre;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(length = 255)
    private String technologies;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Etat etat;
    
    @Temporal(TemporalType.DATE)
    private Date dateDepot;
    
    @Temporal(TemporalType.DATE)
    private Date dateAffectation;
    
    @ManyToOne
    @JoinColumn(name = "enseignant_id", nullable = false)
    @JsonBackReference
    private Enseignant enseignant;
    
    @OneToMany(mappedBy = "projet")
    @JsonBackReference
    private List<Commentaire> commentaires;
    
    @OneToMany(mappedBy = "projet")
    @JsonBackReference
    private List<Document> documents;
    
    @OneToMany(mappedBy = "projet")
    @JsonBackReference
    private List<ChoixProjet> choixProjets;
    
    public enum Etat {
        en_cours, terminé, en_attente
    }
}