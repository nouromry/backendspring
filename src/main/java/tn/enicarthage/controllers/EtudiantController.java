// EtudiantController.java
package tn.enicarthage.controllers;

import tn.enicarthage.dto.ProjetCreationDto;
import tn.enicarthage.models.Document;
import tn.enicarthage.models.Etudiant;
import tn.enicarthage.models.Projet;
import tn.enicarthage.services.EtudiantService;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
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

    @GetMapping("/{etudiantId}")
    public List<Projet> getStudentProjects(@PathVariable("etudiantId") Integer etudiantId) {
        return etudiantService.getStudentProjects(etudiantId);
    }
    
    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<?> getEtudiantByMatricule(@PathVariable String matricule) {
        return etudiantService.getEtudiantByMatricule(matricule)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public List<Etudiant> getAllEtudiants() {
        return etudiantService.getAllEtudiants();
    }
    
   
   
}