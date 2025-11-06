package ru.ssau.tk.phoenix.ooplabs.dao;

import ru.ssau.tk.phoenix.ooplabs.service.FunctionService;

import java.util.Optional;

public interface FunctionDao {
    Optional<Function> findById(Long id);
    Optional<Function> findByUserId(Long userId);
    Function save(Function func);
    Function update(Function func);
    void delete(Long id);
}
