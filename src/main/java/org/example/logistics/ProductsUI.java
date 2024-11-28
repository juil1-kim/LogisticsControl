package org.example.logistics;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProductsUI {
    private ProductsDAO productsDAO;
    private Scanner scanner;

    public ProductsUI() throws SQLException, ClassNotFoundException {
        this.productsDAO = new ProductsDAO();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Product Management ===");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. View Product by ID");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        addProduct();
                        break;
                    case 2:
                        viewAllProducts();
                        break;
                    case 3:
                        viewProductById();
                        break;
                    case 4:
                        updateProduct();
                        break;
                    case 5:
                        deleteProduct();
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addProduct() throws SQLException {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product description: ");
        String description = scanner.nextLine();
        System.out.print("Enter category ID: ");
        int categoryId = scanner.nextInt();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter manufacturer ID: ");
        int manufacturerId = scanner.nextInt();

        ProductsVO product = new ProductsVO();
        product.setName(name);
        product.setDescription(description);
        product.setCategoryId(categoryId);
        product.setPrice(price);
        product.setManufacturerId(manufacturerId);

        productsDAO.addProduct(product);
        System.out.println("Product added successfully!");
    }

    private void viewAllProducts() throws SQLException {
        List<ProductsVO> products = productsDAO.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("\n=== Products ===");
            for (ProductsVO product : products) {
                System.out.println("ID: " + product.getProductId());
                System.out.println("Name: " + product.getName());
                System.out.println("Description: " + product.getDescription());
                System.out.println("Category ID: " + product.getCategoryId());
                System.out.println("Price: " + product.getPrice());
                System.out.println("Created At: " + product.getCreatedAt());
                System.out.println("Manufacturer ID: " + product.getManufacturerId());
                System.out.println("----------------------");
            }
        }
    }

    private void viewProductById() throws SQLException {
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();

        ProductsVO product = productsDAO.getProductById(productId);
        if (product == null) {
            System.out.println("Product not found.");
        } else {
            System.out.println("\n=== Product Details ===");
            System.out.println("ID: " + product.getProductId());
            System.out.println("Name: " + product.getName());
            System.out.println("Description: " + product.getDescription());
            System.out.println("Category ID: " + product.getCategoryId());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Created At: " + product.getCreatedAt());
            System.out.println("Manufacturer ID: " + product.getManufacturerId());
        }
    }

    private void updateProduct() throws SQLException {
        System.out.print("Enter product ID to update: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        System.out.print("Enter new category ID: ");
        int categoryId = scanner.nextInt();
        System.out.print("Enter new price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new manufacturer ID: ");
        int manufacturerId = scanner.nextInt();

        ProductsVO product = new ProductsVO();
        product.setProductId(productId);
        product.setName(name);
        product.setDescription(description);
        product.setCategoryId(categoryId);
        product.setPrice(price);
        product.setManufacturerId(manufacturerId);

        productsDAO.updateProduct(product);
        System.out.println("Product updated successfully!");
    }

    private void deleteProduct() throws SQLException {
        System.out.print("Enter product ID to delete: ");
        int productId = scanner.nextInt();

        productsDAO.deleteProduct(productId);
        System.out.println("Product deleted successfully!");
    }

    public static void main(String[] args) {
        try {
            ProductsUI ui = new ProductsUI();
            ui.start();
        } catch (Exception e) {
            System.out.println("Failed to start application: " + e.getMessage());
        }
    }
}
