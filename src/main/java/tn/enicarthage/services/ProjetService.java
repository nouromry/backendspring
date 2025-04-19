package tn.enicarthage.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.models.Projet;
import tn.enicarthage.repositories.ProjetRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjetService {

    private final ProjetRepository projetRepository;

    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    public Optional<Projet> getProjetById(Integer id) {
        return projetRepository.findById(id);
    }

    public List<Projet> getProjetsByEnseignantId(Integer enseignantId) {
        return projetRepository.findByEnseignantId(enseignantId);
    }
}
