package tn.enicarthage.services;

import tn.enicarthage.dto.CommentaireDTO;
import tn.enicarthage.dto.ProjetWithBinomeDTO;
import tn.enicarthage.models.*;
import tn.enicarthage.repositories.CommentaireRepository;
import tn.enicarthage.repositories.EtudiantRepository;
import tn.enicarthage.repositories.ProjetRepository;
import tn.enicarthage.repositories.BinomeSoutenanceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class EspaceEchangeEtudiantService {
    private final CommentaireRepository commentaireRepository;
    private final EtudiantRepository etudiantRepository;
    private final ProjetRepository projetRepository;
    private final BinomeSoutenanceRepository binomeRepository;

    public EspaceEchangeEtudiantService(CommentaireRepository commentaireRepository,
                                     EtudiantRepository etudiantRepository,
                                     ProjetRepository projetRepository,
                                     BinomeSoutenanceRepository binomeRepository) {
        this.commentaireRepository = commentaireRepository;
        this.etudiantRepository = etudiantRepository;
        this.projetRepository = projetRepository;
        this.binomeRepository = binomeRepository;
    }

    // Get the project associated with a student through their binome
    public Optional<ProjetWithBinomeDTO> getProjetForEtudiant(Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Etudiant not found with id: " + etudiantId));
        
        Binome binome = etudiant.getBinome();
        if (binome == null) {
            return Optional.empty();
        }
        
        Projet projet = binome.getProjetAffecte();
        if (projet == null) {
            return Optional.empty();
        }
        
        // Check if the project is in VALIDE state
        if (projet.getEtat() != Projet.Etat.valide) {
            return Optional.empty();
        }
        
        return Optional.of(new ProjetWithBinomeDTO(projet));
    }

    // Get comments for the student's project
    public List<CommentaireDTO> getCommentsForEtudiantProjet(Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Etudiant not found with id: " + etudiantId));
        
        Binome binome = etudiant.getBinome();
        if (binome == null) {
            throw new RuntimeException("Etudiant is not part of a binome");
        }
        
        Projet projet = binome.getProjetAffecte();
        if (projet == null) {
            throw new RuntimeException("Binome has no assigned project");
        }
        
        return commentaireRepository.findByProjetIdWithAuthor(projet.getId())
                .stream()
                .map(c -> new CommentaireDTO(
                    c.getId(),
                    c.getContenu(),
                    c.getDateCommentaire(),
                    c.getAuteur()
                ))
                .collect(Collectors.toList());
    }

    // Add a new comment from a student to their project
    @Transactional
    public Commentaire addComment(Integer etudiantId, String contenu) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));
        
        Binome binome = etudiant.getBinome();
        if (binome == null) {
            throw new RuntimeException("Etudiant is not part of a binome");
        }
        
        Projet projet = binome.getProjetAffecte();
        if (projet == null) {
            throw new RuntimeException("Binome has no assigned project");
        }
        
        // Check if the project is in VALIDE state
        if (projet.getEtat() != Projet.Etat.valide) {
            throw new RuntimeException("Project is not in a valid state for comments");
        }
        
        Commentaire commentaire = new Commentaire();
        commentaire.setContenu(contenu);
        commentaire.setDateCommentaire(new Date());
        commentaire.setAuteur(etudiant);
        commentaire.setProjet(projet);
        
        return commentaireRepository.save(commentaire);
    }
    
    // Check if student has a binome
    public boolean hasActiveBinome(Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));
        
        return etudiant.getBinome() != null;
    }
    
    // Check if student has a project
    public boolean hasAssignedProject(Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));
        
        Binome binome = etudiant.getBinome();
        if (binome == null) {
            return false;
        }
        
        return binome.getProjetAffecte() != null;
    }
    
    // Get comments created by a specific student
    public List<CommentaireDTO> getCommentsByEtudiant(Integer etudiantId) {
        etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));
                
        return commentaireRepository.findCommentsByEtudiant(etudiantId)
                .stream()
                .map(c -> new CommentaireDTO(
                    c.getId(),
                    c.getContenu(),
                    c.getDateCommentaire(),
                    c.getAuteur()
                ))
                .collect(Collectors.toList());
    }
    
    // Get the latest comments for a student's project
    public List<CommentaireDTO> getLatestCommentsForEtudiantProjects(Integer etudiantId) {
        etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));
                
        return commentaireRepository.findLatestCommentsForEtudiantProjects(etudiantId)
                .stream()
                .map(c -> new CommentaireDTO(
                    c.getId(),
                    c.getContenu(),
                    c.getDateCommentaire(),
                    c.getAuteur()
                ))
                .collect(Collectors.toList());
    }
    
    // Get unread comments for a student (comments from the teacher after student's last comment)
    public List<CommentaireDTO> getUnreadCommentsForEtudiant(Integer etudiantId) {
        etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));
                
        return commentaireRepository.findUnreadCommentsForEtudiant(etudiantId)
                .stream()
                .map(c -> new CommentaireDTO(
                    c.getId(),
                    c.getContenu(),
                    c.getDateCommentaire(),
                    c.getAuteur()
                ))
                .collect(Collectors.toList());
    }
    
    // Get all comments related to a student's project (with better performance than the original method)
    public List<CommentaireDTO> getAllCommentsForEtudiantProjects(Integer etudiantId) {
        etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));
                
        return commentaireRepository.findCommentsForEtudiantProjects(etudiantId)
                .stream()
                .map(c -> new CommentaireDTO(
                    c.getId(),
                    c.getContenu(),
                    c.getDateCommentaire(),
                    c.getAuteur()
                ))
                .collect(Collectors.toList());
    }
}