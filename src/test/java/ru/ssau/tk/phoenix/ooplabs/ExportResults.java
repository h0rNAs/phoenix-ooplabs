package ru.ssau.tk.phoenix.ooplabs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.entities.User;
import ru.ssau.tk.phoenix.ooplabs.repositories.FunctionRepository;
import ru.ssau.tk.phoenix.ooplabs.repositories.UserRepository;

import java.sql.SQLException;
import java.util.*;

@SpringBootTest
public class ExportResults {
    private final String queryResultsTable = "query_performance";
    private final String sortingResultsTable = "sorting_performance";
    private final int COUNT = 10000;

    private List<User> users = new ArrayList<>(COUNT);
    private List<Function> functions = new ArrayList<>(COUNT);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private JdbcTemplate jdbc;


    @Test
    public void performance() {
        for (int i = 0; i < COUNT; i++) {
            User user = new User("user_" + (i + 1), "password");
            users.add(user);
            functions.add(
                    new Function(user, "SIMPLE", "function_" + (i + 1), new HashMap<>()));
        }

        toCsv(queryResultsTable, "save (User)", () -> {
            try {
                saveUsers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(queryResultsTable, "findById (User)", () -> {
            try {
                findUsersById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(queryResultsTable, "findByUsername (User)", () -> {
            try {
                findUsersByUsername();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(queryResultsTable, "delete (User)", () -> {
            try {
                deleteUsers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(queryResultsTable, "save (Function)", () -> {
            try {
                saveFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        toCsv(sortingResultsTable, "findByIdAndOrderByName (ASC)", () -> {
            try {
                findFunctionByIdAndOrderByNameAsc();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(sortingResultsTable, "findByIdAndOrderByName (DESC)", () -> {
            try {
                findFunctionByIdAndOrderByNameDesc();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByType (ASC)", () -> {
            try {
                findFunctionByIdAndOrderByTypeAsc();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByType (DESC)", () -> {
            try {
                findFunctionByIdAndOrderByTypeDesc();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndType (ASC)", () -> {
            try {
                findFunctionByIdAndOrderByNameAndTypeAsc();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndType (DESC)", () -> {
            try {
                findFunctionByIdAndOrderByNameAndTypeDesc();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndTypeIn (ASC)", () -> {
            try {
                findFunctionByIdAndOrderByNameAndTypeInAsc();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndTypeIn (DESC)", () -> {
            try {
                findFunctionByIdAndOrderByNameAndTypeInDesc();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        toCsv(queryResultsTable, "findById (Function)", () -> {
            try {
                findFunctionsById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(queryResultsTable, "findByUserId (Function)", () -> {
            try {
                findFunctionsByUserId();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(queryResultsTable, "update (Function)", () -> {
            try {
                updateFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(queryResultsTable, "delete (Function)", () -> {
            try {
                deleteFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @AfterEach
    void truncateTables() {
        String truncateUsers = "TRUNCATE TABLE users RESTART IDENTITY CASCADE";
        String truncateFunctions = "TRUNCATE TABLE functions RESTART IDENTITY CASCADE";
        jdbc.execute(truncateFunctions);
        jdbc.execute(truncateUsers);
    }

    private void saveUsers() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            users.set(i, userRepository.save(users.get(i)));
        }
    }

    private void findUsersById() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            userRepository.findById(users.get(i).getId());
        }
    }

    private void findUsersByUsername() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            userRepository.findByUsername(users.get(i).getUsername());
        }
    }

    private void deleteUsers() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            userRepository.delete(users.get(i));
        }
    }

    private void saveFunctions() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            Function function = functions.get(i);
            function.setUser(users.get(i));
            functions.set(i, functionRepository.save(function));
        }
    }

    private void findFunctionsById() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            functionRepository.findById(functions.get(i).getId());
        }
    }

    private void findFunctionsByUserId() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            functionRepository.findByUserId(functions.get(i).getUser().getId());
        }
    }

    private void updateFunctions() throws SQLException {
        Map<String, Object> definition = Map.of("function", "x^2");
        for (int i = 0; i < COUNT; i++) {
            Function func = functions.get(i);
            func.setDefinition(definition);
            functionRepository.save(func);
        }
    }

    private void deleteFunctions() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            functionRepository.delete(functions.get(i));
        }
    }

    private void findFunctionByIdAndOrderByNameAsc() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            User user = users.get(i);
            functionRepository.findByUserIdOrderByNameAsc(user.getId()).size();
        }
    }

    private void findFunctionByIdAndOrderByNameDesc() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            User user = users.get(i);
            functionRepository.findByUserIdOrderByNameDesc(user.getId());
        }
    }

    private void findFunctionByIdAndOrderByTypeAsc() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            User user = users.get(i);
            functionRepository.findByUserIdOrderByTypeAsc(user.getId());
        }
    }

    private void findFunctionByIdAndOrderByTypeDesc() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            User user = users.get(i);
            functionRepository.findByUserIdOrderByTypeDesc(user.getId());
        }
    }

    private void findFunctionByIdAndOrderByNameAndTypeAsc() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            User user = users.get(i);
            String type = "SIMPLE";
            functionRepository.findByUserIdAndTypeOrderByNameAscTypeAsc(user.getId(), type);
        }
    }

    private void findFunctionByIdAndOrderByNameAndTypeDesc() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            User user = users.get(i);
            String type = "SIMPLE";
            functionRepository.findByUserIdAndTypeOrderByNameDescTypeDesc(user.getId(), type);
        }
    }

    private void findFunctionByIdAndOrderByNameAndTypeInAsc() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            User user = users.get(i);
            List<String> types = Arrays.asList("SIMPLE", "TABULATED");
            functionRepository.findByUserIdAndTypeInOrderByNameAscTypeAsc(user.getId(), types);
        }
    }

    private void findFunctionByIdAndOrderByNameAndTypeInDesc() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            User user = users.get(i);
            List<String> types = Arrays.asList("SIMPLE", "TABULATED");
            functionRepository.findByUserIdAndTypeInOrderByNameDescTypeDesc(user.getId(), types);
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
