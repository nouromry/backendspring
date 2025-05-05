package tn.enicarthage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.enicarthage.dto.ProjetCreationDto;
import tn.enicarthage.dto.ProjetWithBinomeDTO;
import tn.enicarthage.dto.SoutenanceWithRoleDTO;
import tn.enicarthage.models.Enseignant;
import tn.enicarthage.models.Projet;
import tn.enicarthage.services.EnseignantService;

import java.util.List;

@RestController
@RequestMapping("/api/enseignants")

public class EnseignantController {

    private final EnseignantService enseignantService;

    @Autowired
    public EnseignantController(EnseignantService enseignantService) {
        this.enseignantService = enseignantService;
    }

    @GetMapping
    public ResponseEntity<List<Enseignant>> getAllEnseignants() {
        List<Enseignant> enseignants = enseignantService.getAllEnseignants();
        return new ResponseEntity<>(enseignants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enseignant> getEnseignantById(@PathVariable Integer id) {
        Enseignant enseignant = enseignantService.getEnseignantById(id);
        return new ResponseEntity<>(enseignant, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Enseignant> getEnseignantByEmail(@PathVariable String email) {
        Enseignant enseignant = enseignantService.getEnseignantByEmail(email);
        return new ResponseEntity<>(enseignant, HttpStatus.OK);
    }

    @GetMapping("/specialite/{specialite}")
    public ResponseEntity<List<Enseignant>> getEnseignantsBySpecialite(@PathVariable String specialite) {
        List<Enseignant> enseignants = enseignantService.getEnseignantsBySpecialite(specialite);
        return new ResponseEntity<>(enseignants, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Enseignant> createEnseignant(@RequestBody Enseignant enseignant) {
        Enseignant newEnseignant = enseignantService.createEnseignant(enseignant);
        return new ResponseEntity<>(newEnseignant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enseignant> updateEnseignant(@PathVariable Integer id, @RequestBody Enseignant enseignant) {
        Enseignant updatedEnseignant = enseignantService.updateEnseignant(id, enseignant);
        return new ResponseEntity<>(updatedEnseignant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnseignant(@PathVariable Integer id) {
        enseignantService.deleteEnseignant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Enseignant>> searchEnseignants(@RequestParam String nom) {
        List<Enseignant> enseignants = enseignantService.searchEnseignantsByName(nom);
        return new ResponseEntity<>(enseignants, HttpStatus.OK);
    }
    @PostMapping("/{enseignantId}/projets")
    public Projet createProject(@PathVariable Integer enseignantId, 
                              @RequestBody ProjetCreationDto projetDto) {
        return enseignantService.createProject(projetDto, enseignantId);
    }

    @GetMapping("/{enseignantId}/projets")
    public List<Projet> getEnseignantProjects(@PathVariable Integer enseignantId) {
        return enseignantService.getProjectsByEnseignant(enseignantId);
    }
    
    

    @GetMapping("/{enseignantId}/projets-valides")
    public ResponseEntity<List<ProjetWithBinomeDTO>> getValidProjectsWithBinomeDetails(
            @PathVariable Integer enseignantId) {
        
        List<ProjetWithBinomeDTO> projets = enseignantService.getValidProjectsWithBinomeDetails(enseignantId);
        
        if (projets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(projets);
    }
    
    @GetMapping("/{enseignantId}/soutenances")
    public ResponseEntity<List<SoutenanceWithRoleDTO>> getSoutenancesByEnseignant(
            @PathVariable Integer enseignantId) {
        
        List<SoutenanceWithRoleDTO> soutenances = enseignantService
                .getSoutenancesByEnseignant(enseignantId);
        
        if (soutenances.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(soutenances);
    }
    
}
