package tn.enicarthage.services;

import tn.enicarthage.dto.ProjetCreationDto;
import tn.enicarthage.models.*;
import tn.enicarthage.repositories.*;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EtudiantService {
    private final EtudiantRepository etudiantRepository;
    private final BinomeRepository binomeRepository;
    private final ProjetRepository projetRepository;
    private final EnseignantRepository enseignantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository,
                         BinomeRepository binomeRepository,
                         ProjetRepository projetRepository,
                         EnseignantRepository enseignantRepository) {
        this.etudiantRepository = etudiantRepository;
        this.binomeRepository = binomeRepository;
        this.projetRepository = projetRepository;
        this.enseignantRepository = enseignantRepository;
    }

    @Transactional
    public Projet createProject(ProjetCreationDto projetDto, Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        // Check if student is in a binôme
        Binome binome = binomeRepository.findByEtud1OrEtud2(etudiant, etudiant)
                .orElse(null);
        
        if (binome != null && binome.getProjetAffecte() != null) {
            throw new IllegalStateException("Ce binôme a déjà un projet affecté");
        }
        
        Projet projet = new Projet();
        projet.setTitre(projetDto.getTitre());
        projet.setDescription(projetDto.getDescription());
        projet.setTechnologies(projetDto.getTechnologies());
        projet.setFiliere(projetDto.getFiliere());
        projet.setEtat(Projet.Etat.en_attente);
        projet.setDateDepot(java.sql.Date.valueOf(LocalDate.now()));
        
        // Assign teacher based on filiere/specialite
        Enseignant enseignant = enseignantRepository.findBySpecialite(projetDto.getFiliere())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Aucun enseignant trouvé pour cette filière"));
        projet.setEnseignant(enseignant);
        
        // Set the student creator
        projet.setEtudiantCreateur(etudiant);
        
        // If student is in a binôme, assign the binôme
        if (binome != null) {
            projet.setBinomeAffecte(binome);
        }
        
        return projetRepository.save(projet);
    }

    public List<Projet> getStudentProjects(Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        // Get projects either through binôme or directly as creator
        List<Projet> projets = new ArrayList<>();
        
        // Get projects where student is creator
        projets.addAll(projetRepository.findByEtudiantCreateur(etudiant));
        
        // Get projects through binôme
        Binome binome = binomeRepository.findByEtud1OrEtud2(etudiant, etudiant).orElse(null);
        if (binome != null) {
            projets.addAll(projetRepository.findByBinomeAffecte(binome));
        }
        
        return projets;
    }
}