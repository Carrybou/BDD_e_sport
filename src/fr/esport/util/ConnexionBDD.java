package fr.esport.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBDD {
    private static final String URL = "jdbc:mysql://localhost:3306/esport";
    private static final String USER = "root";
    private static final String PASS = "root"; 

    private static Connection instance = null;

    public static Connection getConnection() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(URL, USER, PASS);
        }
        return instance;
    }

    public static void fermer() throws SQLException {
        if (instance != null && !instance.isClosed()) {
            instance.close();
        }
    }
}