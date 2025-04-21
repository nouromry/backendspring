package tn.enicarthage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.enicarthage.models.Enseignant;
import tn.enicarthage.repositories.EnseignantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findAll();
    }

    public Enseignant getEnseignantById(Integer id) {
        return enseignantRepository.findById(id).orElse(null);
    }

    public Enseignant getEnseignantByEmail(String email) {
        return enseignantRepository.findByEmail(email);
    }

    public List<Enseignant> getEnseignantsBySpecialite(String specialite) {
        return enseignantRepository.findBySpecialite(specialite);
    }

    public Enseignant createEnseignant(Enseignant enseignant) {
        return enseignantRepository.save(enseignant);
    }

    public Enseignant updateEnseignant(Integer id, Enseignant enseignant) {
        Enseignant existing = enseignantRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setNom(enseignant.getNom());
            existing.setPrenom(enseignant.getPrenom());
            existing.setEmail(enseignant.getEmail());
            existing.setSpecialite(enseignant.getSpecialite());
            return enseignantRepository.save(existing);
        }
        return null;
    }

    public void deleteEnseignant(Integer id) {
        enseignantRepository.deleteById(id);
    }

    public List<Enseignant> searchEnseignantsByName(String nom) {
        return enseignantRepository.findByNomContainingIgnoreCase(nom);
    }
}
