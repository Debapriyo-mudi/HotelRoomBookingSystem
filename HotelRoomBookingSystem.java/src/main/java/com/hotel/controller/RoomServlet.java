package com.hotel.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/rooms")
public class RoomServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String hotelIdText = request.getParameter("hotel_id");
        String checkIn = request.getParameter("check_in");
        String checkOut = request.getParameter("check_out");

        HttpSession session = request.getSession();

        // Get dates from session if they are not in URL
        if (checkIn == null || checkIn.trim().isEmpty()) {
            checkIn = (String) session.getAttribute("check_in");
        }

        if (checkOut == null || checkOut.trim().isEmpty()) {
            checkOut = (String) session.getAttribute("check_out");
        }

        // Dates are still missing
        if (checkIn == null || checkIn.trim().isEmpty()
                || checkOut == null || checkOut.trim().isEmpty()) {

            out.println("<h2>Booking dates are missing.</h2>");
            out.println("<a href='index.html'>Back to Search</a>");
            return;
        }

        // Hotel ID is required
        if (hotelIdText == null || hotelIdText.trim().isEmpty()) {

            out.println("<h2>Hotel is missing.</h2>");
            out.println("<a href='index.html'>Back to Search</a>");
            return;
        }

        int hotelId;

        try {
            hotelId = Integer.parseInt(hotelIdText);
        } catch (NumberFormatException e) {

            out.println("<h2>Invalid hotel.</h2>");
            out.println("<a href='index.html'>Back to Home</a>");
            return;
        }

        /*
         * IMPORTANT:
         * Save the booking information in the session.
         * This keeps the information available after login.
         */

        session.setAttribute("hotel_id", hotelId);
        session.setAttribute("check_in", checkIn);
        session.setAttribute("check_out", checkOut);

        String encodedCheckIn =
                URLEncoder.encode(checkIn, StandardCharsets.UTF_8);

        String encodedCheckOut =
                URLEncoder.encode(checkOut, StandardCharsets.UTF_8);

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println("<meta name='viewport' " +
                "content='width=device-width,initial-scale=1.0'>");

        out.println("<title>StayFinder | Rooms</title>");

        out.println("<style>");

        out.println("*{box-sizing:border-box;margin:0;padding:0}");

        out.println("body{font-family:Arial,sans-serif;" +
                "background:#f5f7fa;color:#172033}");

        out.println(".header{background:linear-gradient(135deg,#12355b,#176b87);" +
                "color:white;padding:40px 7%}");

        out.println(".container{width:90%;max-width:1150px;" +
                "margin:30px auto}");

        out.println(".back{display:inline-block;margin-bottom:25px;" +
                "color:#12355b;text-decoration:none;font-weight:bold}");

        out.println(".info{background:white;padding:20px;" +
                "border-radius:12px;margin-bottom:25px;" +
                "box-shadow:0 4px 15px rgba(0,0,0,.07)}");

        out.println(".grid{display:grid;" +
                "grid-template-columns:repeat(auto-fit,minmax(280px,1fr));" +
                "gap:25px}");

        out.println(".card{background:white;border-radius:15px;" +
                "overflow:hidden;box-shadow:0 6px 20px rgba(0,0,0,.1);" +
                "transition:.25s}");

        out.println(".card:hover{transform:translateY(-5px)}");

        out.println(".image{width:100%;height:210px;object-fit:cover}");

        out.println(".content{padding:22px}");

        out.println(".content h2{color:#12355b;margin-bottom:12px}");

        out.println(".price{font-size:20px;font-weight:bold;" +
                "color:#d97706;margin-top:12px}");

        out.println(".button{display:block;text-align:center;" +
                "margin-top:16px;padding:12px;background:#12355b;" +
                "color:white;text-decoration:none;border-radius:8px;" +
                "font-weight:bold}");

        out.println(".button:hover{background:#0b243f}");

        out.println("</style>");

        out.println("</head>");
        out.println("<body>");

        try (Connection con = DBConnection.getConnection()) {

            String hotelSql =
                    "SELECT hotel_name, location, description, rating " +
                    "FROM hotels WHERE hotel_id = ?";

            try (PreparedStatement ps =
                         con.prepareStatement(hotelSql)) {

                ps.setInt(1, hotelId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {

                        out.println("<h2>Hotel not found.</h2>");
                        out.println(
                                "<a href='index.html'>Back to Home</a>"
                        );

                        return;
                    }

                    String hotelName =
                            rs.getString("hotel_name");

                    String location =
                            rs.getString("location");

                    out.println("<div class='header'>");

                    out.println("<h1>" +
                            hotelName +
                            "</h1>");

                    out.println("<p>📍 " +
                            location +
                            "</p>");

                    out.println("</div>");

                    out.println("<div class='container'>");

                    out.println(
                            "<a class='back' href='index.html'>" +
                            "← Back to Search</a>"
                    );

                    out.println("<div class='info'>");

                    out.println("<h2>Available Rooms</h2>");

                    out.println(
                            "<p>📅 Check-in: <strong>" +
                            checkIn +
                            "</strong></p>"
                    );

                    out.println(
                            "<p>📅 Check-out: <strong>" +
                            checkOut +
                            "</strong></p>"
                    );

                    out.println(
                            "<p>⭐ Rating: " +
                            rs.getBigDecimal("rating") +
                            " / 5</p>"
                    );

                    out.println("</div>");

                    String roomSql =
                            "SELECT room_id, room_number, room_type, " +
                            "price_per_night, capacity " +
                            "FROM rooms " +
                            "WHERE hotel_id = ? " +
                            "AND status = 'Available' " +
                            "ORDER BY room_type, room_number";

                    try (PreparedStatement roomPs =
                                 con.prepareStatement(roomSql)) {

                        roomPs.setInt(1, hotelId);

                        try (ResultSet rooms =
                                     roomPs.executeQuery()) {

                            boolean found = false;

                            out.println("<div class='grid'>");

                            while (rooms.next()) {

                                found = true;

                                String roomType =
                                        rooms.getString("room_type");

                                String image;

                                if ("Single".equalsIgnoreCase(roomType)) {

                                    image =
                                            "images/single-room.jpg";

                                } else if
                                ("Double".equalsIgnoreCase(roomType)) {

                                    image =
                                            "images/double-room.jpg";

                                } else if
                                ("Deluxe".equalsIgnoreCase(roomType)) {

                                    image =
                                            "images/deluxe-room.jpg";

                                } else {

                                    image =
                                            "images/suite-room.jpg";
                                }

                                out.println("<div class='card'>");

                                out.println(
                                        "<img class='image' " +
                                        "src='" + image + "' " +
                                        "alt='Room'>"
                                );

                                out.println("<div class='content'>");

                                out.println(
                                        "<h2>" +
                                        roomType +
                                        " Room</h2>"
                                );

                                out.println(
                                        "<p>🚪 Room Number: " +
                                        rooms.getString("room_number") +
                                        "</p>"
                                );

                                out.println(
                                        "<p>👥 Capacity: " +
                                        rooms.getInt("capacity") +
                                        " person(s)</p>"
                                );

                                out.println(
                                        "<p class='price'>₹" +
                                        rooms.getBigDecimal(
                                                "price_per_night") +
                                        " / night</p>"
                                );

                                /*
                                 * Save room ID in session too.
                                 */

                                int roomId =
                                        rooms.getInt("room_id");

                                session.setAttribute(
                                        "room_id",
                                        String.valueOf(roomId)
                                );

                                out.println(
                                        "<a class='button' href=" +
                                        "'booking.html?" +
                                        "room_id=" + roomId +
                                        "&check_in=" +
                                        encodedCheckIn +
                                        "&check_out=" +
                                        encodedCheckOut +
                                        "'>" +
                                        "Book Now →</a>"
                                );

                                out.println("</div>");
                                out.println("</div>");
                            }

                            out.println("</div>");

                            if (!found) {

                                out.println("<div class='info'>");

                                out.println(
                                        "<h2>No rooms available</h2>"
                                );

                                out.println(
                                        "<p>Please choose another hotel.</p>"
                                );

                                out.println("</div>");
                            }
                        }
                    }

                    out.println("</div>");
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<div class='container'>");

            out.println(
                    "<h2>Unable to load rooms.</h2>"
            );

            out.println(
                    "<p>Please try again later.</p>"
            );

            out.println("</div>");
        }

        out.println("</body>");
        out.println("</html>");
    }
}