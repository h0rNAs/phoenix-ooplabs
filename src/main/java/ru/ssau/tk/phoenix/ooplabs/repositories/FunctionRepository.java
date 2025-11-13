package ru.ssau.tk.phoenix.ooplabs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;

import java.util.List;
@Repository
public interface FunctionRepository extends JpaRepository<Function, Long> {
    List<Function> findByUserId(Long userId);
    List<Function> findByName(String name);
    List<Function> findByType(String type);
    List<Function> findByNameAndType(String name, String type);

}