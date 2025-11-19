package ru.ssau.tk.phoenix.ooplabs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;

import java.util.List;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long>, JpaSpecificationExecutor<Function> {
    List<FunctionResponse> findByUserId(Long userId);
}