package com.hotel.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hotel Room Booking System</title>");
        out.println("</head>");

        out.println("<body>");

        out.println("<h1>Hotel Room Booking System</h1>");
        out.println("<h2>Hello from my first Servlet!</h2>");
        out.println("<p>Servlet is working successfully.</p>");

        out.println("</body>");
        out.println("</html>");
    }
}