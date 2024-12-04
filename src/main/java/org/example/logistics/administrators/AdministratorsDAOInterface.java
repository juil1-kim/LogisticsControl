package org.example.logistics.administrators;

import org.example.logistics.branches.BranchesOutgoingOrdersVO;

import java.sql.SQLException;
import java.util.List;

public interface AdministratorsDAOInterface {
    //CREATE
    void addAdministrator(AdministratorsVO administrator) throws SQLException;

    //READ
    List<AdministratorsVO> viewAllAdministrators() throws SQLException;

    //READ
    AdministratorsVO viewAdministratorById(String user_id) throws SQLException;

    //Update
    void updateAdministrator(AdministratorsVO administrator) throws SQLException;

    void deleteAdministrator(String user_id) throws SQLException;

    // 로그인 메서드
    AdministratorsVO login(String user_id, String password) throws SQLException;

    void updatePasswordsToBCrypt() throws SQLException;

}
