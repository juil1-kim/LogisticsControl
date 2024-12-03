package org.example.logistics.administratorsWarehouses;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AdministratorsWarehousesVO {
    private int admin_warehouse_id;
    private int admin_id;
    private int warehouse_id;
    private Timestamp assigned_at;
}
