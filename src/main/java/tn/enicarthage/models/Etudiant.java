package tn.enicarthage.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Data
@Entity
@Table(name = "etudiant")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant extends User {
    
    @Column(precision = 5, scale = 2)
    private BigDecimal moyenneGeneral;
    
    @Column(nullable = false, unique = true, length = 20)
    private String matricule;
    
    @Column(nullable = false, length = 50)
    private String filiere;
    
    @OneToOne(mappedBy = "etud1")
    @JsonBackReference
    private Binome binomeAsEtud1;
    
    @OneToOne(mappedBy = "etud2")
    @JsonBackReference
    private Binome binomeAsEtud2;
    
    @Transient
    @JsonIgnore
    public Binome getBinome() {
        return binomeAsEtud1 != null ? binomeAsEtud1 : binomeAsEtud2;
    }
}