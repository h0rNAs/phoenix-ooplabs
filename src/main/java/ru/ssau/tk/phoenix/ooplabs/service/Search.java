package ru.ssau.tk.phoenix.ooplabs.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.repositories.FunctionRepository;

import java.util.List;

@Service
public class Search {
    private final static Logger logger = LogManager.getLogger();
    @Autowired
    private FunctionRepository functionRepository;


    public List<Function> searchByUserIdSortedByNameAsc (Long userId){
        logger.info("Поиск по по пользавтелю с ID: " + userId);
        List<Function> result = functionRepository.findByUserIdOrderByNameAsc(userId);
        logger.info("Найдено " + result.size() + " функций пользователя с ID: " + userId);
        return result;
    }


}
