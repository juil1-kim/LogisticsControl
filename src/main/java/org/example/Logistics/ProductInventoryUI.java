package org.example.logistics;

import org.example.logistics.branches.ProductInventoryDAO;

import java.util.List;
import java.util.Scanner;

public class ProductInventoryUI {

    private final ProductInventoryDAO dao;

    public ProductInventoryUI() {
        this.dao = new ProductInventoryDAO();
    }

    private void displayInventory() {
        List<ProductInventoryVO> inventoryList = dao.getProductInventory();


        int idWidth = 10;
        int nameWidth = 25;
        int quantityWidth = 10;
        int warehouseWidth = 20;


        String header = String.format("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + quantityWidth + "s | %-" + warehouseWidth + "s",
                "ID", "Product Name", "Quantity", "Warehouse");
        String separator = "-".repeat(idWidth) + "-+-" +
                "-".repeat(nameWidth) + "-+-" +
                "-".repeat(quantityWidth) + "-+-" +
                "-".repeat(warehouseWidth);


        System.out.println("=".repeat(header.length()));
        System.out.println("  Product Inventory List");
        System.out.println("=".repeat(header.length()));
        System.out.println(header);
        System.out.println(separator);


        for (ProductInventoryVO inventory : inventoryList) {
            String row = String.format("%-" + idWidth + "d | %-" + nameWidth + "s | %-" + quantityWidth + "d | %-" + warehouseWidth + "s",
                    inventory.getProductId(),
                    inventory.getProductName(),
                    inventory.getQuantity(),
                    inventory.getWarehouseName());
            System.out.println(row);
        }

        // Footer
        System.out.println("=".repeat(header.length()));
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. View Inventory");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> displayInventory();
                case 2 -> {
                    System.out.println("Exiting...");
                    return;
                }
                // 어제 코테하다 생각나서 람다 표현식을 써봤습니다.
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            ProductInventoryUI ui = new ProductInventoryUI();
            ui.start();
        } finally {
            DatabaseConnection.close(); // .close 처리
        }
    }
}

