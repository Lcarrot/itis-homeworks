package ru.itis.Tyshenko.repositories.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleDataSource {


    public static Connection openConnection() {
        try {
            String URL_DATABASE = "jdbc:postgresql://localhost:5432/Homework";
            String USER = "postgres";
            String PASSWORD = "1029384756Qq";
            return DriverManager.getConnection(URL_DATABASE, USER, PASSWORD);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
