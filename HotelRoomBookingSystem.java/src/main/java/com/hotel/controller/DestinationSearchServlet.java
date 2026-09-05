package com.hotel.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/searchDestinations")
public class DestinationSearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        String q = request.getParameter("q");

        if (q == null || q.trim().isEmpty()) {
            response.getWriter().print("[]");
            return;
        }

        String sql =
            "SELECT destination_id, name, state " +
            "FROM destinations " +
            "WHERE name LIKE ? " +
            "ORDER BY name LIMIT 10";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + q.trim() + "%");

            ResultSet rs = ps.executeQuery();

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {

                if (!first) {
                    json.append(",");
                }

                json.append("{")
                    .append("\"id\":").append(rs.getInt("destination_id"))
                    .append(",\"name\":\"")
                    .append(escape(rs.getString("name")))
                    .append("\"")
                    .append(",\"state\":\"")
                    .append(escape(rs.getString("state")))
                    .append("\"")
                    .append("}");

                first = false;
            }

            json.append("]");

            response.getWriter().print(json.toString());

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(500);
            response.getWriter().print(
                "{\"error\":\"Database error\"}"
            );
        }
    }

    private String escape(String text) {

        if (text == null) {
            return "";
        }

        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"");
    }
}