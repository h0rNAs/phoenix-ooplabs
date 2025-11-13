// TODO: Сделать работу с таблицами через контроллеры

/*
package ru.ssau.tk.phoenix.ooplabs;

import ru.ssau.tk.phoenix.ooplabs.dao.Function;
import ru.ssau.tk.phoenix.ooplabs.dao.FunctionDao;
import ru.ssau.tk.phoenix.ooplabs.dao.User;
import ru.ssau.tk.phoenix.ooplabs.dao.UserDao;
import ru.ssau.tk.phoenix.ooplabs.util.FunctionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExportResults {
    private final String tableName = "query_performance";
    private final int COUNT = 10000;
    private final Connection conn = DataBaseManager.getConnection();
    private final UserDao userDao = DataBaseManager.getUserDao();
    private final FunctionDao functionDao = DataBaseManager.getFunctionDao();

    private List<User> users = new ArrayList<>(COUNT);
    private List<Function> functions = new ArrayList<>(COUNT);


    public static void main(String[] args) throws SQLException {
        ExportResults export = new ExportResults();

        for (int i = 0; i < export.COUNT; i++) {
            User user = new User("user_" + (i + 1), "password");
            export.users.add(user);
            export.functions.add(
                    new Function((long)i, FunctionType.SIMPLE, "{}"));
        }

        export.toCsv("save (User)", () -> {
            try {
                export.saveUsers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv("findById (User)", () -> {
            try {
                export.findUsersById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv("findByUsername (User)", () -> {
            try {
                export.findUsersByUsername();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv("delete (User)", () -> {
            try {
                export.deleteUsers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv("save (Function)", () -> {
            try {
                export.saveFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv("findById (Function)", () -> {
            try {
                export.findFunctionsById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv("findByUserId (Function)", () -> {
            try {
                export.findFunctionsByUserId();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv("update (Function)", () -> {
            try {
                export.updateFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv("delete (Function)", () -> {
            try {
                export.deleteFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        DataBaseManager.truncateTable("users");
        DataBaseManager.truncateTable("functions");
    }

    private void saveUsers() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            users.set(i, userDao.save(users.get(i)));
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
            functions.set(i, functionDao.save(functions.get(i)));
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
            Function func = functions.get(i);
            func.setDefinition("{\"function\": \"x^2\"}");
            functionDao.update(func);
        }
    }

    private void deleteFunctions() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            functionDao.delete(functions.get(i).getId());
        }
    }

    private void toCsv(String query, Runnable task) throws SQLException {
        Long startTime = System.currentTimeMillis();
        task.run();
        Long duration = System.currentTimeMillis() - startTime;

        if (existsInTable(query)){
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

    private boolean existsInTable(String query) throws SQLException{
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
*/
