package tn.enicarthage.models;

import jakarta.persistence.*;
import java.util.Date;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 255)
    private String titre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDepot;
    
    @Column(nullable = false, length = 255)
    private String cheminFichier;
    
    @ManyToOne
    @JoinColumn(name = "binome_id", nullable = false)
    private Binome binome;
    
    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;
    
    public enum Type {
        rapport_final, normal
    }
}