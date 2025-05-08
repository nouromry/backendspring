package tn.enicarthage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import tn.enicarthage.models.Document;
import tn.enicarthage.services.DocumentService;


import java.io.IOException;
import java.net.MalformedURLException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
@RestController
@RequestMapping("/api/documents")
// Allow requests from any origin, you might want to restrict this in production
public class DocumentController {
    
    private final DocumentService documentService;
    
    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.findAllDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Integer id) {
        Optional<Document> document = documentService.findDocumentById(id);
        return document.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        Document savedDocument = documentService.saveDocument(document);
        return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Integer id, @RequestBody Document document) {
        Document updatedDocument = documentService.updateDocument(id, document);
        if (updatedDocument != null) {
            return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Integer id) {
        documentService.deleteDocument(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Document>> getDocumentsByType(@PathVariable String type) {
        try {
            Document.Type documentType = Document.Type.valueOf(type);
            List<Document> documents = documentService.findDocumentsByType(documentType);
            return new ResponseEntity<>(documents, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/binome/{binomeId}")
    public ResponseEntity<List<Document>> getDocumentsByBinomeId(@PathVariable Integer binomeId) {
        List<Document> documents = documentService.findDocumentsByBinomeId(binomeId);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }
    
    @GetMapping("/projet/{projetId}")
    public ResponseEntity<List<Document>> getDocumentsByProjetId(@PathVariable Integer projetId) {
        List<Document> documents = documentService.findDocumentsByProjetId(projetId);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }
    
    
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Document>> getDocumentsByEtudiantId(@PathVariable Integer etudiantId) {
        try {
            List<Document> documents = documentService.getDocumentsByEtudiantId(etudiantId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Upload a new document
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("etudiantId") Integer etudiantId,
            @RequestParam("projetId") Integer projetId,
            @RequestParam("titre") String titre,
            @RequestParam("type") Document.Type type,
            @RequestParam("file") MultipartFile file) {
        
        try {
            Document document = documentService.uploadDocument(etudiantId, projetId, titre, type, file);
            return ResponseEntity.ok(document);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload document: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
    

    @GetMapping("/download/{documentId}/{etudiantId}")
    public ResponseEntity<?> downloadDocument(@PathVariable Integer documentId, @PathVariable Integer etudiantId) {
        try {
            if (!documentService.isAuthorizedToAccessDocument(documentId, etudiantId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to access this document");
            }

            Document document = documentService.getDocumentById(documentId);
            Path filePath = Paths.get("uploads/documents", document.getCheminFichier());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + document.getTitre() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found or cannot be read");
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing file: " + e.getMessage());
        }
    }

    @DeleteMapping("/{documentId}/{etudiantId}")
    public ResponseEntity<?> deleteDocument(@PathVariable Integer documentId, 
                                            @PathVariable Integer etudiantId) {
        try {
            documentService.deleteDocument(documentId, etudiantId);
            return ResponseEntity.ok().body("Document deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}