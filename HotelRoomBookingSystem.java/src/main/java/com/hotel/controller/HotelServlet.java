package com.hotel.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hotels")
public class HotelServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");

        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>");

        out.println("<title>Hotels - Grand Stay</title>");

        out.println("<style>");

        out.println("* {");
        out.println("    margin: 0;");
        out.println("    padding: 0;");
        out.println("    box-sizing: border-box;");
        out.println("    font-family: Arial, sans-serif;");
        out.println("}");

        out.println("body {");
        out.println("    background: #f5f7fa;");
        out.println("    color: #333;");
        out.println("}");

        /* NAVBAR */

        out.println(".navbar {");
        out.println("    background: #111827;");
        out.println("    color: white;");
        out.println("    padding: 18px 7%;");
        out.println("    display: flex;");
        out.println("    justify-content: space-between;");
        out.println("    align-items: center;");
        out.println("}");

        out.println(".logo {");
        out.println("    color: #f5c542;");
        out.println("    font-size: 24px;");
        out.println("    font-weight: bold;");
        out.println("}");

        out.println(".nav-links {");
        out.println("    display: flex;");
        out.println("    gap: 25px;");
        out.println("}");

        out.println(".nav-links a {");
        out.println("    color: white;");
        out.println("    text-decoration: none;");
        out.println("}");

        out.println(".nav-links a:hover {");
        out.println("    color: #f5c542;");
        out.println("}");

        /* HERO */

        out.println(".hero {");
        out.println("    background: #1f2937;");
        out.println("    color: white;");
        out.println("    text-align: center;");
        out.println("    padding: 60px 20px;");
        out.println("}");

        out.println(".hero h1 {");
        out.println("    font-size: 40px;");
        out.println("    margin-bottom: 12px;");
        out.println("}");

        out.println(".hero p {");
        out.println("    color: #d1d5db;");
        out.println("    font-size: 18px;");
        out.println("}");

        /* HOTEL SECTION */

        out.println(".container {");
        out.println("    width: 86%;");
        out.println("    max-width: 1200px;");
        out.println("    margin: 45px auto;");
        out.println("}");

        out.println(".hotel-grid {");
        out.println("    display: grid;");
        out.println("    grid-template-columns: repeat(3, 1fr);");
        out.println("    gap: 28px;");
        out.println("}");

        /* HOTEL CARD */

        out.println(".hotel-card {");
        out.println("    background: white;");
        out.println("    border-radius: 14px;");
        out.println("    overflow: hidden;");
        out.println("    box-shadow: 0 6px 20px rgba(0,0,0,0.10);");
        out.println("    transition: 0.25s;");
        out.println("}");

        out.println(".hotel-card:hover {");
        out.println("    transform: translateY(-6px);");
        out.println("    box-shadow: 0 12px 30px rgba(0,0,0,0.16);");
        out.println("}");

        out.println(".hotel-image {");
        out.println("    width: 100%;");
        out.println("    height: 210px;");
        out.println("    object-fit: cover;");
        out.println("}");

        out.println(".hotel-content {");
        out.println("    padding: 20px;");
        out.println("}");

        out.println(".hotel-name {");
        out.println("    font-size: 22px;");
        out.println("    font-weight: bold;");
        out.println("    margin-bottom: 8px;");
        out.println("    color: #111827;");
        out.println("}");

        out.println(".location {");
        out.println("    color: #6b7280;");
        out.println("    margin-bottom: 12px;");
        out.println("}");

        out.println(".description {");
        out.println("    color: #555;");
        out.println("    line-height: 1.5;");
        out.println("    margin-bottom: 15px;");
        out.println("}");

        out.println(".rating {");
        out.println("    color: #d97706;");
        out.println("    font-weight: bold;");
        out.println("    margin-bottom: 18px;");
        out.println("}");

        out.println(".view-btn {");
        out.println("    display: block;");
        out.println("    text-align: center;");
        out.println("    background: #f5c542;");
        out.println("    color: #111827;");
        out.println("    padding: 12px;");
        out.println("    text-decoration: none;");
        out.println("    border-radius: 7px;");
        out.println("    font-weight: bold;");
        out.println("}");

        out.println(".view-btn:hover {");
        out.println("    background: #e5b52f;");
        out.println("}");

        /* RESPONSIVE */

        out.println("@media(max-width: 900px) {");

        out.println("    .hotel-grid {");
        out.println("        grid-template-columns: repeat(2, 1fr);");
        out.println("    }");

        out.println("}");

        out.println("@media(max-width: 600px) {");

        out.println("    .navbar {");
        out.println("        flex-direction: column;");
        out.println("        gap: 15px;");
        out.println("    }");

        out.println("    .hotel-grid {");
        out.println("        grid-template-columns: 1fr;");
        out.println("    }");

        out.println("}");

        out.println("</style>");

        out.println("</head>");
        out.println("<body>");

        /* NAVBAR */

        out.println("<nav class='navbar'>");

        out.println("<div class='logo'>Grand Stay</div>");

        out.println("<div class='nav-links'>");

        out.println("<a href='index.html'>Home</a>");
        out.println("<a href='hotels'>Hotels</a>");
        out.println("<a href='rooms'>Rooms</a>");
        out.println("<a href='mybookings'>My Bookings</a>");
        out.println("<a href='login.html'>Login</a>");

        out.println("</div>");
        out.println("</nav>");

        /* HERO */

        out.println("<section class='hero'>");

        out.println("<h1>Find Your Perfect Hotel</h1>");

        out.println("<p>");
        out.println("Explore comfortable stays across India.");
        out.println("</p>");

        out.println("</section>");

        /* HOTELS */

        out.println("<div class='container'>");

        out.println("<div class='hotel-grid'>");

        String sql =
                "SELECT * FROM hotels ORDER BY location, hotel_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int hotelId = rs.getInt("hotel_id");

                String hotelName =
                        rs.getString("hotel_name");

                String location =
                        rs.getString("location");

                String address =
                        rs.getString("address");

                String description =
                        rs.getString("description");

                double rating =
                        rs.getDouble("rating");

                String imageUrl =
                        rs.getString("image_url");

                out.println("<div class='hotel-card'>");

                out.println("<img class='hotel-image' "
                        + "src='" + imageUrl + "' "
                        + "alt='" + hotelName + "'>");

                out.println("<div class='hotel-content'>");

                out.println("<div class='hotel-name'>"
                        + hotelName
                        + "</div>");

                out.println("<div class='location'>");
                out.println("📍 " + location);
                out.println("</div>");

                out.println("<div class='location'>");
                out.println(address);
                out.println("</div>");

                out.println("<div class='description'>");

                if (description != null) {
                    out.println(description);
                }

                out.println("</div>");

                out.println("<div class='rating'>");
                out.println("★ " + rating + " / 5");
                out.println("</div>");

                out.println("<a class='view-btn' "
                        + "href='rooms?hotel_id="
                        + hotelId
                        + "'>"
                        + "View Rooms"
                        + "</a>");

                out.println("</div>");

                out.println("</div>");
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Unable to load hotels.</h2>");
        }

        out.println("</div>");
        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }
}