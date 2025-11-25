package ru.ssau.tk.phoenix.ooplabs.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.util.PGobject;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class FunctionDaoImpl implements FunctionDao{
    private final Logger logger = LogManager.getLogger(FunctionDaoImpl.class);
    private final Connection conn;

    public FunctionDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Optional<FunctionResponse> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM functions WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                FunctionResponse func = new FunctionResponse(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("function_type"),
                        ((PGobject)rs.getObject("definition")).getValue()
                );
                return Optional.of(func);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
        return Optional.empty();
    }

    @Override
    public List<FunctionResponse> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM functions WHERE user_id = ?";
        List<FunctionResponse> functions = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                functions.add(new FunctionResponse(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("function_type"),
                        ((PGobject)rs.getObject("definition")).getValue()
                ));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }

        return functions;
    }

    @Override
    public List<FunctionResponse> findWithFilter(List<Criteria> filter) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM functions");
        List<Object> parameters = new ArrayList<>();
        List<String> whereConditions = new ArrayList<>();
        List<String> orderByConditions = new ArrayList<>();

        for (Criteria criteria : filter) {
            if (criteria.getParams() != null && criteria.getParams().length > 0) {
                String column = criteria.getColumn();

                if (!isValidColumn(column)) {
                    logger.warn("Попытка фильтрации по недопустимой колонке: " + column);
                    continue;
                }

                String placeholders = String.join(",", Collections.nCopies(criteria.getParams().length, "?"));
                whereConditions.add(column + " IN (" + placeholders + ")");

                if ("function_type".equals(column)) {
                    for (Object param : criteria.getParams()) {
                        parameters.add(param.toString());
                    }
                } else {
                    Collections.addAll(parameters, criteria.getParams());
                }

                logger.info("Фильтрация по {} с {} параметрами", column, criteria.getParams().length);
            }

            if (criteria.getSortingType() != null) {
                String column = criteria.getColumn();
                if (isValidColumn(column)) {
                    orderByConditions.add(column + " " + criteria.getSortingType());
                }
            }
        }

        if (!whereConditions.isEmpty()) {
            sqlBuilder.append(" WHERE ").append(String.join(" AND ", whereConditions));
        }

        if (!orderByConditions.isEmpty()) {
            sqlBuilder.append(" ORDER BY ").append(String.join(", ", orderByConditions));
            logger.info("Сортировка по: {}", String.join(", ", orderByConditions));
        }

        String sql = sqlBuilder.toString();
        logger.debug(sql);

        List<FunctionResponse> functions = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    functions.add(new FunctionResponse(
                            rs.getLong("id"),
                            rs.getLong("user_id"),
                            rs.getString("name"),
                            rs.getString("function_type"),
                            ((PGobject) rs.getObject("definition")).getValue()
                    ));
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}", e.getMessage());
            throw e;
        }

        return functions;
    }

    @Override
    public FunctionResponse save(FunctionRequest func) throws SQLException {
        String sql = "INSERT INTO functions (user_id, name, function_type, definition) VALUES (?, ?, ?, ?::jsonb) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, func.getUserId());
            ps.setString(2, func.getName());
            ps.setString(3, func.getType().toString());
            ps.setString(4, func.getDefinition());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Long id = rs.getLong("id");
                return new FunctionResponse(id, func);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
        return new FunctionResponse(0L, func);
    }

    @Override
    public FunctionResponse update(FunctionResponse func) throws SQLException {
        String sql = "UPDATE functions SET name = ?, function_type = ?, definition = ?::jsonb WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, func.getName());
            ps.setString(2, func.getType().toString());
            ps.setString(3, func.getDefinition());
            ps.setLong(4, func.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
        return func;
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM functions WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private boolean isValidColumn(String column) {
        return Set.of("user_id", "function_type", "name").contains(column);
    }
}
