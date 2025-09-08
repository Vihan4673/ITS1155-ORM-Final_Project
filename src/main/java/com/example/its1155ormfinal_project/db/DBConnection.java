package com.example.its1155ormfinal_project.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/TheEliteDrivingSchool",
                "root",
                "vihan@46"
        );
    }

    public static DBConnection getInstance() throws SQLException {
//        if (dbConnection == null) {
//            dbConnection = new DBConnection();
//        }
//        return dbConnection;

        return dbConnection == null ?
                dbConnection = new DBConnection() :
                dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
