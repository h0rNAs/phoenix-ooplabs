package ru.ssau.tk.phoenix.ooplabs.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.service.UserService;

import java.sql.*;
import java.util.Optional;

public class UserDaoImpl implements UserDao{
    private final Logger logger = LogManager.getLogger(UserService.class);
    private final Connection conn;

    public UserDaoImpl(Connection connection) {
        conn = connection;
    }


    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, username, password FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                logger.info("Считанный пользователь: " + user.toString());
                return Optional.of(user);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, password FROM users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                logger.info("Считанный пользователь: " + user.toString());
                return Optional.of(user);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?) RETURNING id";
        Long id = user.getId();
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
            throw new RuntimeException(e);
        }
        return new User(id, user);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            logger.info("Пользователь id = " + id + " успешно удален");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
