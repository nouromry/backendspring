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
    @Column(name = "filiere")
    private String filiere;
    
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "enseignant_id", nullable = false)
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
    
    @OneToOne(mappedBy = "projetAffecte")
    @JsonBackReference
    private Binome binomeAffecte;

    
    public enum Etat {
        en_attente, valide, annulee;
        
        // Add a helper method to convert string values safely
    	public static Etat fromString(String value) {
    	    if (value == null) return null;

    	    try {
    	        return valueOf(value.toUpperCase());
    	    } catch (IllegalArgumentException e) {
    	        switch (value.toLowerCase()) {
    	            case "en_attente":
    	            case "en attente":
    	            case "en_cours":
    	                return en_attente;
    	            case "valide":
    	            case "validee":
    	            case "valider":
    	            case "termine":
    	            case "terminee":
    	                return valide;
    	            case "annulee":
    	            case "annulée":
    	                return annulee;
    	            default:
    	                throw new IllegalArgumentException("Valeur d'état inconnue : " + value);
    	        }
    	    }
    	}
    }
}