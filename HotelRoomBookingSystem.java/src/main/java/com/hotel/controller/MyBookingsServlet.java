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

@WebServlet("/myBookings")
public class MyBookingsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        int userId = (Integer) session.getAttribute("user_id");
        String ctx = request.getContextPath();

        String sql =
            "SELECT b.booking_id, r.room_number, r.room_type, " +
            "b.check_in, b.check_out, b.total_amount, " +
            "b.booking_status, b.booking_date " +
            "FROM bookings b " +
            "LEFT JOIN rooms r ON b.room_id = r.room_id " +
            "WHERE b.user_id = ? " +
            "ORDER BY b.booking_date DESC";

        response.setContentType("text/html;charset=UTF-8");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                StringBuilder html = new StringBuilder();

                html.append("<!DOCTYPE html><html lang='en'><head>");
                html.append("<meta charset='UTF-8'>");
                html.append("<meta name='viewport' content='width=device-width,initial-scale=1.0'>");
                html.append("<title>My Bookings | StayFinder</title>");

                html.append("<style>");

                html.append("*{box-sizing:border-box;margin:0;padding:0}");
                html.append("body{font-family:Arial,sans-serif;background:#f4f7fb;color:#172033}");
                html.append("a{text-decoration:none}");

                html.append(".top{background:linear-gradient(135deg,#071b30,#174a78);");
                html.append("padding:22px 7%;color:white;display:flex;");
                html.append("justify-content:space-between;align-items:center}");

                html.append(".logo{font-size:24px;font-weight:bold}");
                html.append(".home{color:white;background:rgba(255,255,255,.12);");
                html.append("padding:10px 18px;border-radius:8px}");

                html.append(".container{width:92%;max-width:1100px;margin:45px auto}");

                html.append(".title{text-align:center;margin-bottom:35px}");
                html.append(".title h1{font-size:38px;color:#102a43;margin-bottom:8px}");
                html.append(".title p{color:#718096}");

                html.append(".booking{background:white;border-radius:18px;");
                html.append("margin-bottom:22px;padding:25px;");
                html.append("box-shadow:0 8px 30px rgba(0,0,0,.08);");
                html.append("border:1px solid #e5eaf0}");

                html.append(".booking-top{display:flex;justify-content:space-between;");
                html.append("align-items:center;margin-bottom:20px}");

                html.append(".booking-id{font-size:20px;font-weight:bold;color:#12355b}");

                html.append(".status{padding:7px 13px;border-radius:20px;");
                html.append("font-size:12px;font-weight:bold;text-transform:uppercase}");

                html.append(".confirmed{background:#dcfce7;color:#166534}");
                html.append(".cancelled{background:#fee2e2;color:#991b1b}");
                html.append(".pending{background:#fef3c7;color:#92400e}");

                html.append(".details{display:grid;grid-template-columns:repeat(4,1fr);");
                html.append("gap:18px;border-top:1px solid #edf0f4;");
                html.append("border-bottom:1px solid #edf0f4;padding:20px 0}");

                html.append(".detail label{display:block;font-size:11px;");
                html.append("color:#718096;text-transform:uppercase;font-weight:bold;");
                html.append("margin-bottom:6px}");

                html.append(".detail span{font-size:15px;font-weight:600;color:#172033}");

                html.append(".booking-bottom{display:flex;justify-content:space-between;");
                html.append("align-items:center;padding-top:18px}");

                html.append(".amount{font-size:20px;font-weight:bold;color:#b96808}");

                html.append(".cancel{border:0;background:#dc2626;color:white;");
                html.append("padding:10px 17px;border-radius:8px;font-weight:bold;");
                html.append("cursor:pointer}");

                html.append(".cancel:hover{background:#b91c1c}");

                html.append(".empty{background:white;border-radius:18px;");
                html.append("padding:65px 25px;text-align:center;");
                html.append("box-shadow:0 8px 30px rgba(0,0,0,.08)}");

                html.append(".empty-icon{font-size:55px;margin-bottom:15px}");
                html.append(".empty h2{font-size:26px;color:#12355b;margin-bottom:10px}");
                html.append(".empty p{color:#718096;margin-bottom:25px}");

                html.append(".find{display:inline-block;background:#174a78;");
                html.append("color:white;padding:12px 24px;border-radius:9px;font-weight:bold}");

                html.append(".back{text-align:center;margin-top:30px}");
                html.append(".back a{color:#174a78;font-weight:bold}");

                html.append("@media(max-width:750px){");
                html.append(".details{grid-template-columns:1fr 1fr}");
                html.append(".booking-top,.booking-bottom{align-items:flex-start;gap:15px}");
                html.append(".top{padding:18px 5%}");
                html.append(".container{width:94%;margin:30px auto}");
                html.append("}");

                html.append("@media(max-width:450px){");
                html.append(".details{grid-template-columns:1fr}");
                html.append(".booking-top{flex-direction:column}");
                html.append(".booking-bottom{flex-direction:column}");
                html.append("}");

                html.append("</style></head><body>");

                /* HEADER */

                html.append("<header class='top'>");
                html.append("<div class='logo'>🏨 StayFinder</div>");
                html.append("<a class='home' href='")
                    .append(ctx)
                    .append("/index.jsp'>Home</a>");
                html.append("</header>");

                /* MAIN */

                html.append("<main class='container'>");

                html.append("<div class='title'>");
                html.append("<h1>My Bookings</h1>");
                html.append("<p>View and manage all your StayFinder reservations</p>");
                html.append("</div>");

                boolean hasBooking = false;

                while (rs.next()) {

                    hasBooking = true;

                    int bookingId = rs.getInt("booking_id");

                    String roomNumber = rs.getString("room_number");
                    String roomType = rs.getString("room_type");
                    String checkIn = rs.getString("check_in");
                    String checkOut = rs.getString("check_out");
                    String amount = rs.getString("total_amount");
                    String status = rs.getString("booking_status");

                    if (roomNumber == null) roomNumber = "N/A";
                    if (roomType == null) roomType = "Standard Room";
                    if (amount == null) amount = "0.00";
                    if (status == null) status = "confirmed";

                    String statusClass = "confirmed";

                    if (status.equalsIgnoreCase("cancelled")) {
                        statusClass = "cancelled";
                    } else if (status.equalsIgnoreCase("pending")) {
                        statusClass = "pending";
                    }

                    html.append("<div class='booking'>");

                    html.append("<div class='booking-top'>");

                    html.append("<div class='booking-id'>");
                    html.append("Booking #").append(bookingId);
                    html.append("</div>");

                    html.append("<div class='status ")
                        .append(statusClass)
                        .append("'>")
                        .append(status)
                        .append("</div>");

                    html.append("</div>");

                    html.append("<div class='details'>");

                    html.append("<div class='detail'>");
                    html.append("<label>Room</label>");
                    html.append("<span>Room ")
                        .append(roomNumber)
                        .append("</span>");
                    html.append("</div>");

                    html.append("<div class='detail'>");
                    html.append("<label>Room Type</label>");
                    html.append("<span>")
                        .append(roomType)
                        .append("</span>");
                    html.append("</div>");

                    html.append("<div class='detail'>");
                    html.append("<label>Check-in</label>");
                    html.append("<span>")
                        .append(checkIn)
                        .append("</span>");
                    html.append("</div>");

                    html.append("<div class='detail'>");
                    html.append("<label>Check-out</label>");
                    html.append("<span>")
                        .append(checkOut)
                        .append("</span>");
                    html.append("</div>");

                    html.append("</div>");

                    html.append("<div class='booking-bottom'>");

                    html.append("<div class='amount'>");
                    html.append("₹").append(amount);
                    html.append("</div>");

                    if (!status.equalsIgnoreCase("cancelled")) {

                        html.append("<form method='post' action='")
                            .append(ctx)
                            .append("/cancelBooking'");

                        html.append(" onsubmit=\"return confirm('Are you sure you want to cancel Booking #")
                            .append(bookingId)
                            .append("?');\">");

                        html.append("<input type='hidden' name='booking_id' value='")
                            .append(bookingId)
                            .append("'>");

                        html.append("<button class='cancel' type='submit'>");
                        html.append("Cancel Booking");
                        html.append("</button>");

                        html.append("</form>");
                    }

                    html.append("</div>");
                    html.append("</div>");
                }

                if (!hasBooking) {

                    html.append("<div class='empty'>");

                    html.append("<div class='empty-icon'>🏨</div>");

                    html.append("<h2>You have no bookings yet</h2>");

                    html.append("<p>");
                    html.append("You haven't made any hotel reservations.");
                    html.append("<br>Find your perfect stay and book your room today!");
                    html.append("</p>");

                    html.append("<a class='find' href='")
                        .append(ctx)
                        .append("/index.jsp'>");

                    html.append("Find a Hotel →");

                    html.append("</a>");

                    html.append("</div>");
                }

                html.append("<div class='back'>");
                html.append("<a href='")
                    .append(ctx)
                    .append("/index.jsp'>← Back to StayFinder</a>");
                html.append("</div>");

                html.append("</main>");

                html.append("</body></html>");

                response.getWriter().println(html.toString());
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                "<h2 style='text-align:center;margin-top:80px;color:#dc2626'>"
                + "Unable to load your bookings."
                + "</h2>"
                + "<p style='text-align:center'>"
                + "Please check the Eclipse/Tomcat console for the error."
                + "</p>"
            );
        }
    }
}