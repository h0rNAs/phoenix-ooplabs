package ru.ssau.tk.phoenix.ooplabs.repositories;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;
import ru.ssau.tk.phoenix.ooplabs.util.FunctionType;
import ru.ssau.tk.phoenix.ooplabs.util.SortingType;

import java.util.ArrayList;
import java.util.List;

public class FunctionSpecifications {
    private static final Logger logger = LogManager.getLogger(FunctionSpecifications.class);

    public static Specification<Function> buildSpecification(List<Criteria> criteriaList) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Criteria criteria : criteriaList) {
                String column = criteria.getColumn();
                Object[] params = criteria.getParams();

                if (params != null && params.length > 0) {
                    switch (column) {
                        case "user_id", "name", "function_type":
                            Predicate predicate = buildPredicate(criteriaBuilder, root, column, params);
                            if (predicate != null) predicates.add(predicate);
                            break;
                    }
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<Function> root, String column, Object[] params){
        List<Predicate> predicates = new ArrayList<>();
        for (Object param : params) {
            predicates.add(criteriaBuilder.equal(root.get(mapColumnToPropertyName(column)), param.toString()));
        }
        if (!predicates.isEmpty()) {
            logger.info("Составлен запрос на поиск по " + column + " из " + params.toString());
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        }
        return null;
    }

    public static Sort buildSort(List<Criteria> criteriaList) {
        List<Sort.Order> orders = new ArrayList<>();

        for (Criteria criteria : criteriaList) {
            if (criteria.getSortingType() != null) {
                String column = mapColumnToPropertyName(criteria.getColumn());
                SortingType type = criteria.getSortingType();

                Sort.Order order = (type == SortingType.ASC)
                        ? Sort.Order.asc(column)
                        : Sort.Order.desc(column);

                logger.info("Составлена сортировка (" + type + ") по " + column);
                orders.add(order);
            }
        }

        return Sort.by(orders);
    }

    private static String mapColumnToPropertyName(String column){
        switch (column) {
            case "user_id":
                return "userId";
            case "function_type":
                return "type";
            default:
                return column;
        }
    }
}
