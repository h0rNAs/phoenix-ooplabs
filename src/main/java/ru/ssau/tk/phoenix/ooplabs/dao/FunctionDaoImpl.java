package ru.ssau.tk.phoenix.ooplabs.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class FunctionDaoImpl implements FunctionDao{
    private final Logger logger = LogManager.getLogger(FunctionDao.class);
    private final Connection conn;

    public FunctionDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Optional<Function> findById(Long id) {
        String sql = "SELECT id, user_id, type FROM functions WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Function func = new Function(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("type")
                );
                logger.info("Считанная функция: " + func);
                return Optional.of(func);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Function> findByUserId(Long userId) {
        String sql = "SELECT id, user_id, type FROM functions WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Function func = new Function(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("type")
                );
                logger.info("Считанная функция: " + func);
                return Optional.of(func);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Function save(Function func) {
        String sql = "INSERT INTO functions (user_id, type) VALUES (?, ?) RETURNING id";
        Long id = func.getId();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, func.getUserId());
            ps.setString(2, func.getType().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                id = rs.getLong("id");
                logger.info("Функция успешно добавлена");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return new Function(id, func);
    }

    @Override
    public Function update(Function func) {
        return null;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM functions WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            logger.info("Функция id = " + id + " успешно удалена");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
