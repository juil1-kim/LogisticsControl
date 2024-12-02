package org.example.logistics.suppliers;

import java.sql.SQLException;
import java.util.Scanner;

public class SuppliersUI {
    private SuppliersDAO suppliersDAO;

    public SuppliersUI() throws SQLException {
        this.suppliersDAO = new SuppliersDAO();
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("\n=== Categories Management ===");
            System.out.println("1. Add Supplier");
            System.out.println("2. View all Suppliers");
            System.out.println("3. View Supplier by ID");
            System.out.println("4. Update Supplier");
            System.out.println("5. Delete Supplier");
            System.out.println("6. Delete All Suppliers");
            System.out.println("7. Exit");
            System.out.println("Select an option: ");

            int select = sc.nextInt();
            sc.nextLine();

            try {
                switch(select) {
                    case 1:
                        addSupplier(sc);
                        break;
                    case 2:
                        viewAllSupplier(sc);
                        break;
                    case 3:
                        viewSupplierByID(sc);
                        break;
                    case 4:
                        updateSupplier(sc);
                        break;
                    case 5:
                        deleteSupplier(sc);
                        break;
                    case 6:
                        deleteAllSuppliers(sc);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Select a valid option!");
                }
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }
        }
    }

    private void addSupplier(Scanner sc) {
        System.out.println("Enter Supplier name: ");
    }

    private void viewAllSupplier(Scanner sc) {
    }

    private void viewSupplierByID(Scanner sc) {
    }

    private void updateSupplier(Scanner sc) {
    }

    private void deleteSupplier(Scanner sc) {
    }

    private void deleteAllSuppliers(Scanner sc) {
    }
}
