package ru.ssau.tk.phoenix.ooplabs.controllers;

import ru.ssau.tk.phoenix.ooplabs.dao.FunctionDao;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class FunctionController {
    private final FunctionDao functionDao;

    public FunctionController(FunctionDao functionDao) {
        this.functionDao = functionDao;
    }

    public FunctionResponse save(FunctionRequest function) throws SQLException {
        return functionDao.save(function);
    }

    public FunctionResponse find(Long id) throws SQLException {
        Optional<FunctionResponse> function = functionDao.findById(id);
        if (function.isPresent()) return function.get();
        throw new NoSuchElementException("Функция не найдена");
    }

    public List<FunctionResponse> findByUserId(Long userId) throws SQLException {
       return functionDao.findByUserId(userId);
    }

    public void delete(FunctionResponse function) throws SQLException {
        functionDao.delete(function.getId());
    }
}
