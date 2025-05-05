package tn.enicarthage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.enicarthage.models.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDetailsDTO {
    private Integer id;
    private String titre;
    private Document.Type type;
    private Date dateDepot;
    private String cheminFichier;
    
    // Binome details
    private Integer binomeId;
    private String etudiant1Nom;
    private String etudiant1Prenom;
    private String etudiant1Filiere;
    private String etudiant2Nom;
    private String etudiant2Prenom;
    private String etudiant2Filiere;
    
    // Project details
    private Integer projetId;
    private String projetTitre;
    
    // Enseignant details
    private Integer enseignantId;
    private String enseignantNom;
    private String enseignantPrenom;
}