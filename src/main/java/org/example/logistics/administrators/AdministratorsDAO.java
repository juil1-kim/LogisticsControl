package org.example.logistics.administrators;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.example.logistics.branches.BranchesOutgoingOrdersVO;
import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

public class AdministratorsDAO implements AdministratorsDAOInterface {
    private Connection conn;


    public AdministratorsDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    //CREATE
    @Override
    public void addAdministrator(AdministratorsVO administrator) throws SQLException {
        String sql = "INSERT INTO Administrators (user_id, password, role) VALUES (?, ?, 'general')";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, administrator.getUser_id());
            stmt.setString(2, administrator.getPassword());
            stmt.executeUpdate();
            CRUDLogger.log("CREATE", "관리자", "일반 관리자 추가: " + administrator.getUser_id());
        } catch (SQLException e) {
            logAndThrow("관리자 생성 실패: ", e);
        }
    }

    //READ
    @Override
    public List<AdministratorsVO> viewAllAdministrators() throws SQLException {
        List<AdministratorsVO> administrators = new ArrayList<>();
        String sql = " SELECT * FROM Administrators WHERE role = 'general'";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                AdministratorsVO administrator = new AdministratorsVO();
                administrator.setAdmin_id(rs.getInt("admin_id"));
                administrator.setUser_id(rs.getString("user_id"));
                administrator.setPassword(rs.getString("password"));
                administrators.add(administrator);
                CRUDLogger.log("READ", "관리자", "검색한 일반 관리자 계정: " + administrator.getUser_id());
            }
        } catch (SQLException e) {
            logAndThrow("관리자 조회 실패: ", e);
        }
        return administrators;
    }

    //READ
    @Override
    public AdministratorsVO viewAdministratorById(String user_id) throws SQLException {
        String sql = "SELECT * FROM Administrators WHERE user_id = ? and role = 'general'";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                AdministratorsVO administrator = new AdministratorsVO();
                administrator.setAdmin_id(rs.getInt("admin_id"));
                administrator.setUser_id(rs.getString("user_id"));
                administrator.setPassword(rs.getString("password"));
                administrator.setRole(rs.getString("role"));
                CRUDLogger.log("READ", "관리자", "'" + administrator.getUser_id()+ "' 계정 관리자 정보 검색");
                return administrator;
            }
        } catch (SQLException e) {
            logAndThrow("아이디로 관리자 조회 실패: ", e);
        }
        return null;
    }

    //Update
    @Override
    public void updateAdministrator(AdministratorsVO administrator) throws SQLException {
        String sql = "UPDATE Administrators SET user_id = ? WHERE admin_id = ? and role = 'general'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, administrator.getUser_id());
            stmt.setInt(2, administrator.getAdmin_id());
            stmt.executeUpdate();
        }
        CRUDLogger.log("UPDATE", "관리자", "계정 관리자 정보 수정: " + administrator.getUser_id());
    }

    @Override
    public void deleteAdministrator(String user_id) throws SQLException {
        String sql = "DELETE FROM Administrators WHERE user_id = ? and role = 'general'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user_id);
            stmt.executeUpdate();
            CRUDLogger.log("DELETE", "관리자", "일반 관리자 계정 삭제: " + user_id);
        } catch (SQLException e) {
            logAndThrow("관리자 삭제 실패: ", e);
        }
    }

    // 로그인 메서드
    @Override
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
            CRUDLogger.log("READ", "관리자", "관리자 로그인: " + user_id);
        } catch (SQLException e) {
            logAndThrow("로그인 실패: ", e);
        }
        return null;
    }

    @Override
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
                    CRUDLogger.log("UPDATE", "관리자", "비밀번호 암호화: " + hashedPassword);
                } catch (SQLException e) {
                    logAndThrow("비밀번호 업데이트 실패: ", e);
                }
            }
        } catch (SQLException e) {
            logAndThrow("데이터베이스 연결 실패: ", e);
        }
    }

    private void logAndThrow(String message, SQLException e) {
        CRUDLogger.log("ERROR", "관리자", message + " - " + e.getMessage());
        throw new RuntimeException(message, e);
    }
}