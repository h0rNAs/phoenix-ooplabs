package ru.ssau.tk.phoenix.ooplabs.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.dao.FunctionDao;
import ru.ssau.tk.phoenix.ooplabs.dao.User;
import ru.ssau.tk.phoenix.ooplabs.dao.UserDao;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class FunctionService implements FunctionApiContract {
    private final Logger logger = LogManager.getLogger(FunctionApiContract.class);
    private final FunctionDao functionDao;
    private final UserDao userDao;

    public FunctionService(FunctionDao functionDao, UserDao userDao) {
        this.functionDao = functionDao;
        this.userDao = userDao;
    }


    @Override
    public FunctionResponse save(FunctionRequest function) throws SQLException {
        FunctionResponse func = functionDao.save(function);
        logger.info("Функция id={} добавлена", func.getId());
        return func;
    }

    @Override
    public FunctionResponse find(Long id) throws SQLException {
        Optional<FunctionResponse> optionalFunction = functionDao.findById(id);
        if (optionalFunction.isEmpty())
            throw new NoSuchElementException("Функция с id= " + id + " не найдена");

        logger.info("Считанная функция: {}", optionalFunction.get());
        return optionalFunction.get();
    }

    @Override
    public List<FunctionResponse> findByUserId(Long userId) throws SQLException {
        Optional<UserResponse> optionalFunction = userDao.findById(userId);
        if (optionalFunction.isEmpty())
            throw new NoSuchElementException("Пользователь с id=" + userId + " не найден");

        List<FunctionResponse> functions = functionDao.findByUserId(userId);
        logger.info("По запросу было считанно {} функций", functions.size());
        return functions;
    }

    @Override
    public List<FunctionResponse> findWithFilter(List<Criteria> filter) throws SQLException {
        List<FunctionResponse> functionResponses = functionDao.findWithFilter(filter);
        logger.info("По запросу было считанно {} функций", functionResponses.size());
        return functionResponses;
    }

    @Override
    public FunctionResponse update(FunctionResponse function) throws SQLException {
        Optional<FunctionResponse> optionalFunction = functionDao.findById(function.getId());
        if (optionalFunction.isEmpty())
            throw new NoSuchElementException("Функция с id= " + function.getId() + " не найдена");

        FunctionResponse newFunction = functionDao.update(function);
        logger.info("Функция id={} обновлена", optionalFunction.get().getId());
        return newFunction;
    }

    @Override
    public void delete(Long id) throws SQLException {
        Optional<FunctionResponse> optionalFunction = functionDao.findById(id);
        if (optionalFunction.isEmpty())
            throw new NoSuchElementException("Функция с id=" + id + " не найдена");

        functionDao.delete(id);
        logger.info("Функция id={} удалена", id);
    }
}
