package org.example.logistics;

import lombok.Data;
import lombok.AllArgsConstructor; // 매개변수 생성자 어노테이션 추가
import lombok.NoArgsConstructor; // 기본 생성자도 필요하면 추가

@Data
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가 (필요한 경우)
public class CategoryProductManufacturerVO {
    private String categoryName;
    private String productName;
    private String manufacturerName;
    private String manufacturerContact;
}


