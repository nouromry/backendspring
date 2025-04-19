package tn.enicarthage.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
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
    @JsonBackReference
    private List<Projet> projets;
    
    @OneToMany(mappedBy = "enseignant")
    @JsonBackReference
    private List<JurySoutenance> jurys;
}