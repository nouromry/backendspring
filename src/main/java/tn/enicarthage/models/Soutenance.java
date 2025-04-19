package tn.enicarthage.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.util.Date;
import java.sql.Time;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "soutenance")
public class Soutenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;
    
    @Column
    private Integer duree;
    
    @Column(nullable = false)
    private Time heureD;
    
    @Column(nullable = false)
    private Time heureF;
    
    @Column(nullable = false)
    private String salle;
    
    @OneToOne
    @JoinColumn(name = "binome_id", nullable = false, unique = true)
    @JsonManagedReference
    private Binome binome;
    
    @OneToMany(mappedBy = "soutenance")
    @JsonBackReference
    private List<JurySoutenance> jury;
}