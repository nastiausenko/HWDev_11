package org.example;

import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        ServletContext context = getServletContext();
        String prefix = context.getRealPath("/WEB-INF/templates/");

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(prefix);
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        Context context = new Context(
                req.getLocale(),
                Map.of("time", parseTime(req))
        );

        engine.process("index", context, resp.getWriter());
        resp.getWriter().close();
    }
    private String parseTime(HttpServletRequest req) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        ZonedDateTime zonedDateTime;

        if (req.getParameterMap().containsKey("timezone")) {
            String timezoneParam = req.getParameter("timezone").replace(" ", "+");
            zonedDateTime = ZonedDateTime.now(ZoneId.of(timezoneParam));
        } else {
            zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        }

        return zonedDateTime.format(dateFormat);
    }
}