package org.example.logistics.productStatistics;

import org.example.logistics.service.DatabaseConnection;
import org.jfree.chart.*;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductInventoryUI {

    private final ProductInventoryDAO dao;
    private final List<JFrame> openFrames;

    public ProductInventoryUI() {
        this.dao = new ProductInventoryDAO();
        this.openFrames = new ArrayList<>();
    }

    private void displayInventoryConsole() {
        List<ProductInventoryVO> inventoryList = dao.getProductInventory();

        int idWidth = 10;
        int nameWidth = 25;
        int quantityWidth = 10;
        int warehouseWidth = 20;

        String header = String.format("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + quantityWidth + "s | %-" + warehouseWidth + "s",
                "ID", "제품명", "수량", "창고명");
        String separator = "-".repeat(idWidth) + "-+-" +
                "-".repeat(nameWidth) + "-+-" +
                "-".repeat(quantityWidth) + "-+-" +
                "-".repeat(warehouseWidth);

        System.out.println("=".repeat(header.length()));
        System.out.println("전체 제품 재고 목록");
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

        System.out.println("=".repeat(header.length()));
    }

    private void displayTableAndBarChart() {
        List<ProductInventoryVO> inventoryList = dao.getProductInventory();

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
        table.setFillsViewportHeight(true);

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
            openFrames.add(frame);
        });
    }

    private void displayPieChart() {
        List<ProductInventoryVO> inventoryList = dao.getProductInventory();

        DefaultPieDataset dataset = new DefaultPieDataset();
        int totalQuantity = inventoryList.stream().mapToInt(ProductInventoryVO::getQuantity).sum();

        for (ProductInventoryVO inventory : inventoryList) {
            double percentage = (inventory.getQuantity() / (double) totalQuantity) * 100;
            dataset.setValue(inventory.getProductName(), percentage);
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
            openFrames.add(frame);
        });
    }

    public void start() {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        while (true) {
            System.out.println("\n1. 제품 목록 보기 (콘솔)");
            System.out.println("2. 제품 + 막대 그래프(재고)");
            System.out.println("3. 원형 그래프(재고)");
            System.out.println("4. 종료");
            System.out.print("옵션을 선택하세요: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> displayInventoryConsole();
                case 2 -> displayTableAndBarChart();
                case 3 -> displayPieChart();
                case 4 -> {
                    System.out.println("종료합니다...");
                    closeAllFrames();
                    return;
                }
                default -> System.out.println("잘못된 선택입니다. 다시 시도하세요.");
            }
        }
    }

    private void closeAllFrames() {
        for (JFrame frame : openFrames) {
            frame.dispose();
        }
        openFrames.clear();
    }

    public static void main(String[] args) {
        try {
            ProductInventoryUI ui = new ProductInventoryUI();
            ui.start();
        } finally {
            DatabaseConnection.close();
        }
    }
}
