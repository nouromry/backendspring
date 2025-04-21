package tn.enicarthage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.models.Document;
import tn.enicarthage.services.DocumentService;

import java.util.List;
import java.util.Optional;

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
}