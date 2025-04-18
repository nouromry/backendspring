package tn.enicarthage.services;

import tn.enicarthage.models.ChefDep;
import tn.enicarthage.repositories.ChefDepRepository;
import org.springframework.stereotype.Service;

@Service
public class ChefDepService {
    private final ChefDepRepository chefDepRepository;

    public ChefDepService(ChefDepRepository chefDepRepository) {
        this.chefDepRepository = chefDepRepository;
    }

    public ChefDep createChefDep(ChefDep chefDep) {
        return chefDepRepository.save(chefDep);
    }
}