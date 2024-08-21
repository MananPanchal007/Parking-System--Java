package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingLot {
    private int capacity;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        initializeParkingSpots();
    }

    private void initializeParkingSpots() {
        try (Connection con = DBConnection.getConnection()) {
            for (int i = 0; i < capacity; i++) {
                String query = "INSERT IGNORE INTO ParkingSpot (spotNumber, available, licensePlate) VALUES (?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setInt(1, i);
                pstmt.setBoolean(2, true);
                pstmt.setString(3, null);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean parkCar(Car car) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM ParkingSpot WHERE available = TRUE LIMIT 1";
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int spotNumber = rs.getInt("spotNumber");

                String updateQuery = "UPDATE ParkingSpot SET available = FALSE, licensePlate = ? WHERE spotNumber = ?";
                PreparedStatement updatePstmt = con.prepareStatement(updateQuery);
                updatePstmt.setString(1, car.getLicenseplate());
                updatePstmt.setInt(2, spotNumber);
                updatePstmt.executeUpdate();

                System.out.println("Car with number: " + car.getLicenseplate() + " parked at spot number: " + spotNumber);
                return true;
            } else {
                System.out.println("Sorry, parking is full");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeCar(String licensePlate) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM ParkingSpot WHERE licensePlate = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, licensePlate);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int spotNumber = rs.getInt("spotNumber");

                String updateQuery = "UPDATE ParkingSpot SET available = TRUE, licensePlate = NULL WHERE spotNumber = ?";
                PreparedStatement updatePstmt = con.prepareStatement(updateQuery);
                updatePstmt.setInt(1, spotNumber);
                updatePstmt.executeUpdate();

                System.out.println("Car with number: " + licensePlate + " removed from spot number: " + spotNumber);
                return true;
            } else {
                System.out.println("Car with number: " + licensePlate + " not found");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void displayParkingStatus() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM ParkingSpot";
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Parking Status:");
            while (rs.next()) {
                int spotNumber = rs.getInt("spotNumber");
                boolean available = rs.getBoolean("available");
                String licensePlate = rs.getString("licensePlate");

                String status = available ? "Available" : "Occupied by " + licensePlate;
                System.out.println("Spot " + spotNumber + ": " + status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
