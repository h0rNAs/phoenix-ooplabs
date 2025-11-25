package ru.ssau.tk.phoenix.ooplabs.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.ErrorResponse;
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
        resp.setContentType("application/json;charset=UTF-8");

        String username = req.getParameter("username");

        if (username != null && !username.isEmpty()){
            try {
                ServletUtils.sendAnswer(userService.find(username), resp);
            } catch (NoSuchElementException e) {
                ErrorResponse error = ErrorResponse.constructNoSuchElementException(e);
                ServletUtils.sendError(error, resp);
            } catch (SQLException e) {
                ErrorResponse error = ErrorResponse.constructException(e);
                ServletUtils.sendError(error, resp);
            }
        }
        else {
            IllegalArgumentException e = new IllegalArgumentException("Неверные критерии для поиска");
            ErrorResponse error = ErrorResponse.constructIllegalArgumentException(e);
            ServletUtils.sendError(error, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        UserRequest userRequest = ServletUtils.parseJson(req, resp, UserRequest.class);

        try {
            ServletUtils.sendAnswer(userService.save(userRequest), resp);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = ErrorResponse.constructIllegalArgumentException(e);
            ServletUtils.sendError(error, resp);
        }
        catch (SQLException e) {
            ErrorResponse error = ErrorResponse.constructException(e);
            ServletUtils.sendError(error, resp);
        }
    }
}
