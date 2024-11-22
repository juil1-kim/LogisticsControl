package org.example.logistics;

import lombok.Data;
import java.sql.Date;

@Data

public class VO {
    private int id;
    private String name;
    private int price;
    private int quantity;
    private Date date;
}

