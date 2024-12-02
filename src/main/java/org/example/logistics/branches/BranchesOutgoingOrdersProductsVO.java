package org.example.logistics.branches;

import lombok.Data;

@Data
public class BranchesOutgoingOrdersProductsVO {
    private int branch_id;
    private String branch_name;
    private String location;

    private int quantity;

    private int order_id;

    private int product_id;
    private String product_name;
}

