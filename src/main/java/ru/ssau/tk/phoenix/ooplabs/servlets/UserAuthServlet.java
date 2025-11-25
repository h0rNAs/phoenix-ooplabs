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
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class UserAuthServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger(UserAuthServlet.class);
    private UserApiContract userService;

    @Override
    public void init() {
        userService = DataBaseManager.getUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (password == null) password = "";

        if (username != null && !username.isEmpty()){
            try {
                ServletUtils.sendAnswer(userService.auth(username, password), resp);
            } catch (NoSuchElementException e){
                ErrorResponse error = ErrorResponse.constructNoSuchElementException(e);
                ServletUtils.sendError(error, resp);
            }
            catch (SQLException e) {
                ErrorResponse error = ErrorResponse.constructException(e);
                ServletUtils.sendError(error, resp);
            }
        }
        else {
            IllegalArgumentException e = new IllegalArgumentException("username не указан");
            ErrorResponse error = ErrorResponse.constructIllegalArgumentException(e);
            ServletUtils.sendError(error, resp);
        }
    }
}
