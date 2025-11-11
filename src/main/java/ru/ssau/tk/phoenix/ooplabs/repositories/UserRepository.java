package ru.ssau.tk.phoenix.ooplabs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ssau.tk.phoenix.ooplabs.entities.User;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "select * from users where username = :username", nativeQuery = true)
    Optional<User> findByUsername(String username);
}
