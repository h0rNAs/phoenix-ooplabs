package ru.ssau.tk.phoenix.ooplabs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;

import java.util.List;

public interface FunctionRepository extends JpaRepository<Function, Long> {
    List<Function>findByUserId(Long userId);
    List<Function>findByType(Long type);
    List<Function> findByName(String name);


}
