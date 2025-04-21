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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SoutenanceService {
    
    private final SoutenanceRepository soutenanceRepository;
    private final JurySoutenanceRepository jurySoutenanceRepository;
    private final BinomeRepository binomeRepository;
    private final EnseignantRepository enseignantRepository;
    
    @Transactional
    public Soutenance createSoutenance(SoutenanceCreateDTO dto) {
        // Find the Binome
        Binome binome = binomeRepository.findById(dto.getBinomeId())
            .orElseThrow(() -> new RuntimeException("Binome with ID " + dto.getBinomeId() + " not found"));
            
        // Check if the binome already has a defense session
        if (soutenanceRepository.findAll().stream().anyMatch(s -> s.getBinome().getId().equals(dto.getBinomeId()))) {
            throw new RuntimeException("This binome already has a scheduled defense session");
        }
        
        // Create the Soutenance entity
        Soutenance soutenance = new Soutenance();
        soutenance.setDate(dto.getDate());
        soutenance.setDuree(dto.getDuree());
        soutenance.setHeureD(dto.getHeureD());
        soutenance.setHeureF(dto.getHeureF());
        soutenance.setSalle(dto.getSalle());
        soutenance.setBinome(binome);
        soutenance.setJury(new ArrayList<>());
        
        // Save the Soutenance to get an ID
        soutenance = soutenanceRepository.save(soutenance);
        
        // Add jury members
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
                jury.setRole(juryMember.getRole());
                
                jurySoutenanceRepository.save(jury);
            }
        }
        
        // Refresh the soutenance with jury
        return soutenanceRepository.findById(soutenance.getId()).orElseThrow();
    }
    
    public List<Soutenance> getAllSoutenances() {
        return soutenanceRepository.findAll();
    }
    
    public Optional<Soutenance> getSoutenanceById(Integer id) {
        return soutenanceRepository.findById(id);
    }
    
    @Transactional
    public void deleteSoutenance(Integer id) {
        Soutenance soutenance = soutenanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Defense session not found"));
        
        // Delete all jury associations first
        soutenance.getJury().forEach(jury -> 
            jurySoutenanceRepository.deleteById(jury.getId())
        );
        
        // Then delete the defense session
        soutenanceRepository.deleteById(id);
    }
    
    // You can add more service methods as needed
}