package tn.enicarthage.controllers;

import tn.enicarthage.models.Enseignant;
import tn.enicarthage.services.EnseignantService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enseignants")
@CrossOrigin("*") 
public class EnseignantController {
    private final EnseignantService enseignantService;

    public EnseignantController(EnseignantService enseignantService) {
        this.enseignantService = enseignantService;
    }

    @PostMapping
    public Enseignant createEnseignant(@RequestBody Enseignant enseignant) {
        return enseignantService.createEnseignant(enseignant);
    }
}
