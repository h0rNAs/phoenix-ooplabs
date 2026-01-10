package ru.ssau.tk.phoenix.ooplabs.web.components;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ssau.tk.phoenix.ooplabs.entities.PageView;
import ru.ssau.tk.phoenix.ooplabs.repositories.PageViewRepository;

import java.io.IOException;
import java.util.UUID;

@Component
public class PageViewLoggingFilter extends OncePerRequestFilter {
    private final PageViewRepository pageViewRepository;

    public PageViewLoggingFilter(PageViewRepository pageViewRepository) {
        this.pageViewRepository = pageViewRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Создать или получить session_id
        HttpSession session = request.getSession(true);
        String sessionIdStr = session.getId();
        if (sessionIdStr.length() != 32 || !sessionIdStr.matches("[0-9A-Fa-f]{32}")) {
            // Обработать ошибку: например, сгенерировать новый UUID или залогировать
            throw new IllegalArgumentException("Неверный формат ID сессии: " + sessionIdStr);
        }
        String formattedSessionId = sessionIdStr.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{12})",
                "$1-$2-$3-$4-$5");
        UUID sessionId = UUID.fromString(formattedSessionId);

        // Пропустить фильтр для определенных путей (например, API, static, assets)
        String uri = request.getRequestURI();
        if (!"/mathhub/index.jsp".equals(uri) || pageViewRepository.existsBySessionIdAndUrl(sessionId, uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        PageView pageView = new PageView();
        pageView.setUrl(uri);
        pageView.setSessionId(sessionId);
        // userId: Если есть аутентификация, возьмите из SecurityContextHolder.getContext().getAuthentication()
        // pageView.setUserId(...);
        pageView.setIpAddress(request.getRemoteAddr());
        pageView.setDeviceType(detectDeviceType(request.getHeader("User-Agent")));

        // Асинхронно сохранить (чтобы не блокировать)
        saveAsync(pageView);

        // Продолжить цепочку фильтров
        filterChain.doFilter(request, response);
    }

    @Async
    protected void saveAsync(PageView pageView) {
        // В реальности аннотируйте @Async, но для примера просто save
        pageViewRepository.save(pageView);
    }

    // Простой метод для определения типа устройства (расширьте по необходимости)
    private String detectDeviceType(String userAgent) {
        if (userAgent == null) return "unknown";
        if (userAgent.toLowerCase().contains("mobile")) return "mobile";
        return "desktop";
    }
}
