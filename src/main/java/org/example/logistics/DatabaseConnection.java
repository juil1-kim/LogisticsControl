package org.example.logistics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// DAO 설계시
// DatabaseConnection.getConnection()을 호출하여 Connection 객체를 가져오기만 하면 됩니다.
// 향후 HikariCP 적용시 수정 필요합니다

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Logistics";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    // Singleton Connection 관리 (Thread-Safe)
    private static Connection con = null;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // 연결이 없거나 닫혀 있으면 새로 생성
        if (con == null || con.isClosed()) {

            // 동기화 블록으로 Thread-Safe 구현
            synchronized (DatabaseConnection.class) {
                if (con == null || con.isClosed()) {
                    // JDBC 1단계. 드라이버 설정
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    System.out.println("Driver Loaded Successfully!");

                    // JDBC 2단계. DB 연결
                    con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    System.out.println("Database Connected Successfully!");
                }
            }
        }
        return con;
    }
}

