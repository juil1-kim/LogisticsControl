package org.example.logistics.administrators;

import lombok.Data;

@Data
public class AdministratorsVO {
    private int admin_id;
    private String user_id;
    private String password;
    private String role;
}
