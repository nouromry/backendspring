package tn.enicarthage.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.dto.BinomeCreationDTO;
import tn.enicarthage.dto.ProjetChoixDTO;
import tn.enicarthage.services.ChoixProjetService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/choix-projets")

public class ChoixProjetController {
    
    @Autowired
    private ChoixProjetService choixProjetService;
    
    
    @PostMapping("/creer-binome")
    public ResponseEntity<?> creerBinome(@RequestBody BinomeCreationDTO binomeDTO) {
        try {
            return ResponseEntity.ok(choixProjetService.creerBinome(binomeDTO.getMatriculeEtud1(), 
                                                                   binomeDTO.getMatriculeEtud2()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }
    
  
    @PostMapping("/binome/{idBinome}/choix")
    public ResponseEntity<?> enregistrerChoixProjets(
            @PathVariable Integer idBinome,
            @RequestBody List<ProjetChoixDTO> choix) {
        try {
            choixProjetService.enregistrerChoixProjets(idBinome, choix);
            return ResponseEntity.ok(Map.of("message", "Les choix de projets ont été enregistrés avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }
    
    
    @GetMapping("/projets-disponibles")
    public ResponseEntity<?> getProjetsDisponibles() {
        return ResponseEntity.ok(choixProjetService.getProjetsDisponibles());
    }

    @GetMapping("/binome/{idBinome}/choix")
    public ResponseEntity<?> getChoixProjets(@PathVariable Integer idBinome) {
        return ResponseEntity.ok(choixProjetService.getChoixProjetsByBinome(idBinome));
    }
}