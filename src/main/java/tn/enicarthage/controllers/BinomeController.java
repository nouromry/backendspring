package tn.enicarthage.controllers;

import tn.enicarthage.dto.BinomeDTO;
import tn.enicarthage.dto.ProjetCreationDto;
import tn.enicarthage.models.Projet;
import tn.enicarthage.services.BinomeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/binomes")
 // adjust if needed
public class BinomeController {

    private final BinomeService binomeService;

    public BinomeController(BinomeService binomeService) {
        this.binomeService = binomeService;
    }

    @GetMapping("/details")
    public List<BinomeDTO> getAllBinomeDetails() {
        return binomeService.getAllBinomesWithDetails();
    }
    
    @PostMapping("/{binomeId}/projets")
    public Projet createProject(@PathVariable Integer binomeId, 
                              @RequestBody ProjetCreationDto projetDto) {
        return binomeService.createProjectForBinome(projetDto, binomeId);
    }

    @GetMapping("/{binomeId}/projets")
    public List<Projet> getBinomeProjects(@PathVariable Integer binomeId) {
        return binomeService.getProjectsByBinome(binomeId);
    }
}
