package org.example.logistics;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CategoriesUI {
    private final CategoriesDAO categoriesDAO;

    // Constructor: DAO 객체 생성
    public CategoriesUI() throws SQLException, ClassNotFoundException {
        this.categoriesDAO = new CategoriesDAO();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Categories Management ===");
            System.out.println("1. Add Category");
            System.out.println("2. View All Categories");
            System.out.println("3. View Category by ID");
            System.out.println("4. Update Category");
            System.out.println("5. Delete Category");
            System.out.println("6. Delete All Categories");
            System.out.println("7. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        addCategory(scanner);
                        break;
                    case 2:
                        viewAllCategories();
                        break;
                    case 3:
                        viewCategoryById(scanner);
                        break;
                    case 4:
                        updateCategory(scanner);
                        break;
                    case 5:
                        deleteCategory(scanner);
                        break;
                    case 6:
                        deleteAllCategories(scanner);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addCategory(Scanner scanner) throws SQLException {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine();
        System.out.print("Enter category description: ");
        String description = scanner.nextLine();

        CategoriesVO category = new CategoriesVO();
        category.setName(name);
        category.setDescription(description);

        categoriesDAO.addCategory(category);
        System.out.println("Category added successfully!");
    }

    private void viewAllCategories() throws SQLException {
        List<CategoriesVO> categories = categoriesDAO.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            System.out.println("\n=== All Categories ===");
            for (CategoriesVO category : categories) {
                System.out.println("ID: " + category.getCategoryId() +
                        ", Name: " + category.getName() +
                        ", Description: " + category.getDescription());
            }
        }
    }

    private void viewCategoryById(Scanner scanner) throws SQLException {
        System.out.print("Enter category ID: ");
        int id = scanner.nextInt();

        CategoriesVO category = categoriesDAO.getCategoryById(id);
        if (category != null) {
            System.out.println("\nCategory Details:");
            System.out.println("ID: " + category.getCategoryId());
            System.out.println("Name: " + category.getName());
            System.out.println("Description: " + category.getDescription());
        } else {
            System.out.println("Category not found.");
        }
    }

    private void updateCategory(Scanner scanner) throws SQLException {
        System.out.print("Enter category ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        CategoriesVO category = categoriesDAO.getCategoryById(id);
        if (category != null) {
            System.out.println("Current name: " + category.getName());
            System.out.print("Enter new name: ");
            String newName = scanner.nextLine();

            System.out.println("Current description: " + category.getDescription());
            System.out.print("Enter new description: ");
            String newDescription = scanner.nextLine();

            category.setName(newName);
            category.setDescription(newDescription);

            categoriesDAO.updateCategory(category);
            System.out.println("Category updated successfully!");
        } else {
            System.out.println("Category not found.");
        }
    }

    private void deleteCategory(Scanner scanner) throws SQLException {
        System.out.print("Enter category ID to delete: ");
        int id = scanner.nextInt();

        categoriesDAO.deleteCategory(id);
        System.out.println("Category deleted successfully!");
    }

    private void deleteAllCategories(Scanner scanner) throws SQLException {
        System.out.print("Are you sure you want to delete all categories? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            categoriesDAO.deleteAllCategories();
            System.out.println("All categories deleted successfully!");
        } else {
            System.out.println("Operation canceled.");
        }
    }

    public static void main(String[] args) {
        try {
            CategoriesUI ui = new CategoriesUI();
            ui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
