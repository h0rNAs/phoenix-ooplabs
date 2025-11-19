package ru.ssau.tk.phoenix.ooplabs.service;

import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;

import java.util.List;

public interface FunctionApiContract {
    FunctionResponse save(FunctionRequest function);
    FunctionResponse find(Long id);
    List<FunctionResponse> findByUserId(Long userId);
    List<FunctionResponse> findWithFilter(List<Criteria> filter);
    void update(FunctionResponse function);
    void delete(Long id);
}
