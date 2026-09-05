package com.hotel.controller;

import java.io.IOException;
import java.util.List;

import com.hotel.dao.RoomDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/searchRooms")
public class RoomSearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String checkIn = request.getParameter("check_in");
        String checkOut = request.getParameter("check_out");

        if (checkIn == null || checkOut == null ||
            checkIn.isEmpty() || checkOut.isEmpty()) {

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                "<h2>Please select check-in and check-out dates.</h2>"
            );

            return;
        }

        RoomDAO roomDAO = new RoomDAO();

        List<String> rooms =
                roomDAO.searchAvailableRooms(checkIn, checkOut);

        request.setAttribute("rooms", rooms);
        request.setAttribute("checkIn", checkIn);
        request.setAttribute("checkOut", checkOut);

        request.getRequestDispatcher("searchResults.jsp")
               .forward(request, response);
    }
}