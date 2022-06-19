package ru.itis.tyshenko.config;

import org.springframework.web.servlet.view.AbstractTemplateView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class StatusViewResolver extends AbstractTemplateView {

    private final String startWith = "status/";

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream servletOutputStream = response.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new PrintWriter(servletOutputStream));
        int status = Integer.parseInt(getUrl().substring(startWith.length()));
        writer.write("<h1 align=\"center\"> Oh, I'm sorry bro, but you've got " + status + " status </h1>");
        for (Map.Entry<String, Object> entry: model.entrySet()) {
            writer.write("<p> " + entry.getKey() + " " + entry.getValue() + " </p>");
        }
        writer.flush();
        writer.close();
    }

    @Override
    public boolean checkResource(Locale locale) throws Exception {
        if (Objects.requireNonNull(getUrl()).contains(startWith)) {
            int status;
            try {
                status = Integer.parseInt(getUrl().substring(startWith.length()));
            } catch (NumberFormatException e) {
                throw new Exception(e);
            }
            return status > 99 && status < 600;
        }
        return false;
    }
}
