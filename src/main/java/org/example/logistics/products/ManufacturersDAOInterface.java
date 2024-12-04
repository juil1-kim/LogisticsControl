package org.example.logistics.products;

import java.util.List;

public interface ManufacturersDAOInterface {
    // 제조사 목록 조회 (READ)
    List<ManufacturersVO> getAllManufacturers();

    // 제조사 추가 (CREATE)
    boolean addManufacturer(String name, String location, String contact);

    // 제조사 수정 (UPDATE)
    boolean updateManufacturer(int id, String name, String location, String contact);

    // 제조사 삭제 (DELETE) -- id 기준
    boolean deleteManufacturer(int id);
}
