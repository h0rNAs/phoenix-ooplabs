package ru.ssau.tk.phoenix.ooplabs;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.entities.User;
import ru.ssau.tk.phoenix.ooplabs.repositories.FunctionRepository;
import ru.ssau.tk.phoenix.ooplabs.repositories.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ExportResults {
    private final String tableName = "query_performance";
    private final int COUNT = 10000;

    private List<User> users = new ArrayList<>(COUNT);
    private List<Function> functions = new ArrayList<>(COUNT);

    @Autowired private UserRepository userRepository;
    @Autowired private FunctionRepository functionRepository;
    @Autowired private JdbcTemplate jdbc;


    @Test
    public void performance() {
        for (int i = 0; i < COUNT; i++) {
            User user = new User("user_" + (i + 1), "password");
            users.add(user);
            functions.add(
                    new Function(user, "SIMPLE", "function_" + (i+1), "{}"));
        }

        toCsv("save (User)", () -> {
            try {
                saveUsers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv("findById (User)", () -> {
            try {
                findUsersById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv("findByUsername (User)", () -> {
            try {
                findUsersByUsername();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv("delete (User)", () -> {
            try {
                deleteUsers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv("save (Function)", () -> {
            try {
                saveFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv("findById (Function)", () -> {
            try {
                findFunctionsById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv("findByUserId (Function)", () -> {
            try {
                findFunctionsByUserId();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv("update (Function)", () -> {
            try {
                updateFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv("delete (Function)", () -> {
            try {
                deleteFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @AfterEach
    void truncateTables(){
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
        for (int i = 0; i < COUNT; i++) {
            Function func = functions.get(i);
            func.setDefinition("{\"function\": \"x^2\"}");
            functionRepository.save(func);
        }
    }

    private void deleteFunctions() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            functionRepository.delete(functions.get(i));
        }
    }

    private void toCsv(String query, Runnable task) {
        Long startTime = System.currentTimeMillis();
        task.run();
        Long duration = System.currentTimeMillis() - startTime;

        if (existsInTable(query)){
            String sql = "UPDATE " + tableName + " SET framework_duration = ? WHERE query = ?";
            jdbc.update(sql, duration, query);
        }
        else {
            String sql = "INSERT INTO " + tableName + " (query, framework_duration) VALUES (?, ?)";
            jdbc.update(sql, query, duration);
        }
    }

    private boolean existsInTable(String query){
        String sql = "SELECT EXISTS(SELECT 1 FROM " + tableName + " WHERE query = ?)";
        return jdbc.queryForObject(sql, Boolean.class, query);
    }

    private List<String> generateUniqueNames(String base, int count){
        List<String> names = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            names.add(base + (i+1));
        }
        return names;
    }
}
