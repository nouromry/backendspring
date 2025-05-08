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
        
        if (dto.getHeureDebut() != null) {
            try {
                soutenance.setHeureD(Time.valueOf(dto.getHeureDebut() + ":00"));
            } catch (Exception e) {
                throw new RuntimeException("Invalid time format. Expected HH:MM");
            }
        } else {
            soutenance.setHeureD(Time.valueOf("09:00:00"));
        }
        
        soutenance.setSalle(dto.getSalle());
        soutenance.setBinome(binome);
        
        if (soutenance.getJury() == null) {
            soutenance.setJury(new ArrayList<>());
        }
        
        soutenance = soutenanceRepository.save(soutenance);
        
        if (dto.getJuryMembers() != null && !dto.getJuryMembers().isEmpty()) {
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
    
    @Transactional
    public SoutenanceCreateDTO createSoutenanceView(SoutenanceCreateDTO dto) {
        Soutenance soutenance = createSoutenance(dto);
        return convertToDTO(soutenance);
    }
    
    public List<Soutenance> getAllSoutenances() {
        return soutenanceRepository.findAll();
    }
    
    public List<SoutenanceCreateDTO> getAllSoutenanceViews() {
        return soutenanceRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public Optional<Soutenance> getSoutenanceById(Integer id) {
        return soutenanceRepository.findById(id);
    }
    
    public Optional<SoutenanceCreateDTO> getSoutenanceViewById(Integer id) {
        return soutenanceRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    public List<SoutenanceCreateDTO> getSoutenanceViewsByDate(Date date) {
        return soutenanceRepository.findAll().stream()
            .filter(s -> s.getDate().equals(date))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public SoutenanceCreateDTO updateSoutenance(Integer id, SoutenanceCreateDTO dto) {
        Soutenance soutenance = soutenanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Defense session not found"));
        
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
        
        if (dto.getBinomeId() != null && !dto.getBinomeId().equals(soutenance.getBinome().getId())) {
            if (soutenanceRepository.findAll().stream()
                    .anyMatch(s -> !s.getId().equals(id) && s.getBinome().getId().equals(dto.getBinomeId()))) {
                throw new RuntimeException("This binome already has a scheduled defense session");
            }
            
            Binome binome = binomeRepository.findById(dto.getBinomeId())
                .orElseThrow(() -> new RuntimeException("Binome with ID " + dto.getBinomeId() + " not found"));
            soutenance.setBinome(binome);
        }
        
        if (dto.getJuryMembers() != null && !dto.getJuryMembers().isEmpty()) {
            if (soutenance.getJury() != null) {
                for (JurySoutenance jury : new ArrayList<>(soutenance.getJury())) {
                    jurySoutenanceRepository.deleteById(jury.getId());
                }
                soutenance.getJury().clear();
            } else {
                soutenance.setJury(new ArrayList<>());
            }
            
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
    
    @Transactional
    public void deleteSoutenance(Integer id) {
        Soutenance soutenance = soutenanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Defense session not found"));
        
        if (soutenance.getJury() != null) {
            for (JurySoutenance jury : new ArrayList<>(soutenance.getJury())) {
                jurySoutenanceRepository.deleteById(jury.getId());
            }
        }
        
        soutenanceRepository.deleteById(id);
    }
    
    public SoutenanceCreateDTO convertToDTO(Soutenance soutenance) {
        SoutenanceCreateDTO dto = new SoutenanceCreateDTO();
        dto.setId(soutenance.getId());
        dto.setSalle(soutenance.getSalle());
        
        if (soutenance.getDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dto.setDateSoutenance(dateFormat.format(soutenance.getDate()));
        }
        
        if (soutenance.getHeureD() != null) {
            String timeStr = soutenance.getHeureD().toString();
            if (timeStr.length() >= 5) {
                timeStr = timeStr.substring(0, 5);
            }
            dto.setHeureDebut(timeStr);
        }
        
        Binome binome = soutenance.getBinome();
        if (binome != null) {
            dto.setBinomeId(binome.getId());
            
            if (binome.getEtud1() != null) {
                dto.setBinomeEtudiant1(binome.getEtud1().getNom() + " " + binome.getEtud1().getPrenom());
            }
            
            if (binome.getEtud2() != null) {
                dto.setBinomeEtudiant2(binome.getEtud2().getNom() + " " + binome.getEtud2().getPrenom());
            }
            
            Projet projet = binome.getProjetAffecte();
            if (projet != null) {
                dto.setProjetId(projet.getId());
                dto.setProjetTitre(projet.getTitre());
                dto.setProjetDescription(projet.getDescription());
                dto.setProjetTechnologies(projet.getTechnologies());
            }
        }
        
        if (soutenance.getJury() != null && !soutenance.getJury().isEmpty()) {
            List<SoutenanceCreateDTO.JuryMemberDTO> juryMemberDTOs = new ArrayList<>();
            StringBuilder enseignants = new StringBuilder();
            
            for (JurySoutenance juryMember : soutenance.getJury()) {
                if (juryMember.getEnseignant() != null) {
                    SoutenanceCreateDTO.JuryMemberDTO juryMemberDTO = new SoutenanceCreateDTO.JuryMemberDTO();
                    juryMemberDTO.setEnseignantId(juryMember.getEnseignant().getId());
                    juryMemberDTO.setRole(juryMember.getRole());
                    
                    juryMemberDTOs.add(juryMemberDTO);
                    
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
    
    @Transactional
    public void addJuryMembersBatch(List<SoutenanceCreateDTO.JuryMemberDTO> juryMembers) {
        if (juryMembers == null || juryMembers.isEmpty()) {
            throw new RuntimeException("No jury members provided");
        }
        
        for (SoutenanceCreateDTO.JuryMemberDTO juryMember : juryMembers) {
            Enseignant enseignant = enseignantRepository.findById(juryMember.getEnseignantId())
                .orElseThrow(() -> new RuntimeException("Enseignant with ID " + juryMember.getEnseignantId() + " not found"));
            
            List<JurySoutenance> existingJuryRoles = jurySoutenanceRepository.findAll().stream()
                .filter(j -> j.getEnseignant().getId().equals(enseignant.getId()))
                .collect(Collectors.toList());
                    }
    }
    
    
    public Optional<SoutenanceCreateDTO> getSoutenanceViewByBinomeId(Integer binomeId) {
        return soutenanceRepository.findAll().stream()
            .filter(s -> s.getBinome().getId().equals(binomeId))
            .findFirst()
            .map(this::convertToDTO);
    }

}