package tn.enicarthage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import tn.enicarthage.models.Projet;
import tn.enicarthage.services.ProjetService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/projets")
@RequiredArgsConstructor
public class ProjetController {

    private final ProjetService projetService;


    // GET /api/projets/enseignant/{enseignantId}
    @GetMapping("/enseignant/{enseignantId}")
    public ResponseEntity<List<Projet>> getProjetsByEnseignantId(@PathVariable Integer enseignantId) {
        return ResponseEntity.ok(projetService.getProjetsByEnseignantId(enseignantId));
    }
    @GetMapping
    public ResponseEntity<List<Projet>> getAllProjets() {
        List<Projet> projets = projetService.getAllProjets();
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projet> getProjetById(@PathVariable("id") Integer id) {
        Optional<Projet> projet = projetService.getProjetById(id);
        return projet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Projet>> filterProjets(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "searchTerm", required = false) String searchTerm,
            @RequestParam(name = "filiere", required = false) String filiere) {

        List<Projet> projets;
        if (status != null) {
            projets = projetService.getProjetsByEtat(status);
        } else if (searchTerm != null) {
            projets = projetService.searchProjets(searchTerm);
        } else if (filiere != null) {
            projets = projetService.filterByFiliere(filiere);
        } else {
            projets = projetService.getAllProjets();
        }
        return ResponseEntity.ok(projets);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<Projet> updateProjetStatus(
        @PathVariable("id") Integer id, 
        @RequestBody Map<String, String> statusMap) {
        
        try {
            String statusValue = statusMap.get("status");
            Projet.Etat etat = Projet.Etat.fromString(statusValue);
            
            Projet projet = projetService.updateProjetStatus(id, etat);
            return projet != null ? ResponseEntity.ok(projet) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping
    public ResponseEntity<Projet> createProjet(@RequestBody Projet projet) {
        Projet savedProjet = projetService.saveProjet(projet);
        return new ResponseEntity<>(savedProjet, HttpStatus.CREATED);
    }
}













