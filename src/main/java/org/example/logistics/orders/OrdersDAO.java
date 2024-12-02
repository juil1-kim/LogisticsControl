package org.example.logistics.orders;

import org.example.logistics.service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {
    private Connection con;

    public OrdersDAO() throws SQLException, ClassNotFoundException {
        this.con = DatabaseConnection.getConnection();
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
            OrdersList.add(request);
        }
        return OrdersList;
    }

    public void createOrder(int warehouseId, int branchId, int productId, int quantity) throws Exception {

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
}
