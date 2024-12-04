package org.example.logistics.products;

import java.util.List;

public interface CategoriesDAOInterface {
    // CREATE: 카테고리 추가
    void addCategory(CategoriesVO category);

    // READ ALL: 모든 카테고리 가져오기
    List<CategoriesVO> getAllCategories();

    // READ BY ID: 특정 ID의 카테고리 가져오기
    CategoriesVO getCategoryById(int categoryId);

    // UPDATE: 카테고리 정보 수정
    void updateCategory(CategoriesVO category);

    // DELETE: 특정 ID의 카테고리 삭제
    void deleteCategory(int categoryId);

    // DELETE ALL: 모든 카테고리 삭제 -- 안쓸거임
    void deleteAllCategories();
}
