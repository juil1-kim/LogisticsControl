package org.example.logistics.administratorsWarehouses;

import lombok.Data;

import java.sql.Timestamp;

// @Data 어노테이션이 포함된 VO(Value Object) 클래스
// VO는 주로 데이터를 담기 위한 객체로, 데이터베이스 테이블의 행(Row)을 표현함.
// AdministratorsWarehousesVO는 관리자-창고 데이터를 객체로 다룰 수 있게 해줌.
@Data
public class AdministratorsWarehousesVO {
    // 관리자-창고 ID (Primary Key 역할)
    // 데이터베이스에서 각 관리자-창고 관계를 고유하게 식별하기 위해 사용됨.
    private int admin_warehouse_id;
    // 관리자 ID
    // 관리자를 식별하는 ID로, 관리자 테이블과 연관 있음 (외래 키로 사용 가능).
    private int admin_id;
    // 창고 ID
    // 창고를 식별하는 ID로, 창고 테이블과 연관 있음 (외래 키로 사용 가능).
    private int warehouse_id;
    // 할당 시간
    // 관리자가 창고에 할당된 시간을 나타냄.
    // 데이터베이스의 `Timestamp` 데이터 유형과 매핑되어 시간 정보를 포함.
    private Timestamp assigned_at;
}
