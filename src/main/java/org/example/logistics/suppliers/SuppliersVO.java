package org.example.logistics.suppliers;

import lombok.Data;

// @Data 어노테이션이 포함된 VO(Value Object) 클래스
// SuppliersVO는 공급자(Supplier)의 정보를 담기 위한 객체로 사용됨.
// 데이터베이스 테이블의 행(Row)을 표현하며, 공급자 데이터를 객체 형태로 다룰 수 있게 해줌.
@Data
public class SuppliersVO {
    // 공급자 ID (Primary Key 역할)
    // 각 공급자를 고유하게 식별하기 위한 ID.
    private int supplierId;
    // 공급자 이름
    // 공급자를 구분하고 인식하기 위해 사용.
    private String name;
    // 연락처 정보
    // 공급자와의 연락을 위해 필요한 정보 (전화번호, 이메일 등).
    private String contact; // 전화번호
    // 공급자의 위치
    // 공급자의 물리적 위치를 나타냄. 물류 시스템에서 공급자와의 거리 또는 지역 정보를 활용 가능.
    private String location;
}