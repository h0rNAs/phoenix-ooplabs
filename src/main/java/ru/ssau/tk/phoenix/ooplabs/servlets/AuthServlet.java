package ru.ssau.tk.phoenix.ooplabs.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;
import ru.ssau.tk.phoenix.ooplabs.service.UserApiContract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AuthServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger(AuthServlet.class);
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
        req.getRequestDispatcher("/auth.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            logger.info("Сохранение пользователя в БД");
            UserResponse user = userService.save(new UserRequest(username, password));
            req.setAttribute("success", "Регистрация успешна! ID пользователя: " + user.getId());

            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            logger.info("Перенаправление на страницу профиля");
            resp.sendRedirect("profile");
        } catch (IllegalArgumentException e) {
            try {
                logger.info("Такой польователь уже существует. Выполняется вход");
                if (userService.authenticate(username, password)){
                    UserResponse user = userService.find(username);

                    HttpSession session = req.getSession();
                    session.setAttribute("user", user);

                    logger.info("Вход выполнен. Перенаправление на страницу профиля");
                    resp.sendRedirect("profile");
                }
                else {
                    // Неверный пороль
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        catch (SQLException e) {
            req.setAttribute("error", "Ошибка регистрации: " + e.getMessage());
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}
