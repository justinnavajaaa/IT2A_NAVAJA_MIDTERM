
package com.scc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Scenariodao {

    public static class VehiclePassage {
        private final int id;
        private final String plateNo;
        private final String vehicleClass;
        private final double tollFee;
        private final String payment;

        public VehiclePassage(int id, String plateNo,
                              String vehicleClass, double tollFee,
                              String payment) {
            this.id = id;
            this.plateNo = plateNo;
            this.vehicleClass = vehicleClass;
            this.tollFee = tollFee;
            this.payment = payment;
        }

        public int getId() {
            return id;
        }

        public String getPlateNo() {
            return plateNo;
        }

        public String getVehicleClass() {
            return vehicleClass;
        }

        public double getTollFee() {
            return tollFee;
        }

        public String getPayment() {
            return payment;
        }
    }

    public boolean insertPassage(String plateNo, String vehicleClass,
                                 double tollFee, String payment) {

        String sql = "INSERT INTO vehicle_passages "
                + "(plate_no, vehicle_class, toll_fee, payment) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = Databaseconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plateNo);
            pstmt.setString(2, vehicleClass);
            pstmt.setDouble(3, tollFee);
            pstmt.setString(4, payment);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }

    public List<VehiclePassage> getAllPassages() {
        List<VehiclePassage> passages = new ArrayList<>();

        String sql = "SELECT id, plate_no, vehicle_class, "
                + "toll_fee, payment FROM vehicle_passages ORDER BY id";

        try (Connection conn = Databaseconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                VehiclePassage passage = new VehiclePassage(
                        rs.getInt("id"),
                        rs.getString("plate_no"),
                        rs.getString("vehicle_class"),
                        rs.getDouble("toll_fee"),
                        rs.getString("payment")
                );

                passages.add(passage);
            }

        } catch (SQLException e) {
            System.out.println("Reading records failed: "
                    + e.getMessage());
        }

        return passages;
    }
}
