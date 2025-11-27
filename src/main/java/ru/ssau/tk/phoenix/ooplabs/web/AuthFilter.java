package ru.ssau.tk.phoenix.ooplabs.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.service.UserApiContract;
import ru.ssau.tk.phoenix.ooplabs.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@WebFilter("/api/*")
public class AuthFilter implements Filter {
    private final Map<String, String> ADMIN_USERS = new HashMap<>();
    private UserApiContract userService;
    private final Logger logger = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        logger.info("Инициализация фильтра");
        userService = DataBaseManager.getUserService();
        ADMIN_USERS.put(DataBaseManager.getProperty("", "admin.username"),
                DataBaseManager.getProperty("", "admin.password"));
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        logger.info("Авторизация пользователя");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();
        String method = request.getMethod();

        if (path.startsWith("/api/users") && "POST".equals(method)) {
            chain.doFilter(req, res);
            return;
        }

        boolean isFunctionsPath = path.startsWith("/api/functions");

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            response.setStatus(401);
            response.setHeader("WWW-Authenticate", "Basic realm=\"Protected\"");
            return;
        }

        String[] credentials = new String(Base64.getDecoder().decode(authHeader.substring(6))).split(":");
        if (credentials.length != 2) {
            response.sendError(401);
            return;
        }
        String username = credentials[0];
        String password = credentials[1];

        boolean isAdmin = "admin".equals(authenticateUser(username, password));
        boolean isUser = !isAdmin && "user".equals(authenticateUser(username, password));
        boolean isAuthenticated = isAdmin || isUser;
        logger.info("Пользователь авторизован {}, isAdmin {}, isUser {}", isAuthenticated, isAdmin, isUser);

        if (isFunctionsPath) {
            if (!isAuthenticated) {
                response.sendError(401);
                return;
            }
            chain.doFilter(req, res);
            return;
        }

        if (path.startsWith("/api/users")) {
            if (!isAuthenticated) {
                response.sendError(401);
                return;
            }
            if ("DELETE".equals(method)) {
                if (!isAdmin) {
                    response.sendError(403);
                    return;
                }
            }
            chain.doFilter(req, res);
            return;
        }

        chain.doFilter(req, res);
    }

    private String authenticateUser(String username, String password) {
        // Сначала проверяем админов
        if (ADMIN_USERS.containsKey(username) && ADMIN_USERS.get(username).equals(password)) {
            return "admin";
        }

        // Затем проверяем обычных пользователей в БД
        try {
            if (userService != null && userService.auth(username, password)) {
                return "user";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
