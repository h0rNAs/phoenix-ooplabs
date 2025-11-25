package ru.ssau.tk.phoenix.ooplabs.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.ErrorResponse;
import ru.ssau.tk.phoenix.ooplabs.service.UserApiContract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class UserByIdServlet extends HttpServlet {
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

        Long id = ServletUtils.parsePathToId(req.getPathInfo(), resp);
        if (id == -1L) return;

        try {
            ServletUtils.sendAnswer(userService.find(id), resp);
        } catch (NoSuchElementException e) {
            ErrorResponse error = ErrorResponse.constructNoSuchElementException(e);
            ServletUtils.sendError(error, resp);
        } catch (SQLException e) {
            ErrorResponse error = ErrorResponse.constructException(e);
            ServletUtils.sendError(error, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        Long id = ServletUtils.parsePathToId(req.getPathInfo(), resp);
        String newPassword = req.getParameter("password");

        try {
            ServletUtils.sendAnswer(userService.update(id, newPassword), resp);
        } catch (NoSuchElementException e){
            ErrorResponse error = ErrorResponse.constructNoSuchElementException(e);
            ServletUtils.sendError(error, resp);
        }
        catch (SQLException e) {
            ErrorResponse error = ErrorResponse.constructException(e);
            ServletUtils.sendError(error, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        Long id = ServletUtils.parsePathToId(req.getPathInfo(), resp);
        if (id == -1) return;

        try {
            userService.delete(id);
            resp.setStatus(204);
        } catch (NoSuchElementException e) {
            ErrorResponse error = ErrorResponse.constructNoSuchElementException(e);
            ServletUtils.sendError(error, resp);
        } catch (SQLException e) {
            ErrorResponse error = ErrorResponse.constructException(e);
            ServletUtils.sendError(error, resp);
        }
    }
}
