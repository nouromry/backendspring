package tn.enicarthage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tn.enicarthage.dto.ProjetCreationDto;
import tn.enicarthage.dto.ProjetWithBinomeDTO;
import tn.enicarthage.dto.SoutenanceWithRoleDTO;
import tn.enicarthage.models.Enseignant;
import tn.enicarthage.models.JurySoutenance;
import tn.enicarthage.models.Projet;
import tn.enicarthage.repositories.EnseignantRepository;
import tn.enicarthage.repositories.JurySoutenanceRepository;
import tn.enicarthage.repositories.ProjetRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnseignantService {

    @Autowired
    private final ProjetRepository projetRepository;
    private final EnseignantRepository enseignantRepository;
    private final JurySoutenanceRepository jurySoutenanceRepository;


    public EnseignantService(ProjetRepository projetRepository, 
                           EnseignantRepository enseignantRepository,JurySoutenanceRepository jurySoutenanceRepository ) {
        this.projetRepository = projetRepository;
        this.enseignantRepository = enseignantRepository;
        this.jurySoutenanceRepository = jurySoutenanceRepository;
    }
    public List<SoutenanceWithRoleDTO> getSoutenancesByEnseignant(Integer enseignantId) {
        List<JurySoutenance> jurySoutenances = jurySoutenanceRepository
                .findWithSoutenanceDetailsByEnseignantId(enseignantId);
        
        return jurySoutenances.stream()
                .map(js -> new SoutenanceWithRoleDTO(js.getSoutenance(), js.getRole()))
                .collect(Collectors.toList());
    }
    @Transactional
    public Projet createProject(ProjetCreationDto projetDto, Integer enseignantId) {
        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant not found"));
        
        Projet projet = new Projet();
        projet.setTitre(projetDto.getTitre());
        projet.setDescription(projetDto.getDescription());
        projet.setTechnologies(projetDto.getTechnologies());
        projet.setEtat(Projet.Etat.en_attente);
        projet.setDateDepot(new Date());
        projet.setFiliere(projetDto.getFiliere());
        projet.setEnseignant(enseignant);
        
        return projetRepository.save(projet);
    }

    public List<Projet> getProjectsByEnseignant(Integer enseignantId) {
        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant not found"));
        return projetRepository.findByEnseignant(enseignant);
    }
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
    public List<ProjetWithBinomeDTO> getValidProjectsWithBinomeDetails(Integer enseignantId) {
        return projetRepository.findValidProjectsWithBinomeDetailsByEnseignantId(enseignantId)
                .stream()
                .map(ProjetWithBinomeDTO::new)
                .collect(Collectors.toList());
    }

}
