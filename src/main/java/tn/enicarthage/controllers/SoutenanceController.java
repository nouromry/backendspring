package tn.enicarthage.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tn.enicarthage.dto.SoutenanceCreateDTO;
import tn.enicarthage.models.Soutenance;
import tn.enicarthage.services.SoutenanceService;

@RestController
@RequestMapping("/api/soutenances")
@RequiredArgsConstructor
public class SoutenanceController {
    
    private final SoutenanceService soutenanceService;
    
    @PostMapping
    public ResponseEntity<Soutenance> createSoutenance(@RequestBody SoutenanceCreateDTO soutenanceDTO) {
        try {
            Soutenance createdSoutenance = soutenanceService.createSoutenance(soutenanceDTO);
            return new ResponseEntity<>(createdSoutenance, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Soutenance>> getAllSoutenances() {
        List<Soutenance> soutenances = soutenanceService.getAllSoutenances();
        return new ResponseEntity<>(soutenances, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Soutenance> getSoutenance(@PathVariable Integer id) {
        return soutenanceService.getSoutenanceById(id)
            .map(soutenance -> new ResponseEntity<>(soutenance, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoutenance(@PathVariable Integer id) {
        try {
            soutenanceService.deleteSoutenance(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Additional controller methods can be added as needed
}