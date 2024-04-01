package org.example;

import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().write(parseTime(req));
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

        String formattedDate = zonedDateTime.format(dateFormat);

        return "<html>\n" +
               "<head><title>Current Time</title></head>\n" +
               "<body>\n" +
               "<h1>Current Time</h1>\n" +
               "<p>" + formattedDate + "</p>\n" +
               "</body></html>";
    }
}