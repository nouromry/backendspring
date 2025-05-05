package tn.enicarthage.controllers;

import tn.enicarthage.dto.CommentaireDTO;
import tn.enicarthage.dto.ProjetWithBinomeDTO;
import tn.enicarthage.models.Commentaire;
import tn.enicarthage.models.Projet;
import tn.enicarthage.services.EspaceEchangeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enseignant/echange")
public class EspaceEchangeController {
    private final EspaceEchangeService espaceEchangeService;

    public EspaceEchangeController(EspaceEchangeService espaceEchangeService) {
        this.espaceEchangeService = espaceEchangeService;
    }

    // Get all comments for all projects supervised by a teacher
    @GetMapping("/{enseignantId}/comments")
    public List<Commentaire> getAllCommentsForEnseignant(@PathVariable Integer enseignantId) {
        return espaceEchangeService.getCommentsForEnseignant(enseignantId);
    }

    // Get comments for a specific project
    @GetMapping("/projet/{projetId}/comments")
    public List<CommentaireDTO> getCommentsForProject(@PathVariable Integer projetId) {
        return espaceEchangeService.getCommentsForProject(projetId);
    }

    // Add a new comment to a project
    @PostMapping("/{enseignantId}/projet/{projetId}/comment")
    public ResponseEntity<Commentaire> addComment(
        @PathVariable Integer enseignantId,
        @PathVariable Integer projetId,
        @RequestBody Map<String, String> requestBody) {
        
        String contenu = requestBody.get("contenu");
        Commentaire comment = espaceEchangeService.addComment(enseignantId, projetId, contenu);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{enseignantId}/projets")
    public List<ProjetWithBinomeDTO> getProjectsForEnseignant(@PathVariable Integer enseignantId) {
        return espaceEchangeService.getProjectsForEnseignant(enseignantId);
    }

}