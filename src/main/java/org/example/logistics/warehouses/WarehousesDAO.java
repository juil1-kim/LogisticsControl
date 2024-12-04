package org.example.logistics.warehouses;

import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarehousesDAO implements WarehousesDAOInterface {
    private Connection con;

    public WarehousesDAO() throws SQLException, ClassNotFoundException {
        this.con = DatabaseConnection.getConnection();
    }

    //Create: 지점 생성
    @Override
    public void addWarehouses(String warehouse_name, String warehouse_loc, int warehouse_cap) throws Exception {
        String query = "insert into Warehouses(name, location, capacity)  values(?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, warehouse_name);
        ps.setString(2, warehouse_loc);
        ps.setInt(3, warehouse_cap);

        ps.executeUpdate();
        CRUDLogger.log("CREATE", "창고", "창고 추가: " + warehouse_name);
    }

    //Read: 지점 목록
    @Override
    public List<WarehousesVO> getAllWarehouses() throws Exception {
        List<WarehousesVO> WarehouseesList = new ArrayList<>();
        String query = "SELECT warehouse_id, name, location, capacity FROM Warehouses";
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            WarehousesVO request = new WarehousesVO();
            request.setWarehouseId(rs.getInt("warehouse_id"));
            request.setName(rs.getString("name"));
            request.setLocation(rs.getString("location"));
            request.setCapacity(rs.getInt("capacity"));
            WarehouseesList.add(request);
            CRUDLogger.log("READ", "창고", "창고 리스트: " + rs.getString("name"));
        }

        return WarehouseesList;
    }

    //Update: 지점 수정
    @Override
    public void updateWarehouses(String name, int id) throws Exception {
        String sql = "update Warehouses set name = ? where Warehouse_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, id);
        ps.executeUpdate();
        CRUDLogger.log("UPDATE", "창고", "창고 리스트: " + name);
    }

    //Delete: 지점 삭제
    @Override
    public void deleteWarehouses(int id) throws Exception {
        String sql = "delete from Warehouses where Warehouse_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("해당 창고가 삭제되었습니다.");
        CRUDLogger.log("DELETE", "창고", "삭제된 창고 ID: " + id);
    }

}
