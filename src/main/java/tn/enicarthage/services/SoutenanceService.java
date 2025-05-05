package tn.enicarthage.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.dto.SoutenanceCreateDTO;
import tn.enicarthage.models.*;
import tn.enicarthage.repositories.BinomeRepository;
import tn.enicarthage.repositories.EnseignantRepository;
import tn.enicarthage.repositories.JurySoutenanceRepository;
import tn.enicarthage.repositories.SoutenanceRepository;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoutenanceService {
    
    private final SoutenanceRepository soutenanceRepository;
    private final JurySoutenanceRepository jurySoutenanceRepository;
    private final BinomeRepository binomeRepository;
    private final EnseignantRepository enseignantRepository;
    
    @Transactional
    public Soutenance createSoutenance(SoutenanceCreateDTO dto) {
        Binome binome = binomeRepository.findById(dto.getBinomeId())
            .orElseThrow(() -> new RuntimeException("Binome with ID " + dto.getBinomeId() + " not found"));
           
        if (soutenanceRepository.findAll().stream().anyMatch(s -> s.getBinome().getId().equals(dto.getBinomeId()))) {
            throw new RuntimeException("This binome already has a scheduled defense session");
        }

        Soutenance soutenance = new Soutenance();
        
        // Parse and set date
        if (dto.getDateSoutenance() != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                soutenance.setDate(dateFormat.parse(dto.getDateSoutenance()));
            } catch (ParseException e) {
                throw new RuntimeException("Invalid date format. Expected yyyy-MM-dd");
            }
        } else {
            throw new RuntimeException("Date is required");
        }
        
        // Parse and set start time (heureD)
        if (dto.getHeureDebut() != null) {
            try {
                // Convert time string to Time object
                soutenance.setHeureD(Time.valueOf(dto.getHeureDebut() + ":00"));
            } catch (Exception e) {
                throw new RuntimeException("Invalid time format. Expected HH:MM");
            }
        } else {
            // Set a default time if none provided
            soutenance.setHeureD(Time.valueOf("09:00:00"));
        }
        
        soutenance.setSalle(dto.getSalle());
        soutenance.setBinome(binome);
        
        // Initialize the jury list if it's null
        if (soutenance.getJury() == null) {
            soutenance.setJury(new ArrayList<>());
        }
        
        soutenance = soutenanceRepository.save(soutenance);
        
        // Add jury members if provided
        if (dto.getJuryMembers() != null && !dto.getJuryMembers().isEmpty()) {
            for (SoutenanceCreateDTO.JuryMemberDTO juryMember : dto.getJuryMembers()) {
                // Make sure we're retrieving an Enseignant, not an Etudiant
                Enseignant enseignant = enseignantRepository.findById(juryMember.getEnseignantId())
                    .orElseThrow(() -> new RuntimeException("Enseignant with ID " + juryMember.getEnseignantId() + " not found"));
                
                JurySoutenance jury = new JurySoutenance();
                JurySoutenance.JurySoutenanceId juryId = new JurySoutenance.JurySoutenanceId(
                    enseignant.getId(), 
                    soutenance.getId()
                );
                
                jury.setId(juryId);
                jury.setEnseignant(enseignant); // Make sure enseignant is of type Enseignant
                jury.setSoutenance(soutenance);
                
                // Set the role - Ensure we're properly handling the enum
                try {
                    jury.setRole(juryMember.getRole());
                } catch (Exception e) {
                    throw new RuntimeException("Error setting role: " + e.getMessage());
                }
                
                jurySoutenanceRepository.save(jury);
                soutenance.getJury().add(jury);
            }
        }
        
        return soutenanceRepository.findById(soutenance.getId()).orElseThrow();
    }
    
    // Method for creating a soutenance that returns a DTO
    @Transactional
    public SoutenanceCreateDTO createSoutenanceView(SoutenanceCreateDTO dto) {
        Soutenance soutenance = createSoutenance(dto);
        return convertToDTO(soutenance);
    }
    
    // Method to get all soutenances
    public List<Soutenance> getAllSoutenances() {
        return soutenanceRepository.findAll();
    }
    
    // Method to get all soutenances as DTOs
    public List<SoutenanceCreateDTO> getAllSoutenanceViews() {
        return soutenanceRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    // Method to get a soutenance by ID
    public Optional<Soutenance> getSoutenanceById(Integer id) {
        return soutenanceRepository.findById(id);
    }
    
    // Method to get a soutenance DTO by ID
    public Optional<SoutenanceCreateDTO> getSoutenanceViewById(Integer id) {
        return soutenanceRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    // Method to get soutenances by date as DTOs
    public List<SoutenanceCreateDTO> getSoutenanceViewsByDate(Date date) {
        return soutenanceRepository.findAll().stream()
            .filter(s -> s.getDate().equals(date))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    // Method to update a soutenance
    @Transactional
    public SoutenanceCreateDTO updateSoutenance(Integer id, SoutenanceCreateDTO dto) {
        Soutenance soutenance = soutenanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Defense session not found"));
        
        // Update fields
        if (dto.getDateSoutenance() != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                soutenance.setDate(dateFormat.parse(dto.getDateSoutenance()));
            } catch (ParseException e) {
                throw new RuntimeException("Invalid date format. Expected yyyy-MM-dd");
            }
        }
        
        if (dto.getHeureDebut() != null) {
            try {
                soutenance.setHeureD(Time.valueOf(dto.getHeureDebut() + ":00"));
            } catch (Exception e) {
                throw new RuntimeException("Invalid time format. Expected HH:MM");
            }
        }
        
        if (dto.getSalle() != null) {
            soutenance.setSalle(dto.getSalle());
        }
        
        // If binome ID is provided and different from current one
        if (dto.getBinomeId() != null && !dto.getBinomeId().equals(soutenance.getBinome().getId())) {
            // Check if new binome already has a defense
            if (soutenanceRepository.findAll().stream()
                    .anyMatch(s -> !s.getId().equals(id) && s.getBinome().getId().equals(dto.getBinomeId()))) {
                throw new RuntimeException("This binome already has a scheduled defense session");
            }
            
            Binome binome = binomeRepository.findById(dto.getBinomeId())
                .orElseThrow(() -> new RuntimeException("Binome with ID " + dto.getBinomeId() + " not found"));
            soutenance.setBinome(binome);
        }
        
        // Update jury if provided
        if (dto.getJuryMembers() != null && !dto.getJuryMembers().isEmpty()) {
            // Remove existing jury members
            if (soutenance.getJury() != null) {
                for (JurySoutenance jury : new ArrayList<>(soutenance.getJury())) {
                    jurySoutenanceRepository.deleteById(jury.getId());
                }
                soutenance.getJury().clear();
            } else {
                soutenance.setJury(new ArrayList<>());
            }
            
            // Add new jury members
            for (SoutenanceCreateDTO.JuryMemberDTO juryMember : dto.getJuryMembers()) {
                Enseignant enseignant = enseignantRepository.findById(juryMember.getEnseignantId())
                    .orElseThrow(() -> new RuntimeException("Enseignant with ID " + juryMember.getEnseignantId() + " not found"));
                
                JurySoutenance jury = new JurySoutenance();
                JurySoutenance.JurySoutenanceId juryId = new JurySoutenance.JurySoutenanceId(
                    enseignant.getId(), 
                    soutenance.getId()
                );
                
                jury.setId(juryId);
                jury.setEnseignant(enseignant);
                jury.setSoutenance(soutenance);
                
                // Set the role - Ensure we're properly handling the enum
                try {
                    jury.setRole(juryMember.getRole());
                } catch (Exception e) {
                    throw new RuntimeException("Error setting role: " + e.getMessage());
                }
                
                JurySoutenance savedJury = jurySoutenanceRepository.save(jury);
                soutenance.getJury().add(savedJury);
            }
        }
        
        soutenanceRepository.save(soutenance);
        return convertToDTO(soutenance);
    }
    
    // Method to delete a soutenance
    @Transactional
    public void deleteSoutenance(Integer id) {
        Soutenance soutenance = soutenanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Defense session not found"));
        
        // Delete all jury associations first
        if (soutenance.getJury() != null) {
            for (JurySoutenance jury : new ArrayList<>(soutenance.getJury())) {
                jurySoutenanceRepository.deleteById(jury.getId());
            }
        }
        
        // Then delete the defense session
        soutenanceRepository.deleteById(id);
    }
    
    // Helper method to convert Soutenance to SoutenanceCreateDTO
    public SoutenanceCreateDTO convertToDTO(Soutenance soutenance) {
        SoutenanceCreateDTO dto = new SoutenanceCreateDTO();
        dto.setId(soutenance.getId());
        dto.setSalle(soutenance.getSalle());
        
        // Format date
        if (soutenance.getDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dto.setDateSoutenance(dateFormat.format(soutenance.getDate()));
        }
        
        // Format time
        if (soutenance.getHeureD() != null) {
            // Format time to HH:MM (without seconds)
            String timeStr = soutenance.getHeureD().toString();
            if (timeStr.length() >= 5) {
                timeStr = timeStr.substring(0, 5);
            }
            dto.setHeureDebut(timeStr);
        }
        
        // Set binome information
        Binome binome = soutenance.getBinome();
        if (binome != null) {
            dto.setBinomeId(binome.getId());
            
            if (binome.getEtud1() != null) {
                dto.setBinomeEtudiant1(binome.getEtud1().getNom() + " " + binome.getEtud1().getPrenom());
            }
            
            if (binome.getEtud2() != null) {
                dto.setBinomeEtudiant2(binome.getEtud2().getNom() + " " + binome.getEtud2().getPrenom());
            }
            
            // Set project information if binome has an assigned project
            Projet projet = binome.getProjetAffecte();
            if (projet != null) {
                dto.setProjetId(projet.getId());
                dto.setProjetTitre(projet.getTitre());
                dto.setProjetDescription(projet.getDescription());
                dto.setProjetTechnologies(projet.getTechnologies());
            }
        }
        
        // Set jury members
        if (soutenance.getJury() != null && !soutenance.getJury().isEmpty()) {
            List<SoutenanceCreateDTO.JuryMemberDTO> juryMemberDTOs = new ArrayList<>();
            StringBuilder enseignants = new StringBuilder();
            
            for (JurySoutenance juryMember : soutenance.getJury()) {
                if (juryMember.getEnseignant() != null) {
                    SoutenanceCreateDTO.JuryMemberDTO juryMemberDTO = new SoutenanceCreateDTO.JuryMemberDTO();
                    juryMemberDTO.setEnseignantId(juryMember.getEnseignant().getId());
                    juryMemberDTO.setRole(juryMember.getRole());
                    
                    juryMemberDTOs.add(juryMemberDTO);
                    
                    // Build enseignants string
                    if (enseignants.length() > 0) {
                        enseignants.append(", ");
                    }
                    enseignants.append(juryMember.getEnseignant().getNom())
                              .append(" ")
                              .append(juryMember.getEnseignant().getPrenom())
                              .append(" (")
                              .append(juryMember.getRole().name())
                              .append(")");
                }
            }
            
            dto.setJuryMembers(juryMemberDTOs);
            dto.setEnseignants(enseignants.toString());
        }
        
        return dto;
    }
    
    // Method to add multiple jury members in batch
    @Transactional
    public void addJuryMembersBatch(List<SoutenanceCreateDTO.JuryMemberDTO> juryMembers) {
        if (juryMembers == null || juryMembers.isEmpty()) {
            throw new RuntimeException("No jury members provided");
        }
        
        for (SoutenanceCreateDTO.JuryMemberDTO juryMember : juryMembers) {
            // Validate enseignant exists
            Enseignant enseignant = enseignantRepository.findById(juryMember.getEnseignantId())
                .orElseThrow(() -> new RuntimeException("Enseignant with ID " + juryMember.getEnseignantId() + " not found"));
            
            // Get all soutenances for which this enseignant is a jury member
            List<JurySoutenance> existingJuryRoles = jurySoutenanceRepository.findAll().stream()
                .filter(j -> j.getEnseignant().getId().equals(enseignant.getId()))
                .collect(Collectors.toList());
            
            // Additional processing for batch operations can be added here
        }
    }
    
    
    public Optional<SoutenanceCreateDTO> getSoutenanceViewByBinomeId(Integer binomeId) {
        return soutenanceRepository.findAll().stream()
            .filter(s -> s.getBinome().getId().equals(binomeId))
            .findFirst()
            .map(this::convertToDTO);
    }

}