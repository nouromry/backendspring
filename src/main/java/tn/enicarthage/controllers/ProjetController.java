package tn.enicarthage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.models.Projet;
import tn.enicarthage.services.ProjetService;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@RequiredArgsConstructor
public class ProjetController {

    private final ProjetService projetService;

    // GET /api/projets
    @GetMapping
    public ResponseEntity<List<Projet>> getAllProjets() {
        return ResponseEntity.ok(projetService.getAllProjets());
    }

    // GET /api/projets/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Projet> getProjetById(@PathVariable Integer id) {
        return projetService.getProjetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/projets/enseignant/{enseignantId}
    @GetMapping("/enseignant/{enseignantId}")
    public ResponseEntity<List<Projet>> getProjetsByEnseignantId(@PathVariable Integer enseignantId) {
        return ResponseEntity.ok(projetService.getProjetsByEnseignantId(enseignantId));
    }
}
