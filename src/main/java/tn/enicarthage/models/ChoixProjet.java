package tn.enicarthage.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

@Entity
@Table(name = "choixProjet")
public class ChoixProjet {
    @EmbeddedId
    private ChoixProjetId id;
    
    @ManyToOne
    @MapsId("idBinome")
    @JoinColumn(name = "idBinome")
    @JsonBackReference
    private Binome binome;
    
    @ManyToOne
    @MapsId("idProjet")
    @JoinColumn(name = "idProjet")
    @JsonManagedReference
    private Projet projet;
    
    @Column(nullable = false)
    private Integer priorite;

    // Getter for 'projet'
    public Projet getProjet() {
        return projet;
    }

    // Getter for 'priorite'
    public Integer getPriorite() {
        return priorite;
    }
    
    @Embeddable
    public static class ChoixProjetId implements java.io.Serializable {
        private Integer idBinome;
        private Integer idProjet;
        
        public ChoixProjetId() {}
        
        public ChoixProjetId(Integer idBinome, Integer idProjet) {
            this.idBinome = idBinome;
            this.idProjet = idProjet;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            
            ChoixProjetId that = (ChoixProjetId) o;
            
            if (!idBinome.equals(that.idBinome)) return false;
            return idProjet.equals(that.idProjet);
        }
        
        @Override
        public int hashCode() {
            int result = idBinome.hashCode();
            result = 31 * result + idProjet.hashCode();
            return result;
        }
    }
}
