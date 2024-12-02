package org.example.logistics.branches;

import lombok.Data;

@Data
public class BranchesOutgoingOrdersVO {
    private int branch_id;
    private String name;
    private String location;

    private int quantity;

    private int order_id;
}

