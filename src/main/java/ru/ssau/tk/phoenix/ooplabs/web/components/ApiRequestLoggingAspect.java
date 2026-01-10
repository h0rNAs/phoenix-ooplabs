package ru.ssau.tk.phoenix.ooplabs.web.components;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.ssau.tk.phoenix.ooplabs.entities.ApiRequest;
import ru.ssau.tk.phoenix.ooplabs.repositories.ApiRequestRepository;

import java.util.UUID;

/**
 * Аспект для логирования запросов к API.
 * Перехватывает методы в контроллерах, помеченных @RestController или с @RequestMapping.
 * Измеряет время выполнения и сохраняет в БД асинхронно.
 */
@Aspect
@Component
public class ApiRequestLoggingAspect {
    private ApiRequestRepository apiRequestRepository;

    public ApiRequestLoggingAspect(ApiRequestRepository apiRequestRepository) {
        this.apiRequestRepository = apiRequestRepository;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || " +
            "within(@org.springframework.stereotype.Controller *)")
    public void apiMethods() {}

    @Around("apiMethods()")
    public Object logApiRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        // Получить request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();  // Не API-запрос
        }
        HttpServletRequest request = attributes.getRequest();

        // Измерить время
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long responseTimeMs = System.currentTimeMillis() - startTime;

        // Получить статус (если ResponseEntity, иначе предположим 200)
        int statusCode = 200;
        if (result instanceof ResponseEntity<?>) {
            statusCode = ((ResponseEntity<?>) result).getStatusCode().value();
        }

        // Получить sessionId (с форматированием, как раньше)
        HttpSession session = request.getSession(false);  // false, чтобы не создавать новую
        UUID sessionId = null;
        if (session != null) {
            String sessionIdStr = session.getId();
            sessionId = formatSessionIdToUUID(sessionIdStr);
        }

        // Создать сущность
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setEndpoint(request.getRequestURI());
        apiRequest.setMethod(request.getMethod());
        apiRequest.setStatusCode(statusCode);
        apiRequest.setResponseTimeMs((int) responseTimeMs);
        // apiRequest.setUserId(...)
        apiRequest.setSessionId(sessionId);

        // Асинхронно сохранить
        saveAsync(apiRequest);

        return result;
    }

    private UUID formatSessionIdToUUID(String sessionIdStr) {
        if (sessionIdStr == null || sessionIdStr.length() != 32) {
            return UUID.randomUUID();  // Фоллбек
        }
        String formatted = sessionIdStr.substring(0, 8) + "-" +
                sessionIdStr.substring(8, 12) + "-" +
                sessionIdStr.substring(12, 16) + "-" +
                sessionIdStr.substring(16, 20) + "-" +
                sessionIdStr.substring(20);
        return UUID.fromString(formatted);
    }

    @Async
    public void saveAsync(ApiRequest apiRequest) {
        apiRequestRepository.save(apiRequest);
    }
}
