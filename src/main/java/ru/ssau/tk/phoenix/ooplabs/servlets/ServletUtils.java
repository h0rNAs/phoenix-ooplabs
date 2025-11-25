package ru.ssau.tk.phoenix.ooplabs.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ssau.tk.phoenix.ooplabs.dto.ErrorResponse;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class ServletUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    public static void sendError(ErrorResponse error, HttpServletResponse resp) throws IOException {
        resp.setStatus(error.getCode());
        try {
            String json = mapper.writeValueAsString(error);
            resp.setStatus(error.getCode());
            resp.getWriter().write(json);
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static void sendAnswer(Object answer, HttpServletResponse resp) throws IOException {
        try {
            String json = mapper.writeValueAsString(answer);
            resp.getWriter().write(json);
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static Long parsePathToId(String path, HttpServletResponse resp) throws IOException {
        if (path == null || path.equals("/")) {
            IllegalArgumentException e = new IllegalArgumentException("Неправильный путь");
            sendError(ErrorResponse.constructIllegalArgumentException(e), resp);
            return -1L;
        }

        String[] pathParts = path.split("/");
        if (pathParts.length < 2) {
            IllegalArgumentException e = new IllegalArgumentException("ID не указан");
            sendError(ErrorResponse.constructIllegalArgumentException(e), resp);
            return -1L;
        }

        String userId = pathParts[1];
        if (!userId.matches("\\d+")) {
            IllegalArgumentException e = new IllegalArgumentException("ID не валиден");
            sendError(ErrorResponse.constructIllegalArgumentException(e), resp);
            return -1L;
        }

        return Long.valueOf(userId);
    }

    public static <T> T parseJson(HttpServletRequest req, HttpServletResponse resp, Class<T> valueType, boolean jsonRequired) throws IOException {
        String contentType = req.getContentType();
        if (contentType == null || !contentType.contains("application/json")) {
            if (jsonRequired)
                resp.sendError(415, "Unsupported Media Type: expected application/json");
            return null;
        }

        return mapper.readValue(
                req.getInputStream(),
                valueType
        );
    }

    public static <T> T parseJson(HttpServletRequest req, HttpServletResponse resp, Class<T> valueType) throws IOException {
        return parseJson(req, resp, valueType, true);
    }
}
