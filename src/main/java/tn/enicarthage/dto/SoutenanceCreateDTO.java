package tn.enicarthage.dto;

import lombok.Data;
import tn.enicarthage.models.JurySoutenance.Role;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
public class SoutenanceCreateDTO {
    private Date date;
    private Integer duree;
    private Time heureD;
    private Time heureF;
    private String salle;
    private Integer binomeId;
    private List<JuryMemberDTO> juryMembers;
    
    @Data
    public static class JuryMemberDTO {
        private Integer enseignantId;
        private Role role;
    }
}