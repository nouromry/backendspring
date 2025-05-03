package tn.enicarthage.dto;

import lombok.Data;
import tn.enicarthage.models.JurySoutenance.Role;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
public class SoutenanceCreateDTO {
	private Integer id;
    private String salle;
    private String enseignants;  // String representation of jury members
    private String dateSoutenance;  // Date in string format
    private Boolean disponible;
    private String heureDebut; 
    
    
    private String projetTitre;
    private Integer projetId;
    private String projetDescription;
    private String projetTechnologies;
        
    private Integer binomeId;
    private String binomeEtudiant1;
    private String binomeEtudiant2;
    private List<JuryMemberDTO> juryMembers;
    
    @Data
    public static class JuryMemberDTO {
        private Integer enseignantId;
        private Role role;
      
    }

	public String getHeureDebut() {
		return heureDebut;
	}
}