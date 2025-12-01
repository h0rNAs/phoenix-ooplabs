package ru.ssau.tk.phoenix.ooplabs.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.entities.User;
import ru.ssau.tk.phoenix.ooplabs.repositories.FunctionRepository;
import ru.ssau.tk.phoenix.ooplabs.repositories.FunctionSpecifications;
import ru.ssau.tk.phoenix.ooplabs.repositories.UserRepository;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FunctionService implements FunctionApiContract{
    private final Logger logger = LogManager.getLogger(FunctionApiContract.class);
    private FunctionRepository functionRepository;
    private UserRepository userRepository;

    public FunctionService(FunctionRepository functionRepository, UserRepository userRepository) {
        this.functionRepository = functionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FunctionResponse save(FunctionRequest function) {
        Function func = functionRepository.save(mapRequestToEntity(function));
        logger.info("Функция id=" + func.getId() + " добавлена");
        return mapEntityToResponse(func);
    }

    @Override
    public FunctionResponse find(Long id) {
        Optional<Function> optionalFunction = functionRepository.findById(id);
        if (optionalFunction.isEmpty())
            throw new NoSuchElementException("Функция с id=" + id + " не найдена");

        logger.info("Считанная функция: " + optionalFunction.get());
        return mapEntityToResponse(optionalFunction.get());
    }

    @Override
    public List<FunctionResponse> findByUserId(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        logger.info("Пользователь  {}", optionalUser.isPresent());
        if (optionalUser.isEmpty())
            throw new NoSuchElementException("Пользователь с id=" + userId + " не найден. Отменено создание функции");

        List<FunctionResponse> functions = new ArrayList<>();
        for (Function func : functionRepository.findByUserId(userId)){
            functions.add(mapEntityToResponse(func));
        }

        logger.info("У пользователя id={} найдено {}", userId, functions.size());
        return functions;
    }

    @Override
    public List<FunctionResponse> findWithFilter(List<Criteria> filter) {
        Specification<Function> spec = FunctionSpecifications.buildSpecification(filter);
        Sort sort = FunctionSpecifications.buildSort(filter);

        List<FunctionResponse> functionResponses = new ArrayList<>();
        for (Function func : functionRepository.findAll(spec, sort)){
            functionResponses.add(mapEntityToResponse(func));
        }
        logger.info("По запросу было считанно " + functionResponses.size() + " функций");
        return functionResponses;
    }

    @Override
    public FunctionResponse update(FunctionResponse function) {
        Optional<Function> optionalFunction = functionRepository.findById(function.getId());
        if (optionalFunction.isEmpty())
            throw new NoSuchElementException("Функция с id=" + function.getId() + " не найдена");

        FunctionResponse func = mapEntityToResponse(functionRepository.save(mapResponseToEntity(function)));
        logger.info("Функция id=" + func.getId() + " обновлена");
        return func;
    }

    @Override
    public void delete(Long id) {
        Optional<Function> optionalFunction = functionRepository.findById(id);
        if (optionalFunction.isEmpty())
            throw new NoSuchElementException("Функция с id=" + id + " не найдена");

        functionRepository.delete(optionalFunction.get());
        logger.info("Функция id=" + id + " удалена");
    }

    public static Function mapResponseToEntity(FunctionResponse response){
        return new Function(response.getId(), response.getUserId(), response.getName(),
                response.getType().toString(), response.getDefinition());
    }

    public static Function mapRequestToEntity(FunctionRequest request) {
        return new Function(request.getUserId(), request.getName(), request.getType().toString(), request.getDefinition());
    }

    public static FunctionResponse mapEntityToResponse(Function entity){
        return new FunctionResponse(entity.getId(), entity.getUserId(),
                entity.getName(), entity.getType(), entity.getDefinition());
    }
}
