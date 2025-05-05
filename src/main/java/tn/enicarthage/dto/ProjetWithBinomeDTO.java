package tn.enicarthage.dto;

import tn.enicarthage.models.*;
import java.util.Date;

public class ProjetWithBinomeDTO {
    private Integer id;
    private String titre;
    private String description;
    private String technologies;
    private String etat;
    private Date dateDepot;
    private Date dateAffectation;
    private String filiere;
    private BinomeDTO binomeAffecte;
    private EnseignantDTO enseignant;

    public ProjetWithBinomeDTO(Projet projet) {
        this.id = projet.getId();
        this.titre = projet.getTitre();
        this.description = projet.getDescription();
        this.technologies = projet.getTechnologies();
        this.etat = projet.getEtat().name();
        this.dateDepot = projet.getDateDepot();
        this.dateAffectation = projet.getDateAffectation();
        this.filiere = projet.getFiliere();
        this.enseignant = new EnseignantDTO(projet.getEnseignant());
        
        if (projet.getBinomeAffecte() != null) {
            this.binomeAffecte = new BinomeDTO(projet.getBinomeAffecte());
        }
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Date getDateDepot() {
        return dateDepot;
    }

    public void setDateDepot(Date dateDepot) {
        this.dateDepot = dateDepot;
    }

    public Date getDateAffectation() {
        return dateAffectation;
    }

    public void setDateAffectation(Date dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public BinomeDTO getBinomeAffecte() {
        return binomeAffecte;
    }

    public void setBinomeAffecte(BinomeDTO binomeAffecte) {
        this.binomeAffecte = binomeAffecte;
    }

    public EnseignantDTO getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(EnseignantDTO enseignant) {
        this.enseignant = enseignant;
    }

    // Inner DTO classes
    public static class BinomeDTO {
        private Integer id;
        private EtudiantDTO etud1;
        private EtudiantDTO etud2;
        private Double moyenneBinome;

        public BinomeDTO(Binome binome) {
            this.id = binome.getId();
            this.etud1 = new EtudiantDTO(binome.getEtud1());
            this.etud2 = new EtudiantDTO(binome.getEtud2());
            this.moyenneBinome = binome.getMoyenneBinome() != null ? 
                binome.getMoyenneBinome().doubleValue() : null;
        }

        // Getters and setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public EtudiantDTO getEtud1() {
            return etud1;
        }

        public void setEtud1(EtudiantDTO etud1) {
            this.etud1 = etud1;
        }

        public EtudiantDTO getEtud2() {
            return etud2;
        }

        public void setEtud2(EtudiantDTO etud2) {
            this.etud2 = etud2;
        }

        public Double getMoyenneBinome() {
            return moyenneBinome;
        }

        public void setMoyenneBinome(Double moyenneBinome) {
            this.moyenneBinome = moyenneBinome;
        }
    }

    public static class EtudiantDTO {
        private Integer id;
        private String nom;
        private String prenom;
        private String matricule;
        private String filiere;
        private String groupe;
        private Double moyenneGeneral;

        public EtudiantDTO(Etudiant etudiant) {
            this.id = etudiant.getId();
            this.nom = etudiant.getNom();
            this.prenom = etudiant.getPrenom();
            this.matricule = etudiant.getMatricule();
            this.filiere = etudiant.getFiliere();
            this.groupe = etudiant.getGroupe();
            this.moyenneGeneral = etudiant.getMoyenneGeneral() != null ? 
                etudiant.getMoyenneGeneral().doubleValue() : null;
        }

        // Getters and setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getMatricule() {
            return matricule;
        }

        public void setMatricule(String matricule) {
            this.matricule = matricule;
        }

        public String getFiliere() {
            return filiere;
        }

        public void setFiliere(String filiere) {
            this.filiere = filiere;
        }

        public String getGroupe() {
            return groupe;
        }

        public void setGroupe(String groupe) {
            this.groupe = groupe;
        }

        public Double getMoyenneGeneral() {
            return moyenneGeneral;
        }

        public void setMoyenneGeneral(Double moyenneGeneral) {
            this.moyenneGeneral = moyenneGeneral;
        }
    }

    public static class EnseignantDTO {
        private Integer id;
        private String nom;
        private String prenom;
        private String specialite;

        public EnseignantDTO(Enseignant enseignant) {
            this.id = enseignant.getId();
            this.nom = enseignant.getNom();
            this.prenom = enseignant.getPrenom();
            this.specialite = enseignant.getSpecialite();
        }

        // Getters and setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getSpecialite() {
            return specialite;
        }

        public void setSpecialite(String specialite) {
            this.specialite = specialite;
        }
    }

    @Override
    public String toString() {
        return "ProjetWithBinomeDTO{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", technologies='" + technologies + '\'' +
                ", etat='" + etat + '\'' +
                ", dateDepot=" + dateDepot +
                ", dateAffectation=" + dateAffectation +
                ", filiere='" + filiere + '\'' +
                ", binomeAffecte=" + binomeAffecte +
                ", enseignant=" + enseignant +
                '}';
    }
}