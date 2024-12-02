package org.example.logistics.administrators;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.example.logistics.service.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

public class AdministratorsDAO {
    private Connection conn;

    public AdministratorsDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // 로그인 메서드
    public AdministratorsVO login(String user_id, String password) throws SQLException {
        String sql = "SELECT * FROM Administrators WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedPasswordHash = rs.getString("password");

                if (BCrypt.checkpw(new String(password), storedPasswordHash)) {
                    AdministratorsVO user = new AdministratorsVO();
                    user.setAdmin_id(rs.getInt("admin_id"));
                    user.setUser_id(rs.getString("user_id"));
                    user.setPassword(storedPasswordHash);
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        }
        return null;
    }

    public void updatePasswordsToBCrypt() throws SQLException {
        String selectSql = "SELECT admin_id, password FROM Administrators";
        String updateSql = "UPDATE Administrators SET password = ? WHERE admin_id = ?";

        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             ResultSet rs = selectStmt.executeQuery()) {

            while (rs.next()) {
                int adminId = rs.getInt("admin_id");
                String plainPassword = rs.getString("password");

                // BCrypt로 비밀번호 해싱
                String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

                // 해시된 비밀번호로 업데이트
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, hashedPassword);
                    updateStmt.setInt(2, adminId);
                    updateStmt.executeUpdate();
                }
            }
        }
    }
}