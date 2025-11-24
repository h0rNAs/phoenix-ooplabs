package ru.ssau.tk.phoenix.ooplabs.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.service.FunctionService;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "mathhub/api/functions")
public class FunctionController {
    private final Logger logger;
    private final FunctionService functionService;

    public FunctionController(FunctionService functionService) {
        this.functionService = functionService;
        this.logger = LogManager.getLogger();
    }

    @PostMapping
    public FunctionResponse create(@RequestBody FunctionRequest function){
        return functionService.save(function);
    }

    @GetMapping(value = {"{id}", ""})
    public List<FunctionResponse> find(
            @PathVariable(required = false) Long id,
            @RequestParam(required = false) Long userId,
            @RequestBody(required = false) List<Criteria> filter
    ){
        logger.info("Поиск функции id={}, userId={}, filter.size={}", id, userId,
                filter == null ? 0 : filter.size());

        if (id != null) return List.of(functionService.find(id));
        else if (userId != null) return functionService.findByUserId(userId);
        else if (filter != null && !filter.isEmpty()) return functionService.findWithFilter(filter);

        logger.error("Неверные критерии для поиска");
        throw new IllegalArgumentException("Неверные критерии для поиска");
    }

    @PutMapping
    public FunctionResponse update(@RequestBody FunctionResponse function){
        logger.info("Обновление функции");
        return functionService.update(function);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        functionService.delete(id);
    }
}
