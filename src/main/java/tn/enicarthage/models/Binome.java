package tn.enicarthage.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "binome")
public class Binome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "etud1", nullable = false, unique = true)
    @JsonManagedReference
    private Etudiant etud1;
    
    @OneToOne
    @JoinColumn(name = "etud2", nullable = false, unique = true)
    @JsonManagedReference
    private Etudiant etud2;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal moyenneBinome;
    
    @OneToMany(mappedBy = "binome")
    @JsonBackReference
    private List<Document> documents;
    
    @OneToOne(mappedBy = "binome")
    @JsonBackReference
    private Soutenance soutenance;
    
    @OneToMany(mappedBy = "binome")
    @JsonBackReference
    private List<ChoixProjet> choixProjets;
}