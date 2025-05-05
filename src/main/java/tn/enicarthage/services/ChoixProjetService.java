package tn.enicarthage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.enicarthage.dto.ProjetChoixDTO;
import tn.enicarthage.dto.ProjetDTO;
import tn.enicarthage.models.*;
import tn.enicarthage.repositories.BinomeRepository;
import tn.enicarthage.repositories.ChoixProjetRepository;
import tn.enicarthage.repositories.EtudiantRepository;
import tn.enicarthage.repositories.ProjetRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChoixProjetService {

    private final BinomeRepository binomeRepository;
    private final EtudiantRepository etudiantRepository;
    private final ProjetRepository projetRepository;
    private final ChoixProjetRepository choixProjetRepository;

    @Autowired
    public ChoixProjetService(
            BinomeRepository binomeRepository,
            EtudiantRepository etudiantRepository,
            ProjetRepository projetRepository,
            ChoixProjetRepository choixProjetRepository) {
        this.binomeRepository = binomeRepository;
        this.etudiantRepository = etudiantRepository;
        this.projetRepository = projetRepository;
        this.choixProjetRepository = choixProjetRepository;
    }

    @Transactional
    public Binome creerBinome(String matriculeEtud1, String matriculeEtud2) throws Exception {
        if (matriculeEtud1.equals(matriculeEtud2)) {
            throw new Exception("Les deux étudiants doivent être différents");
        }
        
        Etudiant etudiant1 = etudiantRepository.findByMatricule(matriculeEtud1)
            .orElseThrow(() -> new Exception("L'étudiant avec le matricule " + matriculeEtud1 + " n'existe pas"));
        
        Etudiant etudiant2 = etudiantRepository.findByMatricule(matriculeEtud2)
            .orElseThrow(() -> new Exception("L'étudiant avec le matricule " + matriculeEtud2 + " n'existe pas"));
        
        Optional<Binome> existingBinome = binomeRepository.findByEtud1OrEtud2(etudiant1, etudiant2);
        if (existingBinome.isPresent()) {
            if (existingBinome.get().getEtud1().equals(etudiant1) || existingBinome.get().getEtud2().equals(etudiant1)) {
                throw new Exception("L'étudiant " + etudiant1.getNom() + " " + etudiant1.getPrenom() +
                                  " fait déjà partie d'un binôme");
            }
            
            if (existingBinome.get().getEtud1().equals(etudiant2) || existingBinome.get().getEtud2().equals(etudiant2)) {
                throw new Exception("L'étudiant " + etudiant2.getNom() + " " + etudiant2.getPrenom() +
                                  " fait déjà partie d'un binôme");
            }
        }
        
        if (!etudiant1.getFiliere().equals(etudiant2.getFiliere())) {
            throw new Exception("Les deux étudiants doivent appartenir à la même filière");
        }
        
        Binome binome = new Binome();
        binome.setEtud1(etudiant1);
        binome.setEtud2(etudiant2);
        
        
        BigDecimal moyenneEtud1 = etudiant1.getMoyenneGeneral();
        BigDecimal moyenneEtud2 = etudiant2.getMoyenneGeneral();
        BigDecimal moyenneBinome = moyenneEtud1.add(moyenneEtud2).divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
        binome.setMoyenneBinome(moyenneBinome);
        
        return binomeRepository.save(binome);
    }
    
    
    @Transactional
    public void enregistrerChoixProjets(Integer idBinome, List<ProjetChoixDTO> choix) throws Exception {
        Binome binome = binomeRepository.findById(idBinome)
            .orElseThrow(() -> new Exception("Le binôme avec l'ID " + idBinome + " n'existe pas"));
        
        if (choix.size() > 10) {
            throw new Exception("Un maximum de 10 choix est autorisé");
        }
        
        if (choix.isEmpty()) {
            throw new Exception("Vous devez sélectionner au moins un projet");
        }
        
        choixProjetRepository.deleteByBinome(binome);
        
        List<Integer> prioritesUtilisees = new ArrayList<>();
        List<Integer> projetsSelectionnes = new ArrayList<>();
        
        for (ProjetChoixDTO choixDTO : choix) {
            if (choixDTO.getPriorite() < 1 || choixDTO.getPriorite() > 10) {
                throw new Exception("La priorité doit être comprise entre 1 et 10");
            }
            
            if (prioritesUtilisees.contains(choixDTO.getPriorite())) {
                throw new Exception("La priorité " + choixDTO.getPriorite() + " est utilisée plusieurs fois");
            }
            prioritesUtilisees.add(choixDTO.getPriorite());
            
            if (projetsSelectionnes.contains(choixDTO.getProjetId())) {
                throw new Exception("Le projet avec l'ID " + choixDTO.getProjetId() + " est sélectionné plusieurs fois");
            }
            projetsSelectionnes.add(choixDTO.getProjetId());
        }
        
        for (ProjetChoixDTO choixDTO : choix) {
            Projet projet = projetRepository.findById(choixDTO.getProjetId())
                .orElseThrow(() -> new Exception("Le projet avec l'ID " + choixDTO.getProjetId() + " n'existe pas"));
            
            if (projet.getEtat() != Projet.Etat.valide) {
                throw new Exception("Le projet '" + projet.getTitre() + "' n'est pas disponible pour sélection");
            }
            
            if (projet.getFiliere() != null && !projet.getFiliere().isEmpty() &&
                !projet.getFiliere().equals(binome.getEtud1().getFiliere())) {
                throw new Exception("Le projet '" + projet.getTitre() + 
                                  "' n'est pas disponible pour votre filière");
            }
            
            ChoixProjet choixProjet = new ChoixProjet();
            ChoixProjet.ChoixProjetId choixId = new ChoixProjet.ChoixProjetId(idBinome, choixDTO.getProjetId());
            choixProjet.setId(choixId);
            choixProjet.setBinome(binome);
            choixProjet.setProjet(projet);
            choixProjet.setPriorite(choixDTO.getPriorite());
            
            choixProjetRepository.save(choixProjet);
        }
    }
   
    public List<ProjetDTO> getProjetsDisponibles() {
        return projetRepository.findByEtat(Projet.Etat.valide)
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

   
    public List<ProjetDTO> getProjetsDisponiblesByFiliere(String filiere) {
        return projetRepository.findByEtatAndFiliere(Projet.Etat.valide, filiere).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
   
    public List<ProjetChoixDTO> getChoixProjetsByBinome(Integer idBinome) {
        Binome binome = binomeRepository.findById(idBinome)
            .orElseThrow(() -> new RuntimeException("Binôme non trouvé"));
            
        return choixProjetRepository.findByBinomeOrderByPrioriteAsc(binome).stream()
            .map(choix -> new ProjetChoixDTO(choix.getProjet().getId(), choix.getPriorite()))
            .collect(Collectors.toList());
    }
    
   
    private ProjetDTO convertToDto(Projet projet) {
        return new ProjetDTO(
            projet.getId(),
            projet.getTitre(),
            projet.getDescription(),
            projet.getTechnologies(),
            projet.getFiliere()
        );
    }
}