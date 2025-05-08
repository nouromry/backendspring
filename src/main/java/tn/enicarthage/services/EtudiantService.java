package tn.enicarthage.services;

import tn.enicarthage.dto.ProjetCreationDto;
import tn.enicarthage.models.*;
import tn.enicarthage.repositories.*;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        
        Enseignant enseignant = enseignantRepository.findBySpecialite(projetDto.getFiliere())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Aucun enseignant trouvé pour cette filière"));
        projet.setEnseignant(enseignant);
        
        projet.setEtudiantCreateur(etudiant);
        
        if (binome != null) {
            projet.setBinomeAffecte(binome);
        }
        
        return projetRepository.save(projet);
    }

    public List<Projet> getStudentProjects(Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        List<Projet> projets = new ArrayList<>();
        
        projets.addAll(projetRepository.findByEtudiantCreateur(etudiant));
        
        Binome binome = binomeRepository.findByEtud1OrEtud2(etudiant, etudiant).orElse(null);
        if (binome != null) {
            projets.addAll(projetRepository.findByBinomeAffecte(binome));
        }
        
        return projets;
    }
    public Optional<Etudiant> getEtudiantByMatricule(String matricule) {
        return etudiantRepository.findByMatricule(matricule);
    }
    
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }
}