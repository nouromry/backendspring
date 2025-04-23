package tn.enicarthage.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.dto.DocumentDetailsDTO;
import tn.enicarthage.exceptions.ResourceNotFoundException;
import tn.enicarthage.models.Document;
import tn.enicarthage.repositories.DocumentDetailsRepository;

@Service
@RequiredArgsConstructor
public class DocumentDetailsService {

    private final DocumentDetailsRepository documentRepository;
    
    public DocumentDetailsDTO getDocumentDetails(Integer documentId) {
        Document document = documentRepository.findDocumentWithDetails(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));
        
        return mapToDocumentDetailsDTO(document);
    }
    // Add this method to get document by ID
    public Document getDocumentById(Integer documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));
    }
    
    
    private DocumentDetailsDTO mapToDocumentDetailsDTO(Document document) {
        DocumentDetailsDTO dto = new DocumentDetailsDTO();
        
        // Document details
        dto.setId(document.getId());
        dto.setTitre(document.getTitre());
        dto.setType(document.getType());
        dto.setDateDepot(document.getDateDepot());
        dto.setCheminFichier(document.getCheminFichier());
        
        // Binome details
        dto.setBinomeId(document.getBinome().getId());
        dto.setEtudiant1Nom(document.getBinome().getEtud1().getNom());
        dto.setEtudiant1Prenom(document.getBinome().getEtud1().getPrenom());
        dto.setEtudiant1Filiere(document.getBinome().getEtud1().getFiliere());
        dto.setEtudiant2Nom(document.getBinome().getEtud2().getNom());
        dto.setEtudiant2Prenom(document.getBinome().getEtud2().getPrenom());
        dto.setEtudiant2Filiere(document.getBinome().getEtud2().getFiliere());
        
        // Project details
        dto.setProjetId(document.getProjet().getId());
        dto.setProjetTitre(document.getProjet().getTitre());
        
        // Enseignant details
        dto.setEnseignantId(document.getProjet().getEnseignant().getId());
        dto.setEnseignantNom(document.getProjet().getEnseignant().getNom());
        dto.setEnseignantPrenom(document.getProjet().getEnseignant().getPrenom());
        
        return dto;
    }
}