package com.hopital.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // ===== Informations de connexion à MySQL =====
    private static final String URL = "jdbc:mysql://localhost:3306/hopital_db";
    private static final String USER = "root";
    private static final String PASSWORD = "MARIAM1234";

    // ===== Méthode qui ouvre une connexion vers la base de données =====
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}