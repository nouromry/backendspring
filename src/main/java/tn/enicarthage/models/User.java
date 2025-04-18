package tn.enicarthage.models;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Data
@Entity
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 50)
    private String nom;
    
    @Column(nullable = false, length = 50)
    private String prenom;
    
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @OneToMany(mappedBy = "auteur")
    private List<Commentaire> commentaires;
  
    public enum Role {
        etudiant, enseignant, chefDep
    }
}
