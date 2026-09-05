package com.hotel.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("user_id") == null) {

            response.getWriter().print(
                "{\"loggedIn\":false}"
            );

            return;
        }

        Integer userId =
            (Integer) session.getAttribute("user_id");

        String fullName =
            (String) session.getAttribute("full_name");

        String email =
            (String) session.getAttribute("email");

        if (fullName == null) {
            fullName = "";
        }

        if (email == null) {
            email = "";
        }

        // Basic JSON-safe replacement
        fullName = fullName
            .replace("\\", "\\\\")
            .replace("\"", "\\\"");

        email = email
            .replace("\\", "\\\\")
            .replace("\"", "\\\"");

        String json =
            "{"
            + "\"loggedIn\":true,"
            + "\"userId\":" + userId + ","
            + "\"fullName\":\"" + fullName + "\","
            + "\"email\":\"" + email + "\""
            + "}";

        response.getWriter().print(json);
    }
}