package tn.enicarthage.services;

import tn.enicarthage.dto.CommentaireDTO;
import tn.enicarthage.dto.ProjetWithBinomeDTO;
import tn.enicarthage.models.*;
import tn.enicarthage.repositories.CommentaireRepository;
import tn.enicarthage.repositories.EnseignantRepository;
import tn.enicarthage.repositories.ProjetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspaceEchangeService {
    private final CommentaireRepository commentaireRepository;
    private final EnseignantRepository enseignantRepository;
    private final ProjetRepository projetRepository;

    public EspaceEchangeService(CommentaireRepository commentaireRepository, 
                              EnseignantRepository enseignantRepository,
                              ProjetRepository projetRepository) {
        this.commentaireRepository = commentaireRepository;
        this.enseignantRepository = enseignantRepository;
        this.projetRepository = projetRepository;
    }

    public List<Commentaire> getCommentsForEnseignant(Integer enseignantId) {
        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant not found"));
        return commentaireRepository.findCommentsForEnseignantProjects(enseignant);
    }

    public List<CommentaireDTO> getCommentsForProject(Integer projetId) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet not found"));
        
        return commentaireRepository.findByProjetIdWithAuthor(projetId)
                .stream()
                .map(c -> new CommentaireDTO(
                    c.getId(),
                    c.getContenu(),
                    c.getDateCommentaire(),
                    c.getAuteur()
                ))
                .collect(Collectors.toList());
    }
    @Transactional
    public Commentaire addComment(Integer enseignantId, Integer projetId, String contenu) {
        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant not found"));
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet not found"));
        
        if (!projet.getEnseignant().getId().equals(enseignantId)) {
            throw new RuntimeException("This teacher doesn't supervise this project");
        }

        Commentaire commentaire = new Commentaire();
        commentaire.setContenu(contenu);
        commentaire.setDateCommentaire(new Date());
        commentaire.setAuteur(enseignant);
        commentaire.setProjet(projet);
        
        return commentaireRepository.save(commentaire);
    }

    
    public List<ProjetWithBinomeDTO> getProjectsForEnseignant(Integer enseignantId) {
        List<Projet> projets = projetRepository.findByEnseignantIdAndEtatValideWithBinome(enseignantId);
        
        if (projets.isEmpty()) {
            throw new RuntimeException("No validated projects found for this teacher");
        }
        
        return projets.stream()
            .map(ProjetWithBinomeDTO::new)
            .collect(Collectors.toList());
    }
}