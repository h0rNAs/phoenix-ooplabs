package ru.ssau.tk.phoenix.ooplabs.web.components;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

@Component
public class ApiBaseInitializer implements ServletContextInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 1) сначала из переменной окружения
        String apiBase = System.getenv("API_BASE");
        // 2) затем из context-params (опционально, если есть web.xml)
        if (apiBase == null || apiBase.isBlank()) {
            apiBase = servletContext.getInitParameter("API_BASE");
        }
        // 3) fallback для разработки
        if (apiBase == null || apiBase.isBlank()) {
            apiBase = "http://localhost:8080";
        }
        servletContext.setAttribute("apiBase", apiBase);
    }
}
