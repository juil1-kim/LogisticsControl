package org.example.logistics.branches;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BranchesDAO {
    Connection con;

    public BranchesDAO() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/Logistics";
        String id = "root";
        String pw = "1234";
        con = DriverManager.getConnection(url, id, pw);
    }

    //Create: 지점 생성
    public void addBranches(String branches_name, String branches_loc) throws Exception {
        String query = "insert into branches(name, location)  values(?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, branches_name);
        ps.setString(2, branches_loc);

        ps.executeUpdate();
        System.out.println("=====삽입 성공=====");
    }

    //Read: 지점 목록
    public List<BranchesVO> getAllBranches() throws Exception {
        List<BranchesVO> branchesList = new ArrayList<>();
        String query = "SELECT branch_id, name, location FROM branches";
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        System.out.println("=====지점 목록=====");
        while (rs.next()) {
            BranchesVO request = new BranchesVO();
            request.setBranchID(rs.getInt("branch_id"));
            request.setBranchName(rs.getString("name"));
            request.setBranchLocate(rs.getString("location"));
            branchesList.add(request);
        }

        return branchesList;
    }

    //Update: 지점 수정
    public void update(String name, int id) throws Exception {
        String sql = "update branches set name = ? where branch_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    //Delete: 지점 삭제
    public void delete(int id) throws Exception {
        String sql = "delete from branches where branch_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ps.executeUpdate();
    }
}
