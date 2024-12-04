package org.example.logistics.branches;

import java.sql.SQLException;
import java.util.List;

public interface BranchesDAOInterface {
    // CREATE: 지점 추가
    void addBranch(BranchesVO branch) throws SQLException;

    // READ ALL: 모든 지점 가져오기
    List<BranchesVO> getAllBranches() throws SQLException;

    // READ BY ID: 특정 ID의 지점 가져오기
    BranchesVO getBranchById(int branchId) throws SQLException;

    // UPDATE: 지점 정보 수정
    void updateBranch(BranchesVO branch) throws SQLException;

    // DELETE: 특정 ID의 지점 삭제
    void deleteBranch(int branch_id) throws SQLException;

    // READ BY TOTAL_SALES : 지점별 총 판매량 순 정렬
    List<BranchesOutgoingOrdersVO> sortingBranchSales() throws SQLException;

    // SORTING BY NAME : 지점별 이름 가나다 순 정렬
    List<BranchesVO> sortingBranchNames() throws SQLException;

    // READ BY PRODUCT_NAME SORTING TOTAL_SALES : 특정 상품별 지점 판매량 정렬
    List<BranchesOutgoingOrdersProductsVO> sortingBranchProduct(int productId) throws SQLException;
}
