package org.example.logistics.warehouses;
import org.example.logistics.service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehousesDAO {
    private Connection con;

    public WarehousesDAO() throws SQLException, ClassNotFoundException {
        this.con = DatabaseConnection.getConnection();
    }

    //Create: 지점 생성
    public void addWarehouses(String warehouse_name, String warehouse_loc, int warehouse_cap) throws Exception {
        String query = "insert into branches(name, location, capacity)  values(?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, warehouse_name);
        ps.setString(2, warehouse_loc);
        ps.setInt(3, warehouse_cap);

        ps.executeUpdate();
        System.out.println("=====창고 추가=====");
    }

    //Read: 지점 목록
    public List<WarehousesVO> getAllWarehouses() throws Exception {
        List<WarehousesVO> WarehouseesList = new ArrayList<>();
        String query = "SELECT warehouse_id, name, location, capacity FROM Warehouses";
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        System.out.println("=====지점 목록=====");
        while (rs.next()) {
            WarehousesVO request = new WarehousesVO();
            request.setWarehouseId(rs.getInt("warehouse_id"));
            request.setName(rs.getString("name"));
            request.setLocation(rs.getString("location"));
            request.setCapacity(rs.getInt("capacity"));
            WarehouseesList.add(request);
        }

        return WarehouseesList;
    }

    //Update: 지점 수정
    public void update(String name, int id) throws Exception {
        String sql = "update Warehouses set name = ? where Warehouse_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    //Delete: 지점 삭제
    public void delete(int id) throws Exception {
        String sql = "delete from Warehouses where Warehouse_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ps.executeUpdate();
    }
}
