package tn.enicarthage.services;

import tn.enicarthage.models.Etudiant;
import tn.enicarthage.repositories.EtudiantRepository;
import org.springframework.stereotype.Service;

@Service
public class EtudiantService {
    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    public Etudiant createEtudiant(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }
}
