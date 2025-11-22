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

public class ProfileServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger(ProfileServlet.class);
    private UserApiContract userService;

    @Override
    public void init() throws ServletException {
        userService = DataBaseManager.getUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponse user = (UserResponse) req.getAttribute("profileUser");
        if (user == null) user = (UserResponse) req.getSession().getAttribute("user");

        if (user != null) {
            logger.info("Пользователь id={} получен. Открытие профиля...", user.getId());
            req.setAttribute("profileUser", user);
            req.getRequestDispatcher("/profile/profile.jsp").forward(req, resp);
        }
        else {
            logger.error("Пользователь не найден");
        }
    }
}
