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

    private final ProductInventoryDAO dao;

    public ProductInventoryUI() {
        this.dao = new ProductInventoryDAO();
    }

    private void displayAllInventoryConsole() {
        List<ProductInventoryVO> allData = dao.getAllProductInventory(); // 전체 데이터를 조회

        if (allData.isEmpty()) {
            System.out.println("조회된 데이터가 없습니다.");
            return;
        }

        // 콘솔 출력
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
        int pageSize = 10;  // 한 페이지에 표시할 데이터 수
        int currentPage = 0;
        boolean hasNextPage = true;

        while (hasNextPage) {
            List<ProductInventoryVO> inventoryList = dao.getProductInventory(currentPage * pageSize, pageSize);

            if (inventoryList.isEmpty()) {
                System.out.println("더 이상 데이터가 없습니다.");
                break;
            }

            // 콘솔 출력
            System.out.printf("%-10s | %-25s | %-10s | %-50s%n", "ID", "제품명", "수량", "창고명");
            System.out.println("-".repeat(100));
            for (ProductInventoryVO inventory : inventoryList) {
                System.out.printf("%-10d | %-25s | %-10d | %-50s%n",
                        inventory.getProductId(),
                        inventory.getProductName(),
                        inventory.getQuantity(),
                        inventory.getWarehouseName());
            }

            // 페이지 탐색 옵션
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
        List<ProductInventoryVO> inventoryList = dao.getProductInventory(0, 10); // 상위 50개 데이터만 조회

        // JTable 데이터 준비
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

        // 막대 그래프 데이터 준비
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (ProductInventoryVO inventory : inventoryList) {
            dataset.addValue(inventory.getQuantity(), "수량", inventory.getProductName());
        }
        JFreeChart barChart = ChartFactory.createBarChart(
                "제품 재고 그래프", "제품명", "수량", dataset
        );
        ChartPanel chartPanel = new ChartPanel(barChart);

        // JFrame에 JTable과 그래프 추가
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("제품 목록 + 재고 그래프");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            frame.add(tableScrollPane, BorderLayout.CENTER);  // 테이블 중앙
            frame.add(chartPanel, BorderLayout.SOUTH);        // 그래프 아래

            frame.pack();
            frame.setVisible(true);
        });
    }

    private void displayPieChart() {
        List<ProductInventoryVO> inventoryList = dao.getProductInventory(0, 50); // 상위 50개 데이터만 가져옴

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
            System.out.println("\n1. 제품 목록 보기 (페이지 탐색)");
            System.out.println("2. 제품 + 막대 그래프(재고)");
            System.out.println("3. 원형 그래프(재고)");
            System.out.println("4. 종료");
            System.out.println("5. 전체 데이터 출력(터미널)");

            System.out.print("옵션을 선택하세요: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> displayInventoryWithPagination();
                case 2 -> displayTableAndBarChart();
                case 3 -> displayPieChart();
                case 4 -> {
                    System.out.println("종료합니다...");
                    return;
                }
                case 5 -> displayAllInventoryConsole();
                default -> System.out.println("잘못된 선택입니다. 다시 시도하세요.");
            }
        }
    }

    public static void main(String[] args) {
        ProductInventoryUI ui = new ProductInventoryUI();
        ui.start();
    }
}

