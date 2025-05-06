package tn.enicarthage.services;

import tn.enicarthage.dto.BinomeDTO;
import tn.enicarthage.models.Binome;
import tn.enicarthage.models.ChoixProjet;
import tn.enicarthage.repositories.BinomeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BinomeService {

    private final BinomeRepository binomeRepository;

    public BinomeService(BinomeRepository binomeRepository) {
        this.binomeRepository = binomeRepository;
    }

    public List<BinomeDTO> getAllBinomesWithDetails() {
        List<Binome> binomes = binomeRepository.findAll();

        return binomes.stream().map(binome -> new BinomeDTO(
        		binome.getId(),
                binome.getEtud1().getPrenom(),
                binome.getEtud1().getNom(),
                binome.getEtud2().getPrenom(),
                binome.getEtud2().getNom(),
                binome.getEtud1().getFiliere(),
                binome.getEtud1().getGroupe(),  // Make sure 'groupe' exists
                binome.getEtud1().getMoyenneGeneral(),
                binome.getEtud2().getMoyenneGeneral(),
                binome.getMoyenneBinome(),
                binome.getChoixProjets().stream()
                    .sorted(Comparator.comparing(ChoixProjet::getPriorite)) // Sort by priorite
                    .map(cp -> cp.getProjet().getTitre()) // Accessing 'projet' and getting 'titre'
                    .collect(Collectors.toList()) // Collect the titles in a list
        )).collect(Collectors.toList()); // Collect the BinomeDTO objects in a list
    }


}
