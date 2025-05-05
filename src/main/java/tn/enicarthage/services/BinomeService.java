package tn.enicarthage.services;

import tn.enicarthage.dto.BinomeDTO;
import tn.enicarthage.dto.ProjetCreationDto;
import tn.enicarthage.models.*;
import tn.enicarthage.repositories.*;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BinomeService {

    private final BinomeRepository binomeRepository;
    private final EtudiantRepository etudiantRepository;
    private final ProjetRepository projetRepository;
    private final EnseignantRepository enseignantRepository;

    public BinomeService(BinomeRepository binomeRepository,
                       EtudiantRepository etudiantRepository,
                       ProjetRepository projetRepository,
                       EnseignantRepository enseignantRepository) {
        this.binomeRepository = binomeRepository;
        this.etudiantRepository = etudiantRepository;
        this.projetRepository = projetRepository;
        this.enseignantRepository = enseignantRepository;
    }

    public List<BinomeDTO> getAllBinomesWithDetails() {
        List<Binome> binomes = binomeRepository.findAll();
        return binomes.stream().map(this::convertToBinomeDTO).collect(Collectors.toList());
    }

    private BinomeDTO convertToBinomeDTO(Binome binome) {
        return new BinomeDTO(
                binome.getEtud1().getPrenom(),
                binome.getEtud1().getNom(),
                binome.getEtud2().getPrenom(),
                binome.getEtud2().getNom(),
                binome.getEtud1().getFiliere(),
                binome.getEtud1().getGroupe(),
                binome.getEtud1().getMoyenneGeneral(),
                binome.getMoyenneBinome(),
                binome.getChoixProjets().stream()
                    .sorted(Comparator.comparing(ChoixProjet::getPriorite))
                    .map(cp -> cp.getProjet().getTitre())
                    .collect(Collectors.toList())
        );
    }

    @Transactional
    public Projet createProjectForBinome(ProjetCreationDto projetDto, Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        Binome binome = binomeRepository.findByEtud1OrEtud2(etudiant, etudiant)
                .orElseThrow(() -> new RuntimeException("L'étudiant ne fait pas partie d'un binôme"));
        
        if (binome.getProjetAffecte() != null) {
            throw new IllegalStateException("Ce binôme a déjà un projet affecté");
        }
        
        Projet projet = new Projet();
        projet.setTitre(projetDto.getTitre());
        projet.setDescription(projetDto.getDescription());
        projet.setTechnologies(projetDto.getTechnologies());
        projet.setFiliere(projetDto.getFiliere());
        projet.setEtat(Projet.Etat.en_attente);
        projet.setDateDepot(Date.valueOf(LocalDate.now()));
        
        // Find teacher by specialite (assuming specialite matches filiere)
        Enseignant enseignant = enseignantRepository.findBySpecialite(projetDto.getFiliere())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Aucun enseignant trouvé pour cette filière/spécialité"));
        projet.setEnseignant(enseignant);
        
        projet.setBinomeAffecte(binome);
        return projetRepository.save(projet);
    }

    public List<Projet> getProjectsByBinome(Integer binomeId) {
        if (!binomeRepository.existsById(binomeId)) {
            throw new RuntimeException("Binôme non trouvé");
        }
        return projetRepository.findByBinomeAffecteId(binomeId);
    }
}