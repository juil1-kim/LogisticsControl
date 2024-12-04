package org.example.logistics.warehouseInventory;

import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WarehouseInventoryDAO implements WarehouseInventoryDAOInterface {
    private Connection con;

    public WarehouseInventoryDAO() throws SQLException, ClassNotFoundException {
        this.con = DatabaseConnection.getConnection();
    }

    @Override
    public ArrayList<IncomingVO> getAllInventoryOrders() throws Exception {
        ArrayList<IncomingVO> InventoryOrders = new ArrayList<>();
        String query = "SELECT incoming_id, warehouse_id, product_id, quantity, incoming_date FROM Incoming";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            IncomingVO iv = new IncomingVO();
            iv.setIncomingId(rs.getInt("incoming_id"));
            iv.setWarehouseId(rs.getInt("warehouse_id"));
            iv.setProductId(rs.getInt("product_id"));
            iv.setQuantity(rs.getInt("quantity"));
            iv.setIncomingDate(rs.getString("incoming_date"));
            CRUDLogger.log("READ", "창고주문", "창고 주문ID: " + rs.getInt("incoming_id"));
            InventoryOrders.add(iv);
        }
        return InventoryOrders;
    }

    @Override
    public ArrayList<WarehouseInventoryVO> getAllInventory(int id) throws Exception {
        ArrayList<WarehouseInventoryVO> inventoryList = new ArrayList<>();
        String query = "SELECT p.product_id, p.name, p.price, wi.quantity, wi.last_updated " +
                "FROM Warehouse_Inventory wi JOIN Products p ON wi.product_id = p.product_id WHERE wi.warehouse_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeQuery();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            WarehouseInventoryVO wivo = new WarehouseInventoryVO();
            wivo.setProductId(rs.getInt("product_id"));
            wivo.setProductName(rs.getString("name"));
            wivo.setProductPrice(rs.getDouble("price"));
            wivo.setQuantity(rs.getInt("quantity"));
            wivo.setLastUpdate(rs.getString("last_updated"));
            CRUDLogger.log("READ", "창고", "창고별 재고: " + rs.getString("name"));
            inventoryList.add(wivo);
        }
        if (inventoryList.size() == 0) {
            System.out.println("재고가 없습니다.");
        }
        return inventoryList;
    }

    //해당 창고에서 원하는 상품을 추가할 수 있다.
    //없는 상품은 새로 추가된다.
    @Override
    public void addInventory(int warehouseId, int productId, int supplierId, int quantity) throws Exception {
        String query = "INSERT INTO Warehouse_Inventory (warehouse_id, product_id, quantity) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity), last_updated = CURRENT_TIMESTAMP;";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, warehouseId);
        ps.setInt(2, productId);
        ps.setInt(3, quantity);
        ps.executeUpdate();
        CRUDLogger.log("UPDATE", "창고", "창고ID: " + warehouseId + " 상품ID: " + productId + " 재고 추가: " + quantity);

        String incomingQuery = "INSERT INTO Incoming (warehouse_id, product_id, supplier_id, quantity) VALUES (?, ?, ?, ?)";
        PreparedStatement incoming = con.prepareStatement(incomingQuery);
        incoming.setInt(1, warehouseId);
        incoming.setInt(2, productId);
        incoming.setInt(3, supplierId);
        incoming.setInt(4, quantity);
        incoming.executeUpdate();
        CRUDLogger.log("CREATE", "Incoming", "상품ID: " + productId + " 재고: " + quantity);
    }
}