package ru.ssau.tk.phoenix.ooplabs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.service.FunctionService;
import ru.ssau.tk.phoenix.ooplabs.service.UserService;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;
import ru.ssau.tk.phoenix.ooplabs.util.FunctionType;
import ru.ssau.tk.phoenix.ooplabs.util.SortingType;

import java.sql.SQLException;
import java.util.*;

@SpringBootTest
public class ExportResults {
    private final String queryResultsTable = "query_performance";
    private final String sortingResultsTable = "sorting_performance";
    private final int COUNT = 10000;

    private List<UserRequest> userRequests = new ArrayList<>(COUNT);
    private List<FunctionRequest> functionRequests = new ArrayList<>(COUNT);
    private List<UserResponse> users = new ArrayList<>(COUNT);
    private List<FunctionResponse> functions = new ArrayList<>(COUNT);

    @Autowired
    private UserService userService;
    @Autowired
    private FunctionService functionService;
    @Autowired
    private JdbcTemplate jdbc;


    @Test
    public void performance() {
        for (int i = 0; i < COUNT; i++) {
            UserRequest user = new UserRequest("user_" + (i + 1), "password");
            userRequests.add(user);
            functionRequests.add(
                    new FunctionRequest((long) i, "function_" + (i + 1), FunctionType.SIMPLE, "{}"));
        }

        toCsv(queryResultsTable, "save (User)", () -> saveUsers());
        toCsv(queryResultsTable, "findById (User)", () -> findUsersById());
        toCsv(queryResultsTable, "findByUsername (User)", () -> findUsersByUsername());
        toCsv(queryResultsTable, "delete (User)", () -> deleteUsers());
        toCsv(queryResultsTable, "save (Function)", () -> saveFunctions());


        List<Criteria> filter = new ArrayList<>();
        filter.add(null);
        filter.add(new Criteria("name", null, SortingType.ASC));
        toCsv(sortingResultsTable, "findByIdAndOrderByName (ASC)", () -> findWithFilter(filter));

        filter.set(1, new Criteria("name", null, SortingType.DESC));
        toCsv(sortingResultsTable, "findByIdAndOrderByName (DESC)", () -> findWithFilter(filter));

        filter.set(1, new Criteria("function_type", null, SortingType.ASC));
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByType (ASC)", () -> findWithFilter(filter));

        filter.set(1, new Criteria("function_type", null, SortingType.DESC));
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByType (DESC)", () -> findWithFilter(filter));

        filter.set(1, new Criteria("name", null, SortingType.ASC));
        filter.add(new Criteria("function_type", null, SortingType.ASC));
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndType (ASC)", () -> findWithFilter(filter));

        filter.set(1, new Criteria("name", null, SortingType.DESC));
        filter.set(2, new Criteria("function_type", null, SortingType.DESC));
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndType (DESC)", () -> findWithFilter(filter));

        filter.set(1, new Criteria("name", null, SortingType.ASC));
        filter.set(2, new Criteria("function_type",
                new Object[]{FunctionType.SIMPLE, FunctionType.COMPOSITE}, SortingType.ASC));
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndTypeIn (ASC)", () -> findWithFilter(filter));

        filter.set(1, new Criteria("name", null, SortingType.DESC));
        filter.set(2, new Criteria("function_type",
                new Object[]{FunctionType.SIMPLE, FunctionType.COMPOSITE}, SortingType.DESC));
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndTypeIn (DESC)", () -> findWithFilter(filter));


        toCsv(queryResultsTable, "findById (Function)", () -> findFunctionsById());
        toCsv(queryResultsTable, "findByUserId (Function)", () -> findFunctionsByUserId());
        toCsv(queryResultsTable, "update (Function)", () -> updateFunctions());
        toCsv(queryResultsTable, "delete (Function)", () -> deleteFunctions());
    }

    @AfterEach
    void truncateTables() {
        String truncateUsers = "TRUNCATE TABLE users RESTART IDENTITY CASCADE";
        String truncateFunctions = "TRUNCATE TABLE functions RESTART IDENTITY CASCADE";
        jdbc.execute(truncateFunctions);
        jdbc.execute(truncateUsers);
    }

    private void saveUsers() {
        for (int i = 0; i < COUNT; i++) {
            users.add(userService.save(userRequests.get(i)));
        }
    }

