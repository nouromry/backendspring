// EtudiantController.java
package tn.enicarthage.controllers;

import tn.enicarthage.models.Etudiant;
import tn.enicarthage.services.EtudiantService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/etudiants")

public class EtudiantController {
    private final EtudiantService etudiantService;

    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @PostMapping
    public Etudiant createEtudiant(@RequestBody Etudiant etudiant) {
        return etudiantService.createEtudiant(etudiant);
    }
}
