package org.example.logistics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    // Singleton Connection 관리 (Thread-Safe)
    private static Connection connection = null;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            synchronized (DatabaseConnection.class) {
                if (connection == null || connection.isClosed()) {
                    // 드라이버 로드
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    System.out.println("Driver Loaded Successfully!");

                    // 연결 생성
                    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    System.out.println("Database Connected Successfully!");
                }
            }
        }
        return connection;
    }
}

