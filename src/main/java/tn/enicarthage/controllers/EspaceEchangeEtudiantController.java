package tn.enicarthage.controllers;

import tn.enicarthage.dto.CommentaireDTO;
import tn.enicarthage.dto.ProjetWithBinomeDTO;
import tn.enicarthage.models.Commentaire;
import tn.enicarthage.services.EspaceEchangeEtudiantService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;



@RestController
@RequestMapping("/api/etudiant/echange")
public class EspaceEchangeEtudiantController {
    private final EspaceEchangeEtudiantService espaceEchangeEtudiantService;

    public EspaceEchangeEtudiantController(EspaceEchangeEtudiantService espaceEchangeEtudiantService) {
        this.espaceEchangeEtudiantService = espaceEchangeEtudiantService;
    }

    @GetMapping("/{etudiantId}/projet")
    public ResponseEntity<ProjetWithBinomeDTO> getProjetForEtudiant(@PathVariable Integer etudiantId) {
        Optional<ProjetWithBinomeDTO> projet = espaceEchangeEtudiantService.getProjetForEtudiant(etudiantId);
        return projet.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{etudiantId}/comments")
    public ResponseEntity<List<CommentaireDTO>> getCommentsForEtudiantProjet(@PathVariable Integer etudiantId) {
        try {
            List<CommentaireDTO> comments = espaceEchangeEtudiantService.getCommentsForEtudiantProjet(etudiantId);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{etudiantId}/comment")
    public ResponseEntity<Commentaire> addComment(
            @PathVariable Integer etudiantId,
            @RequestBody Map<String, String> requestBody) {
        
        try {
            String contenu = requestBody.get("contenu");
            Commentaire comment = espaceEchangeEtudiantService.addComment(etudiantId, contenu);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{etudiantId}/has-binome")
    public ResponseEntity<Map<String, Boolean>> hasActiveBinome(@PathVariable Integer etudiantId) {
        boolean hasBinome = espaceEchangeEtudiantService.hasActiveBinome(etudiantId);
        return ResponseEntity.ok(Map.of("hasBinome", hasBinome));
    }
    
    @GetMapping("/{etudiantId}/has-project")
    public ResponseEntity<Map<String, Boolean>> hasAssignedProject(@PathVariable Integer etudiantId) {
        boolean hasProject = espaceEchangeEtudiantService.hasAssignedProject(etudiantId);
        return ResponseEntity.ok(Map.of("hasProject", hasProject));
    }
    
    @GetMapping("/{etudiantId}/my-comments")
    public ResponseEntity<List<CommentaireDTO>> getCommentsByEtudiant(@PathVariable Integer etudiantId) {
        try {
            List<CommentaireDTO> comments = espaceEchangeEtudiantService.getCommentsByEtudiant(etudiantId);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{etudiantId}/latest-comments")
    public ResponseEntity<List<CommentaireDTO>> getLatestCommentsForEtudiantProjects(@PathVariable Integer etudiantId) {
        try {
            List<CommentaireDTO> comments = espaceEchangeEtudiantService.getLatestCommentsForEtudiantProjects(etudiantId);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{etudiantId}/unread-comments")
    public ResponseEntity<List<CommentaireDTO>> getUnreadCommentsForEtudiant(@PathVariable Integer etudiantId) {
        try {
            List<CommentaireDTO> comments = espaceEchangeEtudiantService.getUnreadCommentsForEtudiant(etudiantId);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{etudiantId}/all-comments")
    public ResponseEntity<List<CommentaireDTO>> getAllCommentsForEtudiantProjects(@PathVariable Integer etudiantId) {
        try {
            List<CommentaireDTO> comments = espaceEchangeEtudiantService.getAllCommentsForEtudiantProjects(etudiantId);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}