package ru.ssau.tk.phoenix.ooplabs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.phoenix.ooplabs.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
