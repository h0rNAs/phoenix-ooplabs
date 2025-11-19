package ru.ssau.tk.phoenix.ooplabs.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.dao.FunctionDao;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class FunctionService implements FunctionApiContract {
    private final Logger logger = LogManager.getLogger(FunctionApiContract.class);
    private final FunctionDao functionDao;

    public FunctionService(FunctionDao functionDao) {
        this.functionDao = functionDao;
    }


    @Override
    public FunctionResponse save(FunctionRequest function) throws SQLException {
        FunctionResponse func = functionDao.save(function);
        logger.info("Функция id=" + func.getId() + " добавлена");
        return func;
    }

    @Override
    public FunctionResponse find(Long id) throws SQLException {
        Optional<FunctionResponse> optionalFunction = functionDao.findById(id);
        if (optionalFunction.isEmpty())
            throw new NoSuchElementException("Функция с id= " + id + " не найдена");

        logger.info("Считанная функция: " + optionalFunction.get());
        return optionalFunction.get();
    }

    public List<FunctionResponse> findByUserId(Long userId) throws SQLException {
        List<Criteria> filter = List.of(new Criteria("user_id", new Object[]{userId}, null));
        return findWithFilter(filter);
    }

    @Override
    public List<FunctionResponse> findWithFilter(List<Criteria> filter) throws SQLException {
        List<FunctionResponse> functionResponses = functionDao.findWithFilter(filter);
        logger.info("По запросу было считанно " + functionResponses.size() + " функций");
        return functionResponses;
    }

    @Override
    public void update(FunctionResponse function) throws SQLException {
        Optional<FunctionResponse> optionalFunction = functionDao.findById(function.getId());
        if (optionalFunction.isEmpty())
            throw new NoSuchElementException("Функция с id= " + function.getId() + " не найдена");

        functionDao.update(function);
        logger.info("Функция id=" + optionalFunction.get().getId() + " обновлена");
    }

    @Override
    public void delete(Long id) throws SQLException {
        Optional<FunctionResponse> optionalFunction = functionDao.findById(id);
        if (optionalFunction.isEmpty())
            throw new NoSuchElementException("Функция с id= " + id + " не найдена");

        functionDao.delete(id);
        logger.info("Функция id=" + id + " удалена");
    }
}
