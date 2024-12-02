package org.example.logistics.administrators_warehouses;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Administrators_WarehousesVO {
    private int admin_warehouse_id;
    private int admin_id;
    private int warehouse_id;
    private Timestamp assigned_at;
}
