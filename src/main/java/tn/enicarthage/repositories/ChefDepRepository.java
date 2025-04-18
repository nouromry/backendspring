package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.enicarthage.models.ChefDep;

import java.util.List;

@Repository
public interface ChefDepRepository extends JpaRepository<ChefDep, Integer> {
    List<ChefDep> findByDepartement(String departement);
}
