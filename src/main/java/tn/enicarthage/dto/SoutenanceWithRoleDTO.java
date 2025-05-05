package tn.enicarthage.dto;


import lombok.Data;
import tn.enicarthage.models.Soutenance;
import tn.enicarthage.models.JurySoutenance.Role;

import java.util.Date;
import java.sql.Time;

@Data
public class SoutenanceWithRoleDTO {
    private Integer id;
    private Date date;
    private Time heureD;
    private String salle;
    private BinomeSoutenanceDTO binome;
    private Role role;

    public SoutenanceWithRoleDTO(Soutenance soutenance, Role role) {
        this.id = soutenance.getId();
        this.date = soutenance.getDate();
        this.heureD = soutenance.getHeureD();
        this.salle = soutenance.getSalle();
        this.binome = new BinomeSoutenanceDTO(soutenance.getBinome());
        this.role = role;
    }
}