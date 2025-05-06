package tn.enicarthage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tn.enicarthage.models.Binome;
import tn.enicarthage.models.Document;
import tn.enicarthage.models.Etudiant;
import tn.enicarthage.models.Projet;
import tn.enicarthage.repositories.DocumentRepository;
import tn.enicarthage.repositories.EtudiantRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentService {
    
    private final DocumentRepository documentRepository;
    
    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }
  
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @Value("${app.document.upload-dir:uploads/documents}")
    private String uploadDir;
    
    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }
    
    public Optional<Document> findDocumentById(Integer id) {
        return documentRepository.findById(id);
    }
    
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }
    
    public Document updateDocument(Integer id, Document document) {
        if (documentRepository.existsById(id)) {
            document.setId(id);
            return documentRepository.save(document);
        }
        return null;
    }
    
    public void deleteDocument(Integer id) {
        documentRepository.deleteById(id);
    }
    
    public List<Document> findDocumentsByType(Document.Type type) {
        return documentRepository.findByType(type);
    }
    
    public List<Document> findDocumentsByBinomeId(Integer binomeId) {
        return documentRepository.findByBinomeId(binomeId);
    }
    
    public List<Document> findDocumentsByProjetId(Integer projetId) {
        return documentRepository.findByProjetId(projetId);
    }
    
   
    
    /**
     * Get all documents associated with a student's binome
     */
    public List<Document> getDocumentsByEtudiantId(Integer etudiantId) {
        Optional<Binome> binome = etudiantRepository.findBinomeByEtudiantId(etudiantId);
        if (!binome.isPresent()) {
            throw new RuntimeException("Etudiant is not part of a binome");
        }
        return documentRepository.findByBinomeId(binome.get().getId());
    }
    
    /**
     * Upload a new document for a student's binome and project
     */
    public Document uploadDocument(Integer etudiantId, Integer projetId, 
                                  String titre, Document.Type type, 
                                  MultipartFile file) throws IOException {
        
        // Verify the student exists and is part of a binome
        Optional<Etudiant> etudiantOpt = etudiantRepository.findById(etudiantId);
        if (!etudiantOpt.isPresent()) {
            throw new RuntimeException("Etudiant not found");
        }
        
        Etudiant etudiant = etudiantOpt.get();
        Binome binome = etudiant.getBinome();
        if (binome == null) {
            throw new RuntimeException("Etudiant is not part of a binome");
        }
        
        // Verify the project is assigned to the binome
        Projet projet = binome.getProjetAffecte();
        if (projet == null || !projet.getId().equals(projetId)) {
            throw new RuntimeException("Project is not assigned to this binome");
        }
        
        // Create document directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate a unique filename
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String fileName = UUID.randomUUID().toString() + fileExtension;
        Path filePath = uploadPath.resolve(fileName);
        
        // Save the file
        Files.copy(file.getInputStream(), filePath);
        
        // Create and save document entity
        Document document = new Document();
        document.setTitre(titre);
        document.setType(type);
        document.setDateDepot(new Date());
        document.setCheminFichier(fileName);
        document.setBinome(binome);
        document.setProjet(projet);
        
        return documentRepository.save(document);
    }
    
    /**
     * Get document by id
     */
    public Document getDocumentById(Integer documentId) {
        return documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document not found"));
    }
    
    /**
     * Delete document by id (check if student is authorized)
     */
    public void deleteDocument(Integer documentId, Integer etudiantId) {
        Document document = getDocumentById(documentId);
        
        // Check if the student is part of the binome that owns this document
        Optional<Binome> binome = etudiantRepository.findBinomeByEtudiantId(etudiantId);
        if (!binome.isPresent() || !document.getBinome().getId().equals(binome.get().getId())) {
            throw new RuntimeException("Not authorized to delete this document");
        }
        
        // Delete the file
        try {
            Path filePath = Paths.get(uploadDir).resolve(document.getCheminFichier());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log the error but continue to delete the database record
            System.err.println("Failed to delete file: " + e.getMessage());
        }
        
        documentRepository.delete(document);
    }
    
    /**
     * Check if a student is authorized to access a document
     */
    public boolean isAuthorizedToAccessDocument(Integer documentId, Integer etudiantId) {
        Document document = getDocumentById(documentId);
        Optional<Binome> binome = etudiantRepository.findBinomeByEtudiantId(etudiantId);
        return binome.isPresent() && document.getBinome().getId().equals(binome.get().getId());
    }
}