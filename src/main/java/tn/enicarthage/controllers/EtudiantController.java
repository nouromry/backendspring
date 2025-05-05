// EtudiantController.java
package tn.enicarthage.controllers;

import tn.enicarthage.dto.ProjetCreationDto;
import tn.enicarthage.models.Etudiant;
import tn.enicarthage.models.Projet;
import tn.enicarthage.services.EtudiantService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/etudiants")
public class EtudiantController {
    private final EtudiantService etudiantService;

    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @PostMapping("/{etudiantId}/projets")
    public Projet createProject(@PathVariable("etudiantId") Integer etudiantId, 
                              @RequestBody ProjetCreationDto projetDto) {
        return etudiantService.createProject(projetDto,etudiantId);
    }

    @GetMapping("/{etudiantId}/projets")
    public List<Projet> getStudentProjects(@PathVariable("etudiantId") Integer etudiantId) {
        return etudiantService.getStudentProjects(etudiantId);
    }
}