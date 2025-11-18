package ru.ssau.tk.phoenix.ooplabs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;

import java.util.List;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long>, JpaSpecificationExecutor<Function> {
    /*List<Function> findByUserId(Long userId);

    List<Function> findByUserIdOrderByNameAsc(Long userId);

    List<Function> findByUserIdOrderByNameDesc(Long userId);

    List<Function> findByUserIdOrderByTypeAsc(Long userId);

    List<Function> findByUserIdOrderByTypeDesc(Long userId);

    List<Function> findByUserIdAndTypeOrderByNameAscTypeAsc(Long userId, String type);

    List<Function> findByUserIdAndTypeOrderByNameDescTypeDesc(Long userId, String type);

    List<Function> findByUserIdAndTypeInOrderByNameAscTypeAsc(Long userId, List<String> types);

    List<Function> findByUserIdAndTypeInOrderByNameDescTypeDesc(Long userId, List<String> types);*/
}