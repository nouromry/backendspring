package tn.enicarthage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class BinomeDTO {
    private String prenomEtud1;
    private String nomEtud1;
    private String prenomEtud2;
    private String nomEtud2;
    private String filiere;
    private String groupe;
    private BigDecimal moyenneEtud1;
    private BigDecimal moyenneBinome;
    private List<String> projetsChoisis;
}
