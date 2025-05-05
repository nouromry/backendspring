package tn.enicarthage.dto;

import java.math.BigDecimal;

import lombok.Data;
import tn.enicarthage.models.Binome;

@Data
public class BinomeSoutenanceDTO {
    private Integer id;
    private EtudiantDTO etud1;
    private EtudiantDTO etud2;
    private BigDecimal moyenneBinome;

    public BinomeSoutenanceDTO(Binome binome) {
        this.id = binome.getId();
        this.etud1 = new EtudiantDTO(binome.getEtud1());
        this.etud2 = new EtudiantDTO(binome.getEtud2());
        this.moyenneBinome = binome.getMoyenneBinome();
    }
}