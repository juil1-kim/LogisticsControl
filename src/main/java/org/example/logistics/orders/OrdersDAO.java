package org.example.logistics.orders;

import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {
    private Connection con;

    public OrdersDAO() throws SQLException, ClassNotFoundException {
        con = DatabaseConnection.getConnection();
    }

    public List<OrdersVO> getAllOrder() throws Exception {
        List<OrdersVO> OrdersList = new ArrayList<>();
        String query = "SELECT Orders.order_id, Orders.warehouse_id, Orders.branch_id, Orders.order_date, " +
                "Orders.status, Outgoing.product_id, Outgoing.quantity FROM Orders " +
                "LEFT JOIN Outgoing ON Orders.order_id = Outgoing.order_id " +
                "ORDER BY Orders.order_id";

        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            OrdersVO request = new OrdersVO();
            request.setOrderId(rs.getInt("order_id"));
            request.setWarehouseId(rs.getInt("warehouse_id"));
            request.setBranchId(rs.getInt("branch_id"));
            request.setOrderDate(rs.getTimestamp("order_date"));
            request.setStatus(rs.getString("status"));
            request.setProductId(rs.getInt("product_id"));
            request.setQuantity(rs.getInt("quantity"));
            CRUDLogger.log("READ", "주문", "주문 ID: " + rs.getInt("order_id"));
            OrdersList.add(request);
        }
        return OrdersList;
    }

    public void addOrder(int warehouseId, int branchId, int productId, int quantity) throws Exception {

        String insertOrder = "INSERT INTO Orders (warehouse_id, branch_id, status) VALUES (?, ?, ?)";
        PreparedStatement orderStmt = con.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
        orderStmt.setInt(1, warehouseId);
        orderStmt.setInt(2, branchId);
        orderStmt.setString(3, "대기");

        int affectedRows = orderStmt.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("주문 추가하는데 실패했습니다.");
        }
        // getGeneratedKeys():과 Statement.RETURN_GENERATED_KEYS 옵션을 통해, Auto_Increment(order_id)를 반환할 수 있다.
        ResultSet generatedKeys = orderStmt.getGeneratedKeys();
        int orderId = 0;
        if (generatedKeys.next()) {
            orderId = generatedKeys.getInt(1);
        } else {
            throw new SQLException("주문 생성하는데 실패했습니다.");
        }

        String insertOutgoing = "INSERT INTO Outgoing (warehouse_id, product_id, order_id, quantity) VALUES (?, ?, ?, ?)";
        PreparedStatement outgoingStmt = con.prepareStatement(insertOutgoing);
        outgoingStmt.setInt(1, warehouseId);
        outgoingStmt.setInt(2, productId);
        outgoingStmt.setInt(3, orderId);
        outgoingStmt.setInt(4, quantity);

        outgoingStmt.executeUpdate();

        System.out.println("주문과 출고 등록이 정상으로 입력됐습니다.");
    }

    public void processOrder(int requestId) {
        Connection connection = null;
        PreparedStatement getRequestStmt = null;
        PreparedStatement updateRequesterStmt = null;
        PreparedStatement updateResponderStmt = null;
        PreparedStatement updateRequestStatusStmt = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            String getRequestQuery = "SELECT Orders.order_id, Orders.warehouse_id, Orders.branch_id, Orders.order_date, " +
                    "Orders.status, Outgoing.product_id, Outgoing.quantity FROM Orders " +
                    "LEFT JOIN Outgoing ON Orders.order_id = Outgoing.order_id where Orders.order_id = ?";
            getRequestStmt = connection.prepareStatement(getRequestQuery);
            getRequestStmt.setInt(1, requestId);
            ResultSet requestResult = getRequestStmt.executeQuery();

            if (!requestResult.next()) {
                System.out.println("요청 ID를 찾을 수 없습니다.");
                return;
            }

            int orderId = requestResult.getInt("order_id");
            int warehouseId = requestResult.getInt("warehouse_id");
            int branchId = requestResult.getInt("branch_id");
            int productId = requestResult.getInt("product_id");
            int quantity = requestResult.getInt("quantity");
            String status = requestResult.getString("status");

            if (!status.equals("대기")) {
                System.out.println("이미 처리된 요청입니다.");
                return;
            }

            String getResponderProductQuery = "SELECT quantity FROM Warehouse_Inventory WHERE warehouse_id = ? AND product_id = ?";
            PreparedStatement getResponderProductStmt = connection.prepareStatement(getResponderProductQuery);
            getResponderProductStmt.setInt(1, warehouseId);
            getResponderProductStmt.setInt(2, productId);
            ResultSet responderProductResult = getResponderProductStmt.executeQuery();

            if (!responderProductResult.next() || responderProductResult.getInt("quantity") < quantity) {
                String rejectRequestQuery = "UPDATE Orders SET status = '취소' WHERE order_id = ?";
                updateRequestStatusStmt = connection.prepareStatement(rejectRequestQuery);
                updateRequestStatusStmt.setInt(1, orderId);
                updateRequestStatusStmt.executeUpdate();

                connection.commit();
                System.out.println("재고 부족으로 요청이 거절되었습니다.");
                return;
            }

            String updateResponderQuery = "UPDATE Warehouse_Inventory SET quantity = quantity - ? WHERE warehouse_id = ? AND product_id = ?";
            updateResponderStmt = connection.prepareStatement(updateResponderQuery);
            updateResponderStmt.setInt(1, quantity);
            updateResponderStmt.setInt(2, warehouseId);
            updateResponderStmt.setInt(3, productId);
            updateResponderStmt.executeUpdate();

            String acceptRequestQuery = "UPDATE Orders SET status = '완료' WHERE order_id = ?";
            updateRequestStatusStmt = connection.prepareStatement(acceptRequestQuery);
            updateRequestStatusStmt.setInt(1, orderId);
            updateRequestStatusStmt.executeUpdate();

            connection.commit();
            System.out.println("요청이 수락되었습니다.");
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("트랜잭션 롤백이 수행되었습니다.");
                } catch (SQLException rollbackEx) {
                    System.err.println("롤백 실패: " + rollbackEx.getMessage());
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (getRequestStmt != null) getRequestStmt.close();
                if (updateRequesterStmt != null) updateRequesterStmt.close();
                if (updateResponderStmt != null) updateResponderStmt.close();
                if (updateRequestStatusStmt != null) updateRequestStatusStmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}