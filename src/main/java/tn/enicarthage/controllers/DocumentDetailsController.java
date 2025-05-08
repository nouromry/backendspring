package tn.enicarthage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.dto.DocumentDetailsDTO;
import tn.enicarthage.services.DocumentDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.dto.DocumentDetailsDTO;
import tn.enicarthage.models.Document;
import tn.enicarthage.services.DocumentService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentDetailsController {

    private final DocumentDetailsService documentService;
    
    @GetMapping("/{id}/details")
    public ResponseEntity<DocumentDetailsDTO> getDocumentDetails(@PathVariable Integer id) {
        DocumentDetailsDTO documentDetails = documentService.getDocumentDetails(id);
        return ResponseEntity.ok(documentDetails);
    }
    
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Integer id) {
        try {
            Document document = documentService.getDocumentById(id);
            
            Path filePath = Paths.get(document.getCheminFichier());
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = "application/pdf";
                
                String originalFilename = filePath.getFileName().toString();
                
                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFilename + "\"")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}