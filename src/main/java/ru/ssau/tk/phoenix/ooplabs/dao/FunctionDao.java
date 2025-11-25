package ru.ssau.tk.phoenix.ooplabs.dao;

import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface FunctionDao {
    Optional<FunctionResponse> findById(Long id) throws SQLException;
    List<FunctionResponse> findByUserId(Long userId) throws SQLException;

    /**
     * Функция для поиска функций по user id с дополнительной фильтрацией
     * @param filter критерии
     * @return
     * @throws SQLException
     */
    List<FunctionResponse> findWithFilter(List<Criteria> filter) throws SQLException;

    FunctionResponse save(FunctionRequest func) throws SQLException;
    FunctionResponse update(FunctionResponse func) throws SQLException;
    void delete(Long id) throws SQLException;
}
