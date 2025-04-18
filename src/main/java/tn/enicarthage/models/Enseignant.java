package tn.enicarthage.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "enseignant")
@PrimaryKeyJoinColumn(name = "id")
public class Enseignant extends User {
    
    @Column(length = 100)
    private String specialite;
    
    @OneToMany(mappedBy = "enseignant")
    private List<Projet> projets;
    
    @OneToMany(mappedBy = "enseignant")
    private List<JurySoutenance> jurys;
}