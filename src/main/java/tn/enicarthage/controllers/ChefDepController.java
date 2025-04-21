package tn.enicarthage.controllers;

import tn.enicarthage.models.ChefDep;
import tn.enicarthage.services.ChefDepService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chefdeps")
 
public class ChefDepController {
    private final ChefDepService chefDepService;

    public ChefDepController(ChefDepService chefDepService) {
        this.chefDepService = chefDepService;
    }

    @PostMapping
    public ChefDep createChefDep(@RequestBody ChefDep chefDep) {
        return chefDepService.createChefDep(chefDep);
    }
}