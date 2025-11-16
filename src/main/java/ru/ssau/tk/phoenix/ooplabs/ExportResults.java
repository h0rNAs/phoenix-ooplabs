// TODO: Сделать работу с таблицами через контроллеры

package ru.ssau.tk.phoenix.ooplabs;

import ru.ssau.tk.phoenix.ooplabs.dao.Function;
import ru.ssau.tk.phoenix.ooplabs.dao.FunctionDao;
import ru.ssau.tk.phoenix.ooplabs.dao.User;
import ru.ssau.tk.phoenix.ooplabs.dao.UserDao;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;
import ru.ssau.tk.phoenix.ooplabs.util.FunctionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExportResults {
    private final int COUNT;

    private final Connection conn = DataBaseManager.getConnection();
    private final UserDao userDao = DataBaseManager.getUserDao();
    private final FunctionDao functionDao = DataBaseManager.getFunctionDao();

    private final List<UserResponse> users = new ArrayList<>();
    private final List<FunctionResponse> functions = new ArrayList<>();
    private final List<UserRequest> userRequests = new ArrayList<>();
    private final List<FunctionRequest> functionRequests = new ArrayList<>();


    public static void main(String[] args) throws SQLException {
        ExportResults export = new ExportResults(10000);

        export.exportQueryResults("query_performance");
        export.exportSortingResults("");

        DataBaseManager.truncateTable("users");
        DataBaseManager.truncateTable("functions");
    }

    public ExportResults(int count) {
        this.COUNT = count;

        for (int i = 0; i < COUNT; i++) {
            UserRequest user = new UserRequest("user_" + (i + 1), "password");
            userRequests.add(user);
            functionRequests.add(
                    new FunctionRequest((long)i, "function_" + (i+1), FunctionType.SIMPLE, "{}"));
        }
    }

    public void exportSortingResults(String tableName) throws SQLException {
        saveUsers();
        saveFunctions();
    }

    public void exportQueryResults(String tableName) throws SQLException {
        toCsv(tableName, "save (User)", () -> {
            try {
                saveUsers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(tableName, "findById (User)", () -> {
            try {
                findUsersById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(tableName, "findByUsername (User)", () -> {
            try {
                findUsersByUsername();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(tableName, "delete (User)", () -> {
            try {
                deleteUsers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(tableName, "save (Function)", () -> {
            try {
                saveFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(tableName, "findById (Function)", () -> {
            try {
                findFunctionsById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(tableName, "findByUserId (Function)", () -> {
            try {
                findFunctionsByUserId();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(tableName, "update (Function)", () -> {
            try {
                updateFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        toCsv(tableName, "delete (Function)", () -> {
            try {
                deleteFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        users.clear();
        functions.clear();
    }

    private void saveUsers() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            users.set(i, userDao.save(userRequests.get(i)));
        }
    }

    private void findUsersById() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            userDao.findById(users.get(i).getId());
        }
    }

    private void findUsersByUsername() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            userDao.findByUsername(users.get(i).getUsername());
        }
    }

    private void deleteUsers() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            userDao.delete(users.get(i).getId());
        }
    }

    private void saveFunctions() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            functions.set(i, functionDao.save(functionRequests.get(i)));
        }
    }

    private void findFunctionsById() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            functionDao.findById(functions.get(i).getId());
        }
    }

    private void findFunctionsByUserId() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            functionDao.findByUserId(functions.get(i).getUserId());
        }
    }

    private void updateFunctions() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            FunctionResponse func = functions.get(i);
            func.setDefinition("{\"function\": \"x^2\"}");
            functionDao.update(func);
        }
    }

    private void deleteFunctions() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            functionDao.delete(functions.get(i).getId());
        }
    }

    private void toCsv(String tableName, String query, Runnable task) throws SQLException {
        Long startTime = System.currentTimeMillis();
        task.run();
        Long duration = System.currentTimeMillis() - startTime;

        if (existsInTable(tableName, query)){
            String sql = "UPDATE " + tableName + " SET manual_duration = ? WHERE query = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setLong(1, duration);
                ps.setString(2, query);
                ps.executeUpdate();
            }
        }
        else {
            String sql = "INSERT INTO " + tableName + " (query, manual_duration) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, query);
                ps.setLong(2, duration);
                ps.executeUpdate();
            }
        }
    }

    private boolean existsInTable(String tableName, String query) throws SQLException{
        String sqlExists = "SELECT EXISTS(SELECT 1 FROM " + tableName + " WHERE query = ?)";
        try(PreparedStatement psExists = conn.prepareStatement(sqlExists)){
            psExists.setString(1, query);
            try(ResultSet rs = psExists.executeQuery()){
                if (rs.next()){
                    return rs.getBoolean(1);
                }
            }
        }
        return false;
    }
}
