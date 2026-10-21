
package com.scc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Databaseconnection {

    private static final String URL = "jdbc:sqlite:expressway.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS vehicle_passages ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "plate_no TEXT NOT NULL, "
                + "vehicle_class TEXT NOT NULL, "
                + "toll_fee REAL NOT NULL, "
                + "payment TEXT NOT NULL)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Database connected successfully.");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
