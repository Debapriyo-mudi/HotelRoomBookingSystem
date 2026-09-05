package com.hotel.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cancelBooking")
public class CancelBookingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Integer userId =
                (Integer) session.getAttribute("user_id");

        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        String bookingText =
                request.getParameter("booking_id");

        if (bookingText == null || bookingText.trim().isEmpty()) {
            response.sendRedirect("mybookings");
            return;
        }

        try {

            int bookingId =
                    Integer.parseInt(bookingText);

            String sql =
                    "UPDATE bookings " +
                    "SET booking_status = 'Cancelled' " +
                    "WHERE booking_id = ? " +
                    "AND user_id = ? " +
                    "AND booking_status <> 'Cancelled'";

            try (Connection con =
                         DBConnection.getConnection();
                 PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setInt(1, bookingId);
                ps.setInt(2, userId);

                int result =
                        ps.executeUpdate();

                response.setContentType(
                        "text/html;charset=UTF-8");

                if (result > 0) {

                    response.getWriter().println(
                        "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<title>Booking Cancelled</title>" +

                        "<style>" +

                        "*{box-sizing:border-box}" +

                        "body{" +
                        "margin:0;" +
                        "font-family:Arial,sans-serif;" +
                        "min-height:100vh;" +
                        "display:flex;" +
                        "align-items:center;" +
                        "justify-content:center;" +
                        "background:linear-gradient(135deg,#07111f,#12355b);" +
                        "padding:20px;" +
                        "}" +

                        ".box{" +
                        "background:white;" +
                        "width:100%;" +
                        "max-width:520px;" +
                        "padding:45px;" +
                        "border-radius:20px;" +
                        "text-align:center;" +
                        "box-shadow:0 20px 60px rgba(0,0,0,.35);" +
                        "}" +

                        ".icon{" +
                        "font-size:55px;" +
                        "margin-bottom:15px;" +
                        "}" +

                        "h1{" +
                        "color:#12355b;" +
                        "margin-bottom:12px;" +
                        "}" +

                        "p{" +
                        "color:#667085;" +
                        "font-size:16px;" +
                        "line-height:1.6;" +
                        "}" +

                        ".btn{" +
                        "display:inline-block;" +
                        "margin-top:20px;" +
                        "padding:13px 24px;" +
                        "border-radius:9px;" +
                        "background:#12355b;" +
                        "color:white;" +
                        "text-decoration:none;" +
                        "font-weight:bold;" +
                        "}" +

                        ".btn:hover{" +
                        "background:#0b243f;" +
                        "}" +

                        "</style>" +
                        "</head>" +

                        "<body>" +

                        "<div class='box'>" +

                        "<div class='icon'>✅</div>" +

                        "<h1>Booking Cancelled</h1>" +

                        "<p>Your booking has been successfully cancelled.</p>" +

                        "<p><strong>Booking ID:</strong> " +
                        bookingId +
                        "</p>" +

                        "<a class='btn' href='mybookings'>" +
                        "View My Bookings" +
                        "</a>" +

                        "<br>" +

                        "<a class='btn' href='index.html'>" +
                        "Back to Home" +
                        "</a>" +

                        "</div>" +

                        "</body>" +
                        "</html>"
                    );

                } else {

                    response.getWriter().println(
                        "<h2>Unable to cancel booking.</h2>" +
                        "<p>The booking may already be cancelled.</p>" +
                        "<a href='mybookings'>Back to My Bookings</a>"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<h2>Cancellation Failed</h2>" +
                "<p>Please try again.</p>" +
                "<a href='mybookings'>Back to My Bookings</a>"
            );
        }
    }
}