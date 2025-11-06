package ru.ssau.tk.phoenix.ooplabs.service;

import ru.ssau.tk.phoenix.ooplabs.dao.Function;
import ru.ssau.tk.phoenix.ooplabs.dao.FunctionDaoImpl;

import java.sql.Connection;
import java.util.Optional;

public class FunctionService {
    private final Connection conn;
    private final FunctionDaoImpl functionDao;

    public FunctionService(Connection connection) {
        conn = connection;
        this.functionDao = new FunctionDaoImpl(connection);
    }

    public Optional<Function> findById(Long id){
        return functionDao.findById(id);
    }
}
