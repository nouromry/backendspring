package tn.enicarthage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetDTO {
    private Integer id;
    private String titre;
    private String description;
    private String technologies;
    private String filiere;
}