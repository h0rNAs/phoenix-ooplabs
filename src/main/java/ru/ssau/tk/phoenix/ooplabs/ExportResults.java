// TODO: Сделать работу с таблицами через контроллеры

package ru.ssau.tk.phoenix.ooplabs;

import ru.ssau.tk.phoenix.ooplabs.dao.FunctionDao;
import ru.ssau.tk.phoenix.ooplabs.dao.UserDao;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;
import ru.ssau.tk.phoenix.ooplabs.util.FunctionType;
import ru.ssau.tk.phoenix.ooplabs.util.SortingType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExportResults {
    private static final String queryResultsTable = "query_performance";
    private static final String sortingResultsTable = "sorting_performance";
    private final int COUNT = 10000;

    private final Connection conn = DataBaseManager.getConnection();
    private final UserDao userDao = DataBaseManager.getUserDao();
    private final FunctionDao functionDao = DataBaseManager.getFunctionDao();

    private final List<UserResponse> users = new ArrayList<>();
    private final List<FunctionResponse> functions = new ArrayList<>();
    private final List<UserRequest> userRequests = new ArrayList<>();
    private final List<FunctionRequest> functionRequests = new ArrayList<>();


    public static void main(String[] args) throws SQLException {
        ExportResults export = new ExportResults();

        export.toCsv(queryResultsTable, "save (User)", () -> {
            try {
                export.saveUsers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(queryResultsTable, "findById (User)", () -> {
            try {
                export.findUsersById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(queryResultsTable, "findByUsername (User)", () -> {
            try {
                export.findUsersByUsername();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(queryResultsTable, "delete (User)", () -> {
            try {
                export.deleteUsers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(queryResultsTable, "save (Function)", () -> {
            try {
                export.saveFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        export.toCsv(sortingResultsTable, "findByIdAndOrderByName (ASC)", () -> {
            try {
                export.findFunctionByIdAndOrderByName(SortingType.ASC);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(sortingResultsTable, "findByIdAndOrderByName (DESC)", () -> {
            try {
                export.findFunctionByIdAndOrderByName(SortingType.DESC);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(sortingResultsTable, "findFunctionByIdAndOrderByType (ASC)", () -> {
            try {
                export.findFunctionByIdAndOrderByType(SortingType.ASC);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(sortingResultsTable, "findFunctionByIdAndOrderByType (DESC)", () -> {
            try {
                export.findFunctionByIdAndOrderByType(SortingType.DESC);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndType (ASC)", () -> {
            try {
                export.findFunctionByIdAndOrderByNameAndType(SortingType.ASC);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndType (DESC)", () -> {
            try {
                export.findFunctionByIdAndOrderByNameAndType(SortingType.DESC);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndTypeIn (ASC)", () -> {
            try {
                export.findFunctionByIdAndOrderByNameAndTypeIn(SortingType.ASC,
                        new FunctionType[]{FunctionType.SIMPLE, FunctionType.COMPOSITE});
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(sortingResultsTable, "findFunctionByIdAndOrderByNameAndTypeIn (DESC)", () -> {
            try {
                export.findFunctionByIdAndOrderByNameAndTypeIn(SortingType.DESC,
                        new FunctionType[]{FunctionType.SIMPLE, FunctionType.COMPOSITE});
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        export.toCsv(queryResultsTable, "findById (Function)", () -> {
            try {
                export.findFunctionsById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(queryResultsTable, "findByUserId (Function)", () -> {
            try {
                export.findFunctionsByUserId();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv(queryResultsTable, "update (Function)", () -> {
            try {
                export.updateFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        export.toCsv("query_performance", "delete (Function)", () -> {
            try {
                export.deleteFunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        DataBaseManager.truncateTable("users");
        DataBaseManager.truncateTable("functions");
    }

    public ExportResults() {
        for (int i = 0; i < COUNT; i++) {
            UserRequest user = new UserRequest("user_" + (i + 1), "password");
            userRequests.add(user);
            functionRequests.add(
                    new FunctionRequest((long)i, "function_" + (i+1), FunctionType.SIMPLE, "{}"));
        }
    }

    private void saveUsers() throws SQLException {
        for (int i = 0; i < COUNT; i++) {
            users.add(userDao.save(userRequests.get(i)));
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
            functions.add(functionDao.save(functionRequests.get(i)));
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

    private void findFunctionByIdAndOrderByName(SortingType sort) throws SQLException {
        List<Criteria> filter = List.of(new Criteria("name", null, sort));
        for (int i = 0; i < COUNT; i++) {
            functionDao.findAndSortWithFilter(users.get(i).getId(), filter);
        }
    }

    private void findFunctionByIdAndOrderByType(SortingType sort) throws SQLException {
        List<Criteria> filter = List.of(new Criteria("function_type", null, sort));
        for (int i = 0; i < COUNT; i++) {
            functionDao.findAndSortWithFilter(users.get(i).getId(), filter);
        }
    }

    private void findFunctionByIdAndOrderByNameAndType(SortingType sort) throws SQLException {
        List<Criteria> filter = List.of(new Criteria("name", null, sort),
                new Criteria("function_type", null, sort));
        for (int i = 0; i < COUNT; i++) {
            functionDao.findAndSortWithFilter(users.get(i).getId(), filter);
        }
    }
    private void findFunctionByIdAndOrderByNameAndTypeIn(SortingType sort, FunctionType[] types) throws SQLException {
        List<Criteria> filter = List.of(new Criteria("name", null, sort),
                new Criteria("function_type", types, sort));
        for (int i = 0; i < COUNT; i++) {
            functionDao.findAndSortWithFilter(users.get(i).getId(), filter);
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
