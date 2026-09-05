package com.hotel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hotel.controller.DBConnection;

public class RoomDAO {

    public List<String> searchAvailableRooms(String checkIn, String checkOut) {

        List<String> rooms = new ArrayList<>();

        String sql =
            "SELECT room_id, room_number, room_type, price_per_night " +
            "FROM rooms " +
            "WHERE status = 'Available' " +
            "AND room_id NOT IN (" +
            "SELECT room_id FROM bookings " +
            "WHERE check_in < ? " +
            "AND check_out > ?" +
            ")";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, checkOut);
            ps.setString(2, checkIn);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String room =
                        rs.getInt("room_id") + " - " +
                        rs.getString("room_number") + " - " +
                        rs.getString("room_type") + " - ₹" +
                        rs.getDouble("price_per_night");

                    rooms.add(room);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rooms;
    }
}