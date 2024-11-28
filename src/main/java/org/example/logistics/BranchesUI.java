package org.example.logistics;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BranchesUI {
    private BranchesDAO branchesDAO;
    private Scanner scanner;

    public BranchesUI() throws SQLException, ClassNotFoundException {
        this.branchesDAO = new BranchesDAO();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Branch Management ===");
            System.out.println("1. Add Branch");
            System.out.println("2. View All Branches");
            System.out.println("3. View Branch by ID");
            System.out.println("4. Update Branch");
            System.out.println("5. Delete Branch");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        addBranch();
                        break;
                    case 2:
                        viewAllBranches();
                        break;
                    case 3:
                        viewBranchById();
                        break;
                    case 4:
                        updateBranch();
                        break;
                    case 5:
                        deleteBranch();
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

    private void addBranch() throws SQLException {
        System.out.print("Enter branch name: ");
        String name = scanner.nextLine();
        System.out.print("Enter branch location: ");
        String location = scanner.nextLine();

        BranchesVO branch = new BranchesVO();
        branch.setName(name);
        branch.setLocation(location);
        branchesDAO.addBranch(branch);

        System.out.println("Branch added successfully!");
    }

    private void viewAllBranches() throws SQLException {
        List<BranchesVO> branches = branchesDAO.getAllBranches();
        if (branches.isEmpty()) {
            System.out.println("No branches found.");
        } else {
            System.out.println("\n=== Branches ===");
            for (BranchesVO branch : branches) {
                System.out.println("ID: " + branch.getBranchId() +
                        ", Name: " + branch.getName() +
                        ", Location: " + branch.getLocation());
            }
        }
    }

    private void viewBranchById() throws SQLException {
        System.out.print("Enter branch ID: ");
        int id = scanner.nextInt();

        BranchesVO branch = branchesDAO.getBranchById(id);
        if (branch == null) {
            System.out.println("Branch not found.");
        } else {
            System.out.println("ID: " + branch.getBranchId());
            System.out.println("Name: " + branch.getName());
            System.out.println("Location: " + branch.getLocation());
        }
    }

    private void updateBranch() throws SQLException {
        System.out.print("Enter branch ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new location: ");
        String location = scanner.nextLine();

        BranchesVO branch = new BranchesVO();
        branch.setBranchId(id);
        branch.setName(name);
        branch.setLocation(location);
        branchesDAO.updateBranch(branch);

        System.out.println("Branch updated successfully!");
    }

    private void deleteBranch() throws SQLException {
        System.out.print("Enter branch ID to delete: ");
        int id = scanner.nextInt();

        branchesDAO.deleteBranch(id);
        System.out.println("Branch deleted successfully!");
    }

    public static void main(String[] args) {
        try {
            BranchesUI ui = new BranchesUI();
            ui.start();
        } catch (Exception e) {
            System.out.println("Failed to start application: " + e.getMessage());
        }
    }
}
