package tn.enicarthage.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.models.Projet;
import tn.enicarthage.repositories.ProjetRepository;
import tn.enicarthage.models.Projet.Etat;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjetService {

    private final ProjetRepository projetRepository;


    public List<Projet> getProjetsByEnseignantId(Integer enseignantId) {
        return projetRepository.findByEnseignantId(enseignantId);
    }
    
    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    public List<Projet> getProjetsByEtat(String etat) {
        return projetRepository.findByEtat(etat);
    }

    public List<Projet> searchProjets(String term) {
        return projetRepository.findByTitreContainingIgnoreCaseOrEnseignant_NomContainingIgnoreCase(term, term);
    }

    public List<Projet> filterByFiliere(String filiere) {
        return projetRepository.findByFiliere(filiere);
    }

    public Optional<Projet> getProjetById(Integer id) {
        return projetRepository.findById(id);
    }
    public Projet updateProjetStatus(Integer projetId, Etat etat) {
        Optional<Projet> projetOpt = projetRepository.findById(projetId);
        if (projetOpt.isPresent()) {
            Projet projet = projetOpt.get();
            projet.setEtat(etat);
            return projetRepository.save(projet);
        }
        return null;
    }

    public Projet saveProjet(Projet projet) {
        return projetRepository.save(projet);
    }
}









