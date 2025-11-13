package ru.ssau.tk.phoenix.ooplabs.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

import java.sql.*;
import java.util.Optional;

public class UserDaoImpl implements UserDao{
    private final Logger logger = LogManager.getLogger(UserDaoImpl.class);
    private final Connection conn;


    public UserDaoImpl(Connection connection) {
        conn = connection;
    }

    @Override
    public Optional<UserResponse> findById(Long id) throws SQLException {
        String sql = "SELECT id, username, password FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserResponse user = new UserResponse(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                logger.info("Считанный пользователь: " + user.toString());
                return Optional.of(user);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password FROM users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserResponse user = new UserResponse(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                logger.info("Считанный пользователь: " + user.toString());
                return Optional.of(user);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
        return Optional.empty();
    }

    @Override
    public UserResponse save(UserRequest user) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?) RETURNING id";
        Long id = 0L;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                id = rs.getLong("id");
                logger.info("Пользователь успешно добавлен");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
        return new UserResponse(id, user);
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            logger.info("Пользователь id = " + id + " успешно удален");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
