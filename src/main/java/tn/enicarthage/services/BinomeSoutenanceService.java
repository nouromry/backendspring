

	package tn.enicarthage.services;

	import tn.enicarthage.models.Binome;
	import tn.enicarthage.models.Etudiant;
	import tn.enicarthage.repositories.BinomeSoutenanceRepository;
	import tn.enicarthage.repositories.EtudiantRepository;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;

	import java.math.BigDecimal;
	import java.util.List;
	import java.util.NoSuchElementException;

	@Service
	public class BinomeSoutenanceService {

	    private final BinomeSoutenanceRepository binomeRepository;
	    private final EtudiantRepository etudiantRepository;

	    @Autowired
	    public BinomeSoutenanceService(BinomeSoutenanceRepository binomeRepository, EtudiantRepository etudiantRepository) {
	        this.binomeRepository = binomeRepository;
	        this.etudiantRepository = etudiantRepository;
	    }

	    // Get all binomes
	    public List<Binome> getAllBinomes() {
	        return binomeRepository.findAll();
	    }

	    // Get binome by ID
	    public Binome getBinomeById(Integer id) {
	        return binomeRepository.findById(id)
	                .orElseThrow(() -> new NoSuchElementException("Binome not found with id: " + id));
	    }

	    // Create new binome
	    @Transactional
	    public Binome createBinome(Binome binome) {
	        Etudiant etud1 = etudiantRepository.findById(binome.getEtud1().getId())
	                .orElseThrow(() -> new NoSuchElementException("Etudiant 1 not found"));

	        Etudiant etud2 = etudiantRepository.findById(binome.getEtud2().getId())
	                .orElseThrow(() -> new NoSuchElementException("Etudiant 2 not found"));

	        if (etud1.getBinome() != null) {
	            throw new IllegalStateException("Etudiant 1 is already in a binome");
	        }

	        if (etud2.getBinome() != null) {
	            throw new IllegalStateException("Etudiant 2 is already in a binome");
	        }

	        binome.setEtud1(etud1);
	        binome.setEtud2(etud2);

	        return binomeRepository.save(binome);
	    }

	    // Update binome
	    @Transactional
	    public Binome updateBinome(Integer id, Binome binomeDetails) {
	        Binome binome = getBinomeById(id);

	        if (binomeDetails.getEtud1() != null) {
	            Etudiant etud1 = etudiantRepository.findById(binomeDetails.getEtud1().getId())
	                    .orElseThrow(() -> new NoSuchElementException("Etudiant 1 not found"));
	            binome.setEtud1(etud1);
	        }

	        if (binomeDetails.getEtud2() != null) {
	            Etudiant etud2 = etudiantRepository.findById(binomeDetails.getEtud2().getId())
	                    .orElseThrow(() -> new NoSuchElementException("Etudiant 2 not found"));
	            binome.setEtud2(etud2);
	        }

	        if (binomeDetails.getMoyenneBinome() != null) {
	            binome.setMoyenneBinome(binomeDetails.getMoyenneBinome());
	        }

	        return binomeRepository.save(binome);
	    }

	    // Delete binome
	    @Transactional
	    public void deleteBinome(Integer id) {
	        Binome binome = getBinomeById(id);
	        binomeRepository.delete(binome);
	    }

	    // Get binomes without soutenances
	    public List<Binome> getBinomesWithoutSoutenance() {
	        return binomeRepository.findBinomesWithoutSoutenance();
	    }

	    // Update binome average
	    @Transactional
	    public Binome updateBinomeAverage(Integer id, BigDecimal average) {
	        Binome binome = getBinomeById(id);
	        binome.setMoyenneBinome(average);
	        return binomeRepository.save(binome);
	    }

	    // Find binomes by student
	    public List<Binome> findBinomesByEtudiant(Integer etudiantId) {
	        return binomeRepository.findByEtudiant(etudiantId);
	    }

	    // Find binomes by average grade range
	    public List<Binome> findBinomesByAverageRange(BigDecimal min, BigDecimal max) {
	        return binomeRepository.findByAverageGradeRange(min, max);
	    }
	}