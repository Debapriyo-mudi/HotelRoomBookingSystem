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

@WebServlet("/searchHotels")
public class HotelSearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String location = request.getParameter("location");
        String checkIn = request.getParameter("check_in");
        String checkOut = request.getParameter("check_out");

        String adultsText = request.getParameter("adults");
        String childrenText = request.getParameter("children");

        if (adultsText == null || adultsText.isEmpty())
            adultsText = "2";

        if (childrenText == null || childrenText.isEmpty())
            childrenText = "0";

        PrintWriter out = response.getWriter();

        if (location == null || location.trim().isEmpty()
                || checkIn == null || checkIn.trim().isEmpty()
                || checkOut == null || checkOut.trim().isEmpty()) {

            out.println("<h2>Search information is missing.</h2>");
            out.println("<a href='index.html'>Back to Search</a>");
            return;
        }

        String in =
            URLEncoder.encode(checkIn, StandardCharsets.UTF_8);

        String outDate =
            URLEncoder.encode(checkOut, StandardCharsets.UTF_8);

        String adults =
            URLEncoder.encode(adultsText, StandardCharsets.UTF_8);

        String children =
            URLEncoder.encode(childrenText, StandardCharsets.UTF_8);

        out.println("<!DOCTYPE html><html><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width,initial-scale=1.0'>");
        out.println("<title>StayFinder | Hotels</title>");

        out.println("<style>");
        out.println("*{box-sizing:border-box;margin:0;padding:0}");
        out.println("body{font-family:Arial,sans-serif;background:#f5f7fa;color:#172033}");
        out.println(".header{background:linear-gradient(135deg,#081c2c,#12355b,#176b87);color:white;padding:45px 7%}");
        out.println(".header h1{margin-bottom:10px}");
        out.println(".container{width:90%;max-width:1150px;margin:35px auto}");
        out.println(".back{display:inline-block;margin-bottom:25px;color:#12355b;text-decoration:none;font-weight:bold}");
        out.println(".info{background:white;padding:20px;border-radius:14px;margin-bottom:25px;box-shadow:0 5px 18px rgba(0,0,0,.08)}");
        out.println(".info p{margin:7px 0}");
        out.println(".grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(280px,1fr));gap:25px}");
        out.println(".card{background:white;border-radius:16px;overflow:hidden;box-shadow:0 7px 22px rgba(0,0,0,.1);transition:.25s}");
        out.println(".card:hover{transform:translateY(-5px)}");
        out.println(".image{width:100%;height:210px;object-fit:cover}");
        out.println(".content{padding:22px}");
        out.println(".content h2{color:#12355b;margin-bottom:10px}");
        out.println(".location{color:#667085;margin-bottom:10px}");
        out.println(".rating{color:#d97706;font-weight:bold;margin-bottom:10px}");
        out.println(".button{display:block;text-align:center;margin-top:17px;padding:13px;background:#12355b;color:white;text-decoration:none;border-radius:9px;font-weight:bold}");
        out.println(".button:hover{background:#0b243f}");
        out.println("</style></head><body>");

        out.println("<div class='header'>");
        out.println("<h1>Hotels in " + location + "</h1>");
        out.println("<p>Find your perfect stay for your selected dates.</p>");
        out.println("</div>");

        out.println("<div class='container'>");

        out.println("<a class='back' href='index.html'>← Search Again</a>");

        out.println("<div class='info'>");
        out.println("<p>📍 <strong>Destination:</strong> " + location + "</p>");
        out.println("<p>📅 <strong>Check-in:</strong> " + checkIn + "</p>");
        out.println("<p>📅 <strong>Check-out:</strong> " + checkOut + "</p>");
        out.println("<p>👨‍👩‍👧 <strong>Guests:</strong> "
                + adultsText + " Adult(s), "
                + childrenText + " Child(ren)</p>");
        out.println("</div>");

        String sql =
            "SELECT hotel_id, hotel_name, location, description, rating, image_url " +
            "FROM hotels WHERE location LIKE ? ORDER BY rating DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + location.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                out.println("<div class='grid'>");

                while (rs.next()) {

                    found = true;

                    String image = rs.getString("image_url");

                    if (image == null || image.trim().isEmpty())
                        image = "images/hotel-default.jpg";

                    out.println("<div class='card'>");

                    out.println("<img class='image' src='" +
                            image + "' alt='Hotel'>");

                    out.println("<div class='content'>");

                    out.println("<h2>" +
                            rs.getString("hotel_name") +
                            "</h2>");

                    out.println("<p class='location'>📍 " +
                            rs.getString("location") +
                            "</p>");

                    out.println("<p class='rating'>⭐ " +
                            rs.getBigDecimal("rating") +
                            " / 5</p>");

                    String description =
                            rs.getString("description");

                    if (description != null)
                        out.println("<p>" + description + "</p>");

                    out.println(
                        "<a class='button' href='rooms?hotel_id=" +
                        rs.getInt("hotel_id") +
                        "&check_in=" + in +
                        "&check_out=" + outDate +
                        "&adults=" + adults +
                        "&children=" + children +
                        "'>View Rooms →</a>"
                    );

                    out.println("</div></div>");
                }

                out.println("</div>");

                if (!found) {
                    out.println("<div class='info'>");
                    out.println("<h2>No hotels found.</h2>");
                    out.println("<p>Try Kolkata, Goa, Jaipur or Mumbai.</p>");
                    out.println("</div>");
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<div class='info'>");
            out.println("<h2>Unable to search hotels.</h2>");
            out.println("<p>Please check your database connection.</p>");
            out.println("</div>");
        }

        out.println("</div></body></html>");
    }
}