    private void findUsersById() {
        for (int i = 0; i < COUNT; i++) {
            userService.find(users.get(i).getId());
        }
    }

    private void findUsersByUsername() {
        for (int i = 0; i < COUNT; i++) {
            userService.find(users.get(i).getUsername());
        }
    }

    private void deleteUsers() {
        for (int i = 0; i < COUNT; i++) {
            userService.delete(users.get(i).getId());
        }
    }

    private void saveFunctions() {
        for (int i = 0; i < COUNT; i++) {
            functions.add(functionService.save(functionRequests.get(i)));
        }
    }

    private void findFunctionsById() {
        for (int i = 0; i < COUNT; i++) {
            functionService.find(functions.get(i).getId());
        }
    }

    private void findFunctionsByUserId() {
        List<Criteria> filter = new ArrayList<>();
        filter.add(null);
        for (int i = 0; i < COUNT; i++) {
            filter.set(0, new Criteria("user_id", new Object[]{users.get(i).getId()}, null));
            functionService.findWithFilter(filter);
        }
    }

    private void updateFunctions() {
        for (int i = 0; i < COUNT; i++) {
            FunctionResponse func = functions.get(i);
            func.setDefinition("{\"function\": \"x^2\"}");
            functionService.update(func);
        }
    }

    private void deleteFunctions() {
        for (int i = 0; i < COUNT; i++) {
            functionService.delete(functions.get(i).getId());
        }
    }

    /**
     * @param filter Первым элементом обязательно user_id!
     */
    private void findWithFilter(List<Criteria> filter) {
        for (int i = 0; i < COUNT; i++) {
            filter.set(0, new Criteria("user_id", new Object[]{users.get(i).getId()}, null));
            functionService.findWithFilter(filter);
        }
    }

    private void findFunctionByIdAndOrderByName(SortingType sort) {
        List<Criteria> filter = new ArrayList<>();
        filter.add(new Criteria());
        filter.add(new Criteria("name", null, sort));

        for (int i = 0; i < COUNT; i++) {
            filter.set(0, new Criteria("user_id", new Object[]{users.get(i).getId()}, null));
            functionService.findWithFilter(filter);
        }
    }

    private void findFunctionByIdAndOrderByType(SortingType sort) {
        List<Criteria> filter = List.of(new Criteria(), new Criteria("function_type", null, sort));
        for (int i = 0; i < COUNT; i++) {
            filter.set(0, new Criteria("user_id", new Object[]{users.get(i).getId()}, null));
            functionService.findWithFilter(filter);
        }
    }

    private void findFunctionByIdAndOrderByNameAndType(SortingType sort) {
        List<Criteria> filter = List.of(new Criteria(), new Criteria("name", null, sort),
                new Criteria("function_type", null, sort));
        for (int i = 0; i < COUNT; i++) {
            filter.set(0, new Criteria("user_id", new Object[]{users.get(i).getId()}, null));
            functionService.findWithFilter(filter);
        }
    }

    private void findFunctionByIdAndOrderByNameAndTypeIn(SortingType sort, FunctionType[] types) {
        List<Criteria> filter = List.of(new Criteria(), new Criteria("name", null, sort),
                new Criteria("function_type", types, sort));
        for (int i = 0; i < COUNT; i++) {
            filter.set(0, new Criteria("user_id", new Object[]{users.get(i).getId()}, null));
            functionService.findWithFilter(filter);
        }
    }

    private void toCsv(String tableName, String query, Runnable task) {
        Long startTime = System.currentTimeMillis();
        task.run();
        Long duration = System.currentTimeMillis() - startTime;

        if (existsInTable(tableName, query)) {
            String sql = "UPDATE " + tableName + " SET framework_duration = ? WHERE query = ?";
            jdbc.update(sql, duration, query);
        } else {
            String sql = "INSERT INTO " + tableName + " (query, framework_duration) VALUES (?, ?)";
            jdbc.update(sql, query, duration);
        }
    }

    private boolean existsInTable(String tableName, String query) {
        String sql = "SELECT EXISTS(SELECT 1 FROM " + tableName + " WHERE query = ?)";
        return jdbc.queryForObject(sql, Boolean.class, query);
    }
}
