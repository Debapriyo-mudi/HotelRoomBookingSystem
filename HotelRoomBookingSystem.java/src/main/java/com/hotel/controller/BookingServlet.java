package com.hotel.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String roomText = request.getParameter("room_id");
        String checkInText = request.getParameter("check_in");
        String checkOutText = request.getParameter("check_out");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("login.html");
            return;
        }

        Integer userId = (Integer) session.getAttribute("user_id");

        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Check whether booking information was submitted
        if (roomText == null ||
            checkInText == null ||
            checkOutText == null ||
            roomText.trim().isEmpty() ||
            checkInText.trim().isEmpty() ||
            checkOutText.trim().isEmpty()) {

            response.getWriter().println(
                "<!DOCTYPE html>" +
                "<html><head><title>Booking Error</title></head>" +
                "<body style='font-family:Arial;text-align:center;padding:60px'>" +
                "<h2>Booking information is missing.</h2>" +
                "<a href='index.html'>Back to Home</a>" +
                "</body></html>"
            );

            return;
        }

        try {

            int roomId = Integer.parseInt(roomText);

            LocalDate checkIn = LocalDate.parse(checkInText);
            LocalDate checkOut = LocalDate.parse(checkOutText);

            // Check-out must be after check-in
            if (!checkOut.isAfter(checkIn)) {

                response.getWriter().println(
                    "<!DOCTYPE html>" +
                    "<html><head><title>Invalid Dates</title></head>" +
                    "<body style='font-family:Arial;text-align:center;padding:60px'>" +
                    "<h2>Invalid dates.</h2>" +
                    "<p>Check-out must be after check-in.</p>" +
                    "<a href='index.html'>Back to Home</a>" +
                    "</body></html>"
                );

                return;
            }

            // Calculate number of nights
            long nights =
                    ChronoUnit.DAYS.between(checkIn, checkOut);

            if (nights <= 0) {

                response.getWriter().println(
                    "<h2>Invalid number of nights.</h2>" +
                    "<a href='index.html'>Back to Home</a>"
                );

                return;
            }

            try (Connection con = DBConnection.getConnection()) {

                /*
                 * STEP 1:
                 * Get room price and make sure the room is available.
                 */
                String roomSql =
                    "SELECT price_per_night " +
                    "FROM rooms " +
                    "WHERE room_id = ? " +
                    "AND status = 'Available'";

                BigDecimal price;

                try (PreparedStatement ps =
                         con.prepareStatement(roomSql)) {

                    ps.setInt(1, roomId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {

                            response.getWriter().println(
                                "<!DOCTYPE html>" +
                                "<html><head><title>Room Not Available</title></head>" +
                                "<body style='font-family:Arial;text-align:center;padding:60px'>" +
                                "<h2>Room is not available.</h2>" +
                                "<p>Please choose another room.</p>" +
                                "<a href='index.html'>Search Again</a>" +
                                "</body></html>"
                            );

                            return;
                        }

                        price =
                            rs.getBigDecimal("price_per_night");
                    }
                }

                /*
                 * STEP 2:
                 * Check whether the room is already booked
                 * for the selected dates.
                 */
                String availabilitySql =
                    "SELECT COUNT(*) " +
                    "FROM bookings " +
                    "WHERE room_id = ? " +
                    "AND booking_status <> 'Cancelled' " +
                    "AND check_in < ? " +
                    "AND check_out > ?";

                try (PreparedStatement ps =
                         con.prepareStatement(availabilitySql)) {

                    ps.setInt(1, roomId);
                    ps.setString(2, checkOutText);
                    ps.setString(3, checkInText);

                    try (ResultSet rs = ps.executeQuery()) {

                        rs.next();

                        int existingBookings = rs.getInt(1);

                        if (existingBookings > 0) {

                            response.getWriter().println(
                                "<!DOCTYPE html>" +
                                "<html><head><title>Room Not Available</title></head>" +
                                "<body style='font-family:Arial;text-align:center;padding:60px'>" +
                                "<h2>Room Not Available</h2>" +
                                "<p>This room is already booked for these dates.</p>" +
                                "<a href='index.html'>Search Again</a>" +
                                "</body></html>"
                            );

                            return;
                        }
                    }
                }

                /*
                 * STEP 3:
                 * Calculate total booking amount.
                 *
                 * total_amount = price per night × number of nights
                 */
                BigDecimal totalAmount =
                    price.multiply(
                        BigDecimal.valueOf(nights)
                    );

                /*
                 * STEP 4:
                 * Insert the booking into the CORRECT table:
                 * bookings
                 *
                 * total_amount is explicitly inserted here.
                 */
                String insertSql =
                    "INSERT INTO bookings " +
                    "(user_id, room_id, check_in, check_out, " +
                    "total_amount, booking_status) " +
                    "VALUES (?, ?, ?, ?, ?, 'Confirmed')";

                try (PreparedStatement ps =
                         con.prepareStatement(insertSql)) {

                    ps.setInt(1, userId);
                    ps.setInt(2, roomId);
                    ps.setDate(3,
                        java.sql.Date.valueOf(checkIn));
                    ps.setDate(4,
                        java.sql.Date.valueOf(checkOut));
                    ps.setBigDecimal(5, totalAmount);

                    int result =
                        ps.executeUpdate();

                    if (result > 0) {

                        /*
                         * STEP 5:
                         * Booking successful.
                         */
                        response.getWriter().println(
                            "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "<meta charset='UTF-8'>" +
                            "<meta name='viewport' " +
                            "content='width=device-width,initial-scale=1.0'>" +
                            "<title>Booking Successful</title>" +

                            "<style>" +

                            "body{" +
                            "font-family:Arial,sans-serif;" +
                            "background:#f5f7fa;" +
                            "margin:0;" +
                            "padding:70px 20px;" +
                            "text-align:center;" +
                            "color:#172033;" +
                            "}" +

                            ".box{" +
                            "background:white;" +
                            "max-width:550px;" +
                            "margin:auto;" +
                            "padding:40px;" +
                            "border-radius:18px;" +
                            "box-shadow:0 10px 30px rgba(0,0,0,.12);" +
                            "}" +

                            "h1{" +
                            "color:#16803c;" +
                            "margin-bottom:15px;" +
                            "}" +

                            ".details{" +
                            "background:#f8fafc;" +
                            "padding:20px;" +
                            "border-radius:12px;" +
                            "margin:25px 0;" +
                            "text-align:left;" +
                            "}" +

                            ".details p{" +
                            "margin:10px 0;" +
                            "}" +

                            ".amount{" +
                            "font-size:22px;" +
                            "font-weight:bold;" +
                            "color:#b45309;" +
                            "}" +

                            ".btn{" +
                            "display:inline-block;" +
                            "margin:8px;" +
                            "padding:12px 20px;" +
                            "background:#12355b;" +
                            "color:white;" +
                            "text-decoration:none;" +
                            "border-radius:8px;" +
                            "font-weight:bold;" +
                            "}" +

                            ".btn:hover{" +
                            "background:#176b87;" +
                            "}" +

                            "</style>" +
                            "</head>" +

                            "<body>" +

                            "<div class='box'>" +

                            "<h1>🎉 Booking Confirmed!</h1>" +

                            "<p>Your room has been successfully booked.</p>" +

                            "<div class='details'>" +

                            "<p><strong>Check-in:</strong> " +
                            checkInText +
                            "</p>" +

                            "<p><strong>Check-out:</strong> " +
                            checkOutText +
                            "</p>" +

                            "<p><strong>Nights:</strong> " +
                            nights +
                            "</p>" +

                            "<p class='amount'>" +
                            "Total Amount: ₹" +
                            totalAmount +
                            "</p>" +

                            "</div>" +

                            /*
                             * IMPORTANT:
                             * MyBookingsServlet uses:
                             * @WebServlet("/mybookings")
                             *
                             * Therefore the link must also use:
                             * mybookings
                             */
                            "<a class='btn' href='mybookings'>" +
                            "My Bookings" +
                            "</a>" +

                            "<a class='btn' href='index.html'>" +
                            "Back to Home" +
                            "</a>" +

                            "</div>" +

                            "</body>" +
                            "</html>"
                        );

                    } else {

                        response.getWriter().println(
                            "<!DOCTYPE html>" +
                            "<html><body " +
                            "style='font-family:Arial;text-align:center;padding:60px'>" +
                            "<h2>Booking Failed.</h2>" +
                            "<p>The booking could not be saved.</p>" +
                            "<a href='index.html'>Back to Home</a>" +
                            "</body></html>"
                        );
                    }
                }
            }

        } catch (NumberFormatException e) {

            e.printStackTrace();

            response.getWriter().println(
                "<!DOCTYPE html>" +
                "<html><body " +
                "style='font-family:Arial;text-align:center;padding:60px'>" +
                "<h2>Invalid Room ID.</h2>" +
                "<p>Please select the room again.</p>" +
                "<a href='index.html'>Back to Home</a>" +
                "</body></html>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<!DOCTYPE html>" +
                "<html><body " +
                "style='font-family:Arial;text-align:center;padding:60px'>" +
                "<h2>Booking Failed</h2>" +
                "<p>" +
                "An error occurred while creating your booking." +
                "</p>" +
                "<a href='index.html'>Back to Home</a>" +
                "</body></html>"
            );
        }
    }
}