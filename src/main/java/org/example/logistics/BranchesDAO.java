package org.example.logistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchesDAO {
    private Connection conn;

    // Constructor: DatabaseConnection에서 Connection 가져오기
    public BranchesDAO() throws SQLException, ClassNotFoundException {
        this.conn = DatabaseConnection.getConnection();
    }

    // CREATE: 지점 추가
    public void addBranch(BranchesVO branch) throws SQLException {
        String sql = "INSERT INTO Branches (name, location) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, branch.getName());
            stmt.setString(2, branch.getLocation());
            stmt.executeUpdate();
        }
    }

    // READ ALL: 모든 지점 가져오기
    public List<BranchesVO> getAllBranches() throws SQLException {
        List<BranchesVO> branches = new ArrayList<>();
        String sql = "SELECT * FROM Branches";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BranchesVO branch = new BranchesVO();
                branch.setBranchId(rs.getInt("branch_id"));
                branch.setName(rs.getString("name"));
                branch.setLocation(rs.getString("location"));
                branches.add(branch);
            }
        }
        return branches;
    }

    // READ BY ID: 특정 ID의 지점 가져오기
    public BranchesVO getBranchById(int branchId) throws SQLException {
        String sql = "SELECT * FROM Branches WHERE branch_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BranchesVO branch = new BranchesVO();
                branch.setBranchId(rs.getInt("branch_id"));
                branch.setName(rs.getString("name"));
                branch.setLocation(rs.getString("location"));
                return branch;
            }
        }
        return null;
    }

    // UPDATE: 지점 정보 수정
    public void updateBranch(BranchesVO branch) throws SQLException {
        String sql = "UPDATE Branches SET name = ?, location = ? WHERE branch_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, branch.getName());
            stmt.setString(2, branch.getLocation());
            stmt.setInt(3, branch.getBranchId());
            stmt.executeUpdate();
        }
    }

    // DELETE: 특정 ID의 지점 삭제
    public void deleteBranch(int branchId) throws SQLException {
        String sql = "DELETE FROM Branches WHERE branch_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            stmt.executeUpdate();
        }
    }
}

