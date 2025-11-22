package ru.ssau.tk.phoenix.ooplabs.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;
import ru.ssau.tk.phoenix.ooplabs.service.UserApiContract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class SearchUserServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger(SearchUserServlet.class);
    private UserApiContract userService;
    @Override
    public void init() throws ServletException {
        userService = DataBaseManager.getUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String username = req.getParameter("username");

        UserResponse user = null;
        if (id != null && !id.isEmpty()){
            try {
                user = userService.find(Long.valueOf(id));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if (username != null && !username.isEmpty()){
            try {
                user = userService.find(username);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else user = (UserResponse) req.getSession().getAttribute("user");

        if (user != null) {
            req.setAttribute("profileUser", user);
        }
        else {
            logger.error("не удалось найти пользователя id={}, username={}", id, username);
        }
        req.getRequestDispatcher("/profile/profile.jsp").forward(req, resp);
    }
}
