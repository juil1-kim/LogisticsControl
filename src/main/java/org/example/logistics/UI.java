package org.example.logistics;

import java.sql.SQLException;
import java.util.Scanner;

public class UI {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("");

        DAO dao = new DAO(); //1-2단계 실행!


        VO bag = dao.one(2); // 단일 요소 검색

        System.out.println("검색한 No>> " + bag.getId());
        System.out.println("검색한 Name>> " + bag.getName());
        System.out.println("검색한 Price>> " + bag.getPrice());
        System.out.println("검색한 Quantity>> " + bag.getQuantity());


    }
}
