package ru.ssau.tk.phoenix.ooplabs.service;

import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;

import java.sql.SQLException;
import java.util.List;

public interface FunctionApiContract {
    FunctionResponse save(FunctionRequest function) throws SQLException;
    FunctionResponse find(Long id) throws SQLException;
    List<FunctionResponse> findByUserId(Long userId) throws SQLException;
    List<FunctionResponse> findWithFilter(List<Criteria> filter) throws SQLException;
    FunctionResponse update(FunctionResponse function) throws SQLException;
    void delete(Long id) throws SQLException;
}