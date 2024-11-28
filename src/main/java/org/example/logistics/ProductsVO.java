package org.example.logistics;

import lombok.Data;

//생성자
@Data

public class ProductsVO {
    private int productId;
    private String name;
    private String description;
    private int categoryId;
    private double price;
    private String createdAt;
    private int manufacturerId;
}

