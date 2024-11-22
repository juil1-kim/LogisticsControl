package org.example.logistics;
import java.sql.*;


public class DAO {
    Connection con;
    public DAO() throws ClassNotFoundException, SQLException {
        // 1. 드라이버 설정
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("1. 드라이버 설정 성공!");

        // 2. DB 연결
        String url = "jdbc:mysql://localhost:3306/mydb";
        String id = "root";
        String pw = "1234";
        con = DriverManager.getConnection(url, id, pw);
        System.out.println("2. DB 연결 성공");
    }

    //----------기능 구현 파트

    // 특정 ID로 단일 조회
    public VO one(int id) throws SQLException {
        VO bag = new VO();
        String sql = "select * from MOCK_DATA where id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet table = ps.executeQuery();
        if(table.next()){
            bag.setId(table.getInt("id"));
            bag.setName(table.getString("name"));
            bag.setPrice(table.getInt("price"));
            bag.setQuantity(table.getInt("quantity"));
            bag.setDate(table.getDate("date"));
        } else {
            throw new SQLException("검색 결과가 없습니다.");
        }
        return bag;
    }
}
