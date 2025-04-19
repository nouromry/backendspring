package tn.enicarthage.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "jury_soutenance")
public class JurySoutenance {
    @EmbeddedId
    private JurySoutenanceId id;
    
    @ManyToOne
    @MapsId("enseignantId")
    @JoinColumn(name = "enseignant_id")
    @JsonBackReference
    private Enseignant enseignant;
    
    @ManyToOne
    @MapsId("soutenanceId")
    @JoinColumn(name = "soutenance_id")
    @JsonBackReference
    private Soutenance soutenance;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    public enum Role {
        président, examinateur, rapporteur
    }
    
    @Embeddable
    public static class JurySoutenanceId implements java.io.Serializable {
        private Integer enseignantId;
        private Integer soutenanceId;
        
        public JurySoutenanceId() {}
        
        public JurySoutenanceId(Integer enseignantId, Integer soutenanceId) {
            this.enseignantId = enseignantId;
            this.soutenanceId = soutenanceId;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            
            JurySoutenanceId that = (JurySoutenanceId) o;
            
            if (!enseignantId.equals(that.enseignantId)) return false;
            return soutenanceId.equals(that.soutenanceId);
        }
        
        @Override
        public int hashCode() {
            int result = enseignantId.hashCode();
            result = 31 * result + soutenanceId.hashCode();
            return result;
        }
    }
}