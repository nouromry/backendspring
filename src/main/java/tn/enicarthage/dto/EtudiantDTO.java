package tn.enicarthage.dto;

import java.math.BigDecimal;

import lombok.Data;
import tn.enicarthage.models.Etudiant;

@Data
public class EtudiantDTO {
    private Integer id;
    private String nom;
    private String prenom;
    private String matricule;
    private String filiere;
    private String groupe;
    private BigDecimal moyenneGeneral;

    public EtudiantDTO(Etudiant etudiant) {
        this.id = etudiant.getId();
        this.nom = etudiant.getNom();
        this.prenom = etudiant.getPrenom();
        this.matricule = etudiant.getMatricule();
        this.filiere = etudiant.getFiliere();
        this.groupe = etudiant.getGroupe();
        this.moyenneGeneral = etudiant.getMoyenneGeneral();
    }
}