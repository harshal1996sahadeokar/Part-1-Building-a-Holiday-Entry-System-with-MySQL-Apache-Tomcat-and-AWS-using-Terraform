package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/HolidayFilterServlet")
public class HolidayFilterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String date = request.getParameter("date");
        if (date == null || date.isEmpty()) {
            out.println("<h3>Please provide a date parameter.</h3>");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/holidaysdb", "username", "password")) {
            String query = "SELECT * FROM holidays WHERE date = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, date);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                out.println("<h3>Holiday Found:</h3>");
                out.println("<p>" + resultSet.getString("holiday_name") + " on " + resultSet.getString("date") + "</p>");
            } else {
                out.println("<h3>No holiday found for the given date.</h3>");
            }
        } catch (SQLException e) {
            e.printStackTrace(out);
        }
    }
}

