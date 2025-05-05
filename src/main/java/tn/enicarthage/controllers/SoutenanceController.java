package tn.enicarthage.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<?> createSoutenance(@RequestBody SoutenanceCreateDTO soutenanceDTO) {
        try {
            // Log the incoming data for debugging
            System.out.println("Received request: " + soutenanceDTO);
            
            SoutenanceCreateDTO createdSoutenance = soutenanceService.createSoutenanceView(soutenanceDTO);
            return new ResponseEntity<>(createdSoutenance, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the full exception stack trace
            e.printStackTrace();
            
            // Return detailed error information
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", new Date().toString());
            
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<SoutenanceCreateDTO>> getAllSoutenances() {
        List<SoutenanceCreateDTO> soutenances = soutenanceService.getAllSoutenanceViews();
        return new ResponseEntity<>(soutenances, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSoutenance(@PathVariable Integer id) {
        return soutenanceService.getSoutenanceViewById(id)
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
    
    @GetMapping("/by-date")
    public ResponseEntity<List<SoutenanceCreateDTO>> getSoutenancesByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        List<SoutenanceCreateDTO> soutenances = soutenanceService.getSoutenanceViewsByDate(date);
        return new ResponseEntity<>(soutenances, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSoutenance(@PathVariable Integer id, 
                                                 @RequestBody SoutenanceCreateDTO soutenanceDTO) {
        try {
            SoutenanceCreateDTO updatedSoutenance = soutenanceService.updateSoutenance(id, soutenanceDTO);
            return new ResponseEntity<>(updatedSoutenance, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", new Date().toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/jury/batch")
    public ResponseEntity<Void> addJuryMembers(@RequestBody List<SoutenanceCreateDTO.JuryMemberDTO> juryMembers) {
        try {
            soutenanceService.addJuryMembersBatch(juryMembers);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    // Get soutenances with project information
    @GetMapping("/with-projects")
    public ResponseEntity<List<SoutenanceCreateDTO>> getSoutenancesWithProjects() {
        List<SoutenanceCreateDTO> dtos = soutenanceService.getAllSoutenanceViews();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
    
    
    @GetMapping("/by-binome/{binomeId}")
    public ResponseEntity<?> getSoutenanceByBinomeId(@PathVariable Integer binomeId) {
        return soutenanceService.getSoutenanceViewByBinomeId(binomeId)
            .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}