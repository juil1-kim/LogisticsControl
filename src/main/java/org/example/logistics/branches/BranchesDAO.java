package org.example.logistics.branches;

import org.example.logistics.productStatistics.ProductInventoryVO;
import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchesDAO implements BranchesDAOInterface {
    private Connection conn;

    // Constructor: DatabaseConnection에서 Connection 가져오기
    public BranchesDAO() throws SQLException, ClassNotFoundException {
        this.conn = DatabaseConnection.getConnection();
    }

    // CREATE: 지점 추가
    @Override
    public void addBranch(BranchesVO branch) throws SQLException {
        String sql = "INSERT INTO Branches (name, location) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, branch.getName());
            stmt.setString(2, branch.getLocation());
            stmt.executeUpdate();
            CRUDLogger.log("CREATE", "지점", "지점 추가: " + branch.getName());
        } catch (SQLException e) {
            logAndThrow("지점 추가: ", e);
        }
    }

    // READ ALL: 모든 지점 가져오기
    @Override
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
                CRUDLogger.log("READ", "지점", "지점 조회: " + branch.getName());
            }
        } catch (SQLException e) {
            logAndThrow("모든 지점 조회 실패: ",e);
        }
        return branches;
    }

    // READ BY ID: 특정 ID의 지점 가져오기
    @Override
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
                CRUDLogger.log("READ", "지점", "지점 조회: " + branch.getName());
                return branch;
            }
        } catch (SQLException e) {
            logAndThrow("조회 실패", e);
        }
        return null;
    }

    // UPDATE: 지점 정보 수정
    @Override
    public void updateBranch(BranchesVO branch) throws SQLException {
        String sql = "UPDATE Branches SET name = ?, location = ? WHERE branch_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, branch.getName());
            stmt.setString(2, branch.getLocation());
            stmt.setInt(3, branch.getBranchId());
            stmt.executeUpdate();
            CRUDLogger.log("UPDATE", "지점", "지점 수정: " + branch.getName());
        } catch (SQLException e) {
            logAndThrow("지점 정보 수정 실패: ", e);
        }
    }

    // DELETE: 특정 ID의 지점 삭제
    @Override
    public void deleteBranch(int branch_id) throws SQLException {
        String sql = "DELETE FROM Branches WHERE branch_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branch_id);
            stmt.executeUpdate();
        }
        CRUDLogger.log("DELETE", "지점", "지점 삭제: " + branch_id);
    }

    // READ BY TOTAL_SALES : 지점별 총 주문량 순 정렬
    @Override
    public List<BranchesOutgoingOrdersVO> sortingBranchSales() throws SQLException {
        List<BranchesOutgoingOrdersVO> branches = new ArrayList<>();
        String sql = "SELECT " +
                "b.name AS branch_name, " +
                "SUM(o.quantity) AS total_sales " +
                "FROM " +
                "Outgoing o " +
                "JOIN " +
                "Orders ord ON o.order_id = ord.order_id " +
                "JOIN " +
                "Branches b ON ord.branch_id = b.branch_id " +
                "GROUP BY " +
                "b.branch_id, b.name " +
                "ORDER BY " +
                "total_sales DESC;";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                BranchesOutgoingOrdersVO branch = new BranchesOutgoingOrdersVO();
                branch.setName(rs.getString("branch_name"));
                branch.setQuantity(rs.getInt("total_sales"));
                branches.add(branch);
                CRUDLogger.log("READ", "지점", "지점 총 주문량: " + rs.getInt("total_sales"));
            }
        } catch (SQLException e) {
            logAndThrow("지점별 주문량 점검 실패", e);
        }
        return branches;
    }

    // SORTING BY NAME : 지점별 이름 가나다 순 정렬
    @Override
    public List<BranchesVO> sortingBranchNames() throws SQLException {
        List<BranchesVO> branches = new ArrayList<>();
        String sql = "SELECT " +
                "branch_id, " +
                "name AS branch_name, " +
                "location " +
                "FROM " +
                "Branches " +
                "ORDER BY " +
                "name ASC;";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                BranchesVO branch = new BranchesVO();
                branch.setBranchId(rs.getInt("branch_id"));
                branch.setName(rs.getString("branch_name"));
                branch.setLocation(rs.getString("location"));
                branches.add(branch);
                CRUDLogger.log("READ", "지점", "지점 ID: " + rs.getInt("branch_id"));
            }
        } catch (SQLException e) {
            logAndThrow("조회 실패", e);
        }
        return branches;
    }

    // READ BY PRODUCT_NAME SORTING TOTAL_SALES : 특정 상품별 지점 주문량 정렬
    @Override
    public List<BranchesOutgoingOrdersProductsVO> sortingBranchProduct(int productId) throws SQLException {
        List<BranchesOutgoingOrdersProductsVO> branches = new ArrayList<>();
        String sql = "SELECT " +
                "b.name AS branch_name, " +
                "p.name AS product_name, " +
                "SUM(o.quantity) AS total_sales " +
                "FROM " +
                "Outgoing o " +
                "JOIN " +
                "Orders ord ON o.order_id = ord.order_id " +
                "JOIN " +
                "Branches b ON ord.branch_id = b.branch_id " +
                "JOIN " +
                "Products p ON o.product_id = p.product_id " +
                "WHERE " +
                "p.product_id = ? " +
                "GROUP BY " +
                "b.branch_id, b.name, p.product_id, p.name " +
                "ORDER BY " +
                "total_sales DESC;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId); // 사용자로부터 입력받은 productId를 설정
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BranchesOutgoingOrdersProductsVO branch = new BranchesOutgoingOrdersProductsVO();
                    branch.setBranch_name(rs.getString("branch_name"));
                    branch.setProduct_name(rs.getString("product_name"));
                    branch.setQuantity(rs.getInt("total_sales"));
                    branches.add(branch);
                    CRUDLogger.log("READ", "지점", "검색한 상품: " + rs.getString("product_name") + ", 총 주문량: " + rs.getInt("total_sales"));
                }
            } catch (SQLException e) {
                logAndThrow("조회 실패", e);
            }
        }
        return branches;
    }

    private void logAndThrow(String message, SQLException e) {
        CRUDLogger.log("ERROR", "지점", message + " - " + e.getMessage());
        throw new RuntimeException(message, e);
    }
}