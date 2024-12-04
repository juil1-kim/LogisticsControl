package org.example.logistics.productStatistics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class ProductInventoryUI {

    private final ProductInventoryDAOInterface dao;

    // 생성자: DAO 인터페이스 구현체를 주입받음
    public ProductInventoryUI(ProductInventoryDAOInterface dao) {
        this.dao = dao;
    }

    private void displayAllInventoryConsole() {
        List<ProductInventoryVO> allData = dao.getAllProductInventory();

        if (allData.isEmpty()) {
            System.out.println("조회된 데이터가 없습니다.");
            return;
        }

        System.out.printf("%-10s | %-25s | %-10s | %-50s%n", "ID", "제품명", "수량", "창고명");
        System.out.println("-".repeat(100));
        for (ProductInventoryVO inventory : allData) {
            System.out.printf("%-10d | %-25s | %-10d | %-50s%n",
                    inventory.getProductId(),
                    inventory.getProductName(),
                    inventory.getQuantity(),
                    inventory.getWarehouseName());
        }
        System.out.println("-".repeat(100));
        System.out.println("총 " + allData.size() + "개의 데이터가 조회되었습니다.");
    }

    private void displayInventoryWithPagination() {
        Scanner scanner = new Scanner(System.in);
        int pageSize = 10;
        int currentPage = 0;
        boolean hasNextPage = true;

        while (hasNextPage) {
            List<ProductInventoryVO> inventoryList = dao.getProductInventory(currentPage * pageSize, pageSize);

            if (inventoryList.isEmpty()) {
                System.out.println("더 이상 데이터가 없습니다.");
                break;
            }

            System.out.printf("%-10s | %-25s | %-10s | %-50s%n", "ID", "제품명", "수량", "창고명");
            System.out.println("-".repeat(100));
            for (ProductInventoryVO inventory : inventoryList) {
                System.out.printf("%-10d | %-25s | %-10d | %-50s%n",
                        inventory.getProductId(),
                        inventory.getProductName(),
                        inventory.getQuantity(),
                        inventory.getWarehouseName());
            }

            System.out.println("\n1. 다음 페이지");
            System.out.println("2. 이전 페이지");
            System.out.println("3. 종료");
            System.out.print("옵션을 선택하세요: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> currentPage++;
                case 2 -> {
                    if (currentPage > 0) currentPage--;
                    else System.out.println("첫 페이지입니다.");
                }
                case 3 -> {
                    hasNextPage = false;
                    System.out.println("페이지 탐색을 종료합니다.");
                }
                default -> System.out.println("잘못된 선택입니다. 다시 시도하세요.");
            }
        }
    }

    private void displayTableAndBarChart() {
        List<ProductInventoryVO> inventoryList = dao.getProductInventory(0, 10);

        String[] columnNames = {"ID", "제품명", "수량", "창고명"};
        Object[][] data = new Object[inventoryList.size()][4];
        for (int i = 0; i < inventoryList.size(); i++) {
            ProductInventoryVO inventory = inventoryList.get(i);
            data[i][0] = inventory.getProductId();
            data[i][1] = inventory.getProductName();
            data[i][2] = inventory.getQuantity();
            data[i][3] = inventory.getWarehouseName();
        }
        JTable table = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(table);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (ProductInventoryVO inventory : inventoryList) {
            dataset.addValue(inventory.getQuantity(), "수량", inventory.getProductName());
        }
        JFreeChart barChart = ChartFactory.createBarChart(
                "제품 재고 그래프", "제품명", "수량", dataset
        );
        ChartPanel chartPanel = new ChartPanel(barChart);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("제품 목록 + 재고 그래프");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            frame.add(tableScrollPane, BorderLayout.CENTER);
            frame.add(chartPanel, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        });
    }

    private void displayPieChart() {
        List<ProductInventoryVO> inventoryList = dao.getProductInventory(0, 50);

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (ProductInventoryVO inventory : inventoryList) {
            dataset.setValue(inventory.getProductName(), inventory.getQuantity());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "제품 재고 분포", dataset, true, true, false
        );

        PiePlot plot = (PiePlot) pieChart.getPlot();
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0.00%")
        );
        plot.setLabelGenerator(labelGenerator);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("제품 재고 원형 그래프");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new ChartPanel(pieChart));
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void start() {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        while (true) {
            System.out.println("1. 제품 목록 보기");
            System.out.println("2. 제품 목록 보기 (페이지 탐색)");
            System.out.println("3. 상위 10개 제품 재고 & 그래프 보기");
            System.out.println("4. 백분율 그래프 보기");
            System.out.println("0. 이전 메뉴로 돌아가기");

            System.out.print("옵션을 선택하세요: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> displayAllInventoryConsole();
                case 2 -> displayInventoryWithPagination();
                case 3 -> displayTableAndBarChart();
                case 4 -> displayPieChart();
                case 0 -> {
                    return;
                }
                default -> System.out.println("잘못된 선택입니다. 다시 시도하세요.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            // DAO 구현체 생성
            ProductInventoryDAOInterface dao = new ProductInventoryDAO();
            ProductInventoryUI ui = new ProductInventoryUI(dao);
            ui.start();
        } catch (Exception e) {
            System.out.println("프로그램 시작에 실패했습니다: " + e.getMessage());
        }
    }
}

