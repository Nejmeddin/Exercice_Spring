package tn.enis.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enis.app.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
