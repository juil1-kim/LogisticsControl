package org.example.logistics.administratorsWarehouses;

import lombok.Data;

import java.sql.Timestamp; // Timestamp 클래스를 사용하여 시간 정보를 다룸.

@Data // Lombok 어노테이션, 이 클래스를 컴파일 시 자동으로 getter, setter, toString 메서드 생성
public class AdministratorsWarehousesVO {
    // 창고 관리자와 창고 정보를 연결하는 중간 테이블의 데이터를 저장하는 VO (Value Object) 클래스
    private int admin_warehouse_id; // 관리자-창고 관계의 고유 ID
    private int admin_id; // 관리자 ID (관리자를 식별하는 값)
    private int warehouse_id; // 창고 ID (창고를 식별하는 값)
    private Timestamp assigned_at; // 창고 관리자에게 할당된 시간, 관리자가 해당 창고에 배정된 시간을 기록

    // 위의 변수들은 관리자와 창고 사이의 관계를 나타내는 데 필요한 정보를 포함하고 있음.
    // 이 VO 클래스는 데이터베이스의 'Administrator_Warehouses' 테이블에 대응하는 객체
}
