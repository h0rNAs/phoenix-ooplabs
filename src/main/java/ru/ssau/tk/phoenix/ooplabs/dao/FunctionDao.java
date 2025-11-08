package ru.ssau.tk.phoenix.ooplabs.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface FunctionDao {
    Optional<Function> findById(Long id) throws SQLException;
    List<Function> findByUserId(Long userId) throws SQLException;
    Function save(Function func) throws SQLException;
    Function update(Function func) throws SQLException;
    void delete(Long id) throws SQLException;
}
