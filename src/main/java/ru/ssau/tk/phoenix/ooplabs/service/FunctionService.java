/*
package ru.ssau.tk.phoenix.ooplabs.service;

import ru.ssau.tk.phoenix.ooplabs.dao.Function;
import ru.ssau.tk.phoenix.ooplabs.dao.FunctionDaoImpl;

import java.sql.Connection;
import java.util.List;
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

    public List<Function> findByUserID(Long userId){
        return functionDao.findByUserId(userId);
    }

    public Function create(Function function){
        return functionDao.save(function);
    }

    public Function update(Function function){
        Optional<Function> optionalFunc = functionDao.findById(function.getId());
        if (optionalFunc.isEmpty()){
            throw new IllegalStateException("Функции с таким id не существует");
        }
        return functionDao.update(function);
    }

    public void delete(Long id){
        Optional<Function> optionalFunc = functionDao.findById(id);
        if (optionalFunc.isEmpty()){
            throw new IllegalStateException("Функции с таким id не существует");
        }
        functionDao.delete(id);
    }
}
*/
