package ru.itis.Tyshenko.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ru.itis.Tyshenko.entity.Cottage;
import ru.itis.Tyshenko.repositories.CottageRepositoryImpl;
import ru.itis.Tyshenko.repositories.utility.SimpleDataSource;
import ru.itis.Tyshenko.services.CottageServiceImpl;
import ru.itis.Tyshenko.repositories.*;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(value = "/rest")
public class RestServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private CottageServiceImpl service;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Cottage> cottage = service.get(objectMapper.readValue(req.getReader(), Cottage.class));
        if (cottage.isPresent()) {
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(cottage.get()));
            resp.getWriter().flush();
        }
        else {
            resp.getWriter().println("this cottage didn't find");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        service.add(objectMapper.readValue(req.getReader(), Cottage.class));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        service.update(objectMapper.readValue(req.getReader(), Cottage.class));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        service.delete(objectMapper.readValue(req.getReader(), Cottage.class));
    }

    @Override
    public void init(ServletConfig config) {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        CottageRepository repository = new  CottageRepositoryImpl(SimpleDataSource.openConnection());
        service = new CottageServiceImpl(repository);
    }
}
