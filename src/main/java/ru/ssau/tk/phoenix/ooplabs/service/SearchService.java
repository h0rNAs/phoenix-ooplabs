package ru.ssau.tk.phoenix.ooplabs.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.repositories.FunctionRepository;

import java.util.List;

@Service
public class SearchService {
    private final static Logger logger = LogManager.getLogger(SearchService.class);
    @Autowired
    private FunctionRepository functionRepository;

    public List<Function> findFunctionByIdAndOrderByNameAsc(Long userId) {
        logger.info("Поиск с сортировкой по имени и пользователю с ID:" + userId + "(Возрастание).");
        List<Function> result = functionRepository.findByUserIdOrderByNameAsc(userId);
        logger.info("Найдено " + result.size() + " функций пользователя с ID:" + userId + ".");
        return result;
    }

    public List<Function> findFunctionByIdAndOrderByNameDesc(Long userId) {
        logger.info("Поиск с сортировкой по имени и пользователю с ID:" + userId + "(Убывание).");
        List<Function> result = functionRepository.findByUserIdOrderByNameDesc(userId);
        logger.info("Найдено " + result.size() + " функций пользователя с ID:" + userId + ".");
        return result;
    }

    public List<Function> findFunctionByIdAndOrderByTypeAsc(Long userId) {
        logger.info("Поиск с сортировкой по типу и пользователю с ID:" + userId + "(Возрастание).");
        List<Function> result = functionRepository.findByUserIdOrderByTypeAsc(userId);
        logger.info("Найдено " + result.size() + " функций пользователя с ID:" + userId + ".");
        return result;
    }

    public List<Function> findFunctionByIdAndOrderByTypeDesc(Long userId) {
        logger.info("Поиск с сортировкой по типу и пользователю с ID:" + userId + "(Убывание).");
        List<Function> result = functionRepository.findByUserIdOrderByTypeDesc(userId);
        logger.info("Найдено " + result.size() + " функций пользователя с ID:" + userId + ".");
        return result;
    }

    public List<Function> findFunctionByIdAndOrderByNameAndTypeAsc(Long userId, String type) {
        logger.info("Поиск по пользователю с ID:" + userId + ", отсортированный по имени и типу(Возрастание).");
        List<Function> result = functionRepository.findByUserIdAndTypeOrderByNameAscTypeAsc(userId, type);
        logger.info("Найдено " + result.size() + " функций пользователя с ID:" + userId + " и типом:" + type + ".");
        return result;
    }

    public List<Function> findFunctionByIdAndOrderByNameAndTypeDesc(Long userId, String type) {
        logger.info("Поиск по пользователю с ID:" + userId + ", отсортированный по имени и типу(Убывание).");
        List<Function> result = functionRepository.findByUserIdAndTypeOrderByNameDescTypeDesc(userId, type);
        logger.info("Найдено " + result.size() + " функций пользователя с ID:" + userId + " и типом:" + type + ".");
        return result;
    }

    public List<Function> findFunctionByIdAndOrderByNameAndTypeInAsc(Long userId, List<String> types) {
        logger.info("Поиск по пользователю с ID:" + userId + ", отсортированный по имени и типам:" + types + "(Возрастание).");
        List<Function> result = functionRepository.findByUserIdAndTypeInOrderByNameAscTypeAsc(userId, types);
        logger.info("Найдено " + result.size() + " функций пользователя с ID:" + userId + " и типами:" + types + ".");
        return result;
    }

    public List<Function> findFunctionByIdAndOrderByNameAndTypeInDesc(Long userId, List<String> types) {
        logger.info("Поиск по пользователю с ID:" + userId + ", отсортированный по имени и типам:" + types + "(Убывание).");
        List<Function> result = functionRepository.findByUserIdAndTypeInOrderByNameDescTypeDesc(userId, types);
        logger.info("Найдено " + result.size() + " функций пользователя с ID:" + userId + " и типами:" + types + ".");
        return result;
    }
}