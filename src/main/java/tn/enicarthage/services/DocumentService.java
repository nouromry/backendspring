package tn.enicarthage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.enicarthage.models.Document;
import tn.enicarthage.repositories.DocumentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {
    
    private final DocumentRepository documentRepository;
    
    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }
    
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
}