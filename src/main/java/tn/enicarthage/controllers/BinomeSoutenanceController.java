package tn.enicarthage.controllers;

import tn.enicarthage.models.Binome;
import tn.enicarthage.models.Projet;
import tn.enicarthage.services.BinomeSoutenanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/binomes")

public class BinomeSoutenanceController {

    private final BinomeSoutenanceService binomeService;

    @Autowired
    public BinomeSoutenanceController(BinomeSoutenanceService binomeService) {
        this.binomeService = binomeService;
    }

    @GetMapping
    public ResponseEntity<List<Binome>> getAllBinomes() {
        List<Binome> binomes = binomeService.getAllBinomes();
        return ResponseEntity.ok(binomes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Binome> getBinomeById(@PathVariable Integer id) {
        Binome binome = binomeService.getBinomeById(id);
        return ResponseEntity.ok(binome);
    }

    @PostMapping
    public ResponseEntity<Binome> createBinome(@RequestBody Binome binome) {
        Binome createdBinome = binomeService.createBinome(binome);
        return new ResponseEntity<>(createdBinome, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Binome> updateBinome(@PathVariable Integer id, @RequestBody Binome binomeDetails) {
        Binome updatedBinome = binomeService.updateBinome(id, binomeDetails);
        return ResponseEntity.ok(updatedBinome);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBinome(@PathVariable Integer id) {
        binomeService.deleteBinome(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/without-soutenance")
    public ResponseEntity<List<Binome>> getBinomesWithoutSoutenance() {
        List<Binome> binomes = binomeService.getBinomesWithoutSoutenance();
        return ResponseEntity.ok(binomes);
    }

    @PatchMapping("/{id}/average")
    public ResponseEntity<Binome> updateBinomeAverage(
            @PathVariable Integer id, 
            @RequestBody Map<String, BigDecimal> payload) {
        
        BigDecimal moyenneBinome = payload.get("moyenneBinome");
        if (moyenneBinome == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Binome updatedBinome = binomeService.updateBinomeAverage(id, moyenneBinome);
        return ResponseEntity.ok(updatedBinome);
    }
    
    @GetMapping("/student/{etudiantId}")
    public ResponseEntity<List<Binome>> getBinomesByEtudiant(@PathVariable Integer etudiantId) {
        List<Binome> binomes = binomeService.findBinomesByEtudiant(etudiantId);
        return ResponseEntity.ok(binomes);
    }
    
    @GetMapping("/average-range")
    public ResponseEntity<List<Binome>> getBinomesByAverageRange(
            @RequestParam BigDecimal min, 
            @RequestParam BigDecimal max) {
        
        List<Binome> binomes = binomeService.findBinomesByAverageRange(min, max);
        return ResponseEntity.ok(binomes);
    }
    
    
    
    @GetMapping("/{id}/projet")
    public ResponseEntity<Projet> getProjetByBinomeId(@PathVariable Integer id) {
        return binomeService.getProjetByBinomeId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}