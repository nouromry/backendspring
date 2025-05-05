package tn.enicarthage.dto;

import lombok.Data;

@Data
public class ProjetCreationDto {
    private String titre;
    private String description;
    private String technologies;
    private String filiere;
}