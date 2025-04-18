package tn.enicarthage.models;


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
    private Binome binomeAsEtud1;
    
    @OneToOne(mappedBy = "etud2")
    private Binome binomeAsEtud2;
    
    @Transient
    public Binome getBinome() {
        return binomeAsEtud1 != null ? binomeAsEtud1 : binomeAsEtud2;
    }
}