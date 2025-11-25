package ru.ssau.tk.phoenix.ooplabs.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.ErrorResponse;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.service.FunctionApiContract;
import ru.ssau.tk.phoenix.ooplabs.service.UserApiContract;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class FunctionServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger();
    private FunctionApiContract functionService;

    @Override
    public void init() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            functionService = DataBaseManager.getFunctionService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String userId = req.getParameter("userId");
        Criteria[] filter = ServletUtils.parseJson(req, resp, Criteria[].class, false);

        if (userId != null && !userId.isEmpty()) {
            try {
                ServletUtils.sendAnswer(
                        functionService.findByUserId(Long.valueOf(userId)), resp);
            } catch (NumberFormatException e) {
                ErrorResponse error = ErrorResponse.constructIllegalArgumentException(e);
                ServletUtils.sendError(error, resp);
            } catch (NoSuchElementException e) {
                ErrorResponse error = ErrorResponse.constructNoSuchElementException(e);
                ServletUtils.sendError(error, resp);
            } catch (SQLException e) {
                ErrorResponse error = ErrorResponse.constructException(e);
                ServletUtils.sendError(error, resp);
            }
        } else if (filter != null && filter.length > 0) {
            try {
                ServletUtils.sendAnswer(
                        functionService.findWithFilter(List.of(filter)), resp);
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

        FunctionRequest func = ServletUtils.parseJson(req, resp, FunctionRequest.class);

        try {
            ServletUtils.sendAnswer(functionService.save(func), resp);
        }
        catch (SQLException e) {
            ErrorResponse error = ErrorResponse.constructException(e);
            ServletUtils.sendError(error, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        FunctionResponse func = ServletUtils.parseJson(req, resp, FunctionResponse.class);

        try {
            ServletUtils.sendAnswer(functionService.update(func), resp);
        } catch (NoSuchElementException e) {
            ErrorResponse error = ErrorResponse.constructNoSuchElementException(e);
            ServletUtils.sendError(error, resp);
        }
        catch (SQLException e) {
            ErrorResponse error = ErrorResponse.constructException(e);
            ServletUtils.sendError(error, resp);
        }
    }
}
