package ru.ssau.tk.phoenix.ooplabs.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.util.PGobject;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                logger.info("Считанная функция: " + func);
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
        List<FunctionResponse> functions = new ArrayList<>();
        String sql = "SELECT * FROM functions WHERE user_id = ?";
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
                logger.info("Функция успешно добавлена");
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
        String sql = "UPDATE functions SET definition = ?::jsonb WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, func.getDefinition());
            ps.setLong(2, func.getId());
            ps.executeUpdate();
            logger.info("Функция успешно обновлена");
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
            logger.info("Функция id = " + id + " успешно удалена");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
