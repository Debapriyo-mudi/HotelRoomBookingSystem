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
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String roomId = request.getParameter("room_id");
        String checkIn = request.getParameter("check_in");
        String checkOut = request.getParameter("check_out");

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    HttpSession session = request.getSession();

                    session.setAttribute("user_id", rs.getInt("user_id"));
                    session.setAttribute("full_name", rs.getString("full_name"));
                    session.setAttribute("email", rs.getString("email"));

                    if (roomId == null || roomId.isEmpty()) {
                        roomId = (String) session.getAttribute("pending_room_id");
                    }

                    if (checkIn == null || checkIn.isEmpty()) {
                        checkIn = (String) session.getAttribute("pending_check_in");
                    }

                    if (checkOut == null || checkOut.isEmpty()) {
                        checkOut = (String) session.getAttribute("pending_check_out");
                    }

                    if (roomId != null && !roomId.isEmpty()
                            && checkIn != null && !checkIn.isEmpty()
                            && checkOut != null && !checkOut.isEmpty()) {

                        session.removeAttribute("pending_room_id");
                        session.removeAttribute("pending_check_in");
                        session.removeAttribute("pending_check_out");

                        response.sendRedirect(
                            request.getContextPath()
                            + "/booking.html?room_id="
                            + roomId
                            + "&check_in="
                            + checkIn
                            + "&check_out="
                            + checkOut
                        );

                    } else {

                        response.sendRedirect(
                            request.getContextPath() + "/index.jsp"
                        );
                    }

                } else {

                    response.setContentType("text/html;charset=UTF-8");

                    response.getWriter().println(
                        "<h2>Invalid email or password!</h2>" +
                        "<a href='" +
                        request.getContextPath() +
                        "/login.html'>Try Again</a>"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                "<h2>Login failed because of a server error.</h2>" +
                "<a href='" +
                request.getContextPath() +
                "/login.html'>Back to Login</a>"
            );
        }
    }
}