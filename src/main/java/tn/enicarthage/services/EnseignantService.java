package tn.enicarthage.services;

import tn.enicarthage.models.Enseignant;
import tn.enicarthage.repositories.EnseignantRepository;
import org.springframework.stereotype.Service;

@Service
public class EnseignantService {
    private final EnseignantRepository enseignantRepository;

    public EnseignantService(EnseignantRepository enseignantRepository) {
        this.enseignantRepository = enseignantRepository;
    }

    public Enseignant createEnseignant(Enseignant enseignant) {
        return enseignantRepository.save(enseignant);
    }
}