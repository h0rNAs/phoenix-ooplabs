package ru.ssau.tk.phoenix.ooplabs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class FunctionServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger();
    private FunctionApiContract functionService;
    private ObjectMapper mapper = new ObjectMapper();

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
        List<FunctionResponse> functions = new ArrayList<>();

        String id = req.getParameter("id");
        String userId = req.getParameter("userId");

        if (id != null && !id.isEmpty()){
            try {
                functions.add(functionService.find(Long.valueOf(id)));
                logger.info("Функция найдена по id={}", id);
            } catch (NoSuchElementException e) {
                // Функция не найдена
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (userId != null && !userId.isEmpty()) {
            try {
                functions = functionService.findByUserId(Long.valueOf(userId));
                logger.info("Функции найдены по userId={}", userId);
            } catch (NoSuchElementException e) {
                // Функция не найдена
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                functions = getWithFilter(req);
                logger.info("Функции найдены с использование фильтра");
            } catch (NoSuchElementException e) {
                // Функция не найдена
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        mapper.writeValue(resp.getWriter(), functions);
    }

    private List<FunctionResponse> getWithFilter(HttpServletRequest req) throws IOException, SQLException {
        BufferedReader reader = req.getReader();
        StringBuilder jsonBody = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            jsonBody.append(line);
        }

        if (jsonBody.isEmpty())
            throw new NoSuchElementException("Не найдено критериев сортировки");

        ObjectMapper mapper = new ObjectMapper();
        List<Criteria> filter = Arrays.stream(mapper.readValue(jsonBody.toString(), Criteria[].class)).toList();

        return functionService.findWithFilter(filter);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        FunctionRequest functionRequest = (FunctionRequest) req.getAttribute("func");

        if (functionRequest != null) {
            try {
                FunctionResponse func = functionService.save(functionRequest);
                logger.info("Функция id={} сохранена", func.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            //sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Функция не найдена");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String definition = req.getParameter("definition");
        FunctionResponse func = null;

        if (id != null && !id.isEmpty()) {
            try {
                func = functionService.find(Long.valueOf(id));
                func.setDefinition(definition);
                functionService.update(func);
                logger.info("Функция id={} обновлена", id);
            } catch (NoSuchElementException e) {
                // Функция не найдена
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id != null && !id.isEmpty()) {
            try {
                functionService.delete(Long.valueOf(id));
                logger.info("Функция id={} удалена", id);
            } catch (NoSuchElementException e) {
                // Функция не найдена
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendError(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        mapper.writeValue(response.getWriter(), error);
    }
}
