package tn.enicarthage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "chefDep")
@PrimaryKeyJoinColumn(name = "id")
public class ChefDep extends User {
    
    @Column(nullable = false, length = 100)
    private String departement;
    
    public String getDepartement() {
        return departement;
    }
    
    public void setDepartement(String departement) {
        this.departement = departement;
    }
}