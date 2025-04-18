package tn.enicarthage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.enicarthage.models.User;
import tn.enicarthage.models.User.Role;

import java.util.List;
import java.util.Optional;
@Repository

public interface UserRepository extends JpaRepository<User, Integer> {
	 Optional<User> findByEmail(String email);
	 List<User> findByRole(Role role);
    boolean existsByEmail(String email);
}
