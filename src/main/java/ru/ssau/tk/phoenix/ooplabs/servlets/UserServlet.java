package ru.ssau.tk.phoenix.ooplabs.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;
import ru.ssau.tk.phoenix.ooplabs.service.UserApiContract;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class UserServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger();
    private UserApiContract userService;

    @Override
    public void init() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            userService = DataBaseManager.getUserService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String username = req.getParameter("username");

        UserResponse user = null;
        if (id != null && !id.isEmpty()){
            try {
                user = userService.find(Long.valueOf(id));
            } catch (NoSuchElementException e) {
                // Пользователь не найден
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if (username != null && !username.isEmpty()){
            try {
                user = userService.find(username);
            } catch (NoSuchElementException e) {
                // Пользователь не найден
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            // Неверные данные
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(user);
            resp.getWriter().write(json);
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            UserResponse user = userService.save(new UserRequest(username, password));
        } catch (IllegalArgumentException e) {
            // Пользователь уже зарегестрирован
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String newPassword = req.getParameter("newPassword");

        try {
            userService.update(Long.valueOf(id), newPassword);
        } catch (NoSuchElementException e){
            // Пользователь не найден
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        try {
            userService.delete(Long.valueOf(id));
        } catch (NoSuchElementException e) {
            // Пользователь не найден
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
