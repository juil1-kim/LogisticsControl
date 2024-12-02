package org.example.logistics.productStatistics;

import org.example.logistics.service.DatabaseConnection;
import org.jfree.chart.*;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


import javax.swing.*;
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

    private void displayInventory() {
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

        // Footer
        System.out.println("=".repeat(header.length()));
    }

    private void displayInventoryGraph() {
        List<ProductInventoryVO> inventoryList = dao.getProductInventory();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (ProductInventoryVO inventory : inventoryList) {
            dataset.addValue(inventory.getQuantity(), "수량", inventory.getProductName());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "제품 재고 그래프", // Title
                "제품명",          // X-axis Label
                "수량",           // Y-axis Label
                dataset           // Dataset
        );

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("제품 재고 그래프");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new ChartPanel(barChart));
            frame.pack();
            frame.setVisible(true);
            openFrames.add(frame); // 열린 창 관리
        });
    }

    private void displayInventoryPieChart() {
        List<ProductInventoryVO> inventoryList = dao.getProductInventory();

        DefaultPieDataset dataset = new DefaultPieDataset();
        int totalQuantity = inventoryList.stream().mapToInt(ProductInventoryVO::getQuantity).sum();

        for (ProductInventoryVO inventory : inventoryList) {
            double percentage = (inventory.getQuantity() / (double) totalQuantity) * 100;
            dataset.setValue(inventory.getProductName(), percentage);
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "제품 재고 분포", // Title
                dataset,          // Dataset
                true,             // Legend
                true,             // Tooltips
                false             // URLs
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
            openFrames.add(frame); // 열린 창 관리
        });
    }

    public void start() {
        Scanner scanner = new Scanner(System.in, "UTF-8"); // UTF-8로 한글 지원
        while (true) {
            System.out.println("\n1. 제품 목록 보기 (표)");
            System.out.println("2. 제품 재고 보기 (막대 그래프)");
            System.out.println("3. 제품 재고 보기 (원형 그래프)");
            System.out.println("4. 종료");
            System.out.print("옵션을 선택하세요: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> displayInventory();
                case 2 -> displayInventoryGraph();
                case 3 -> displayInventoryPieChart();
                case 4 -> {
                    System.out.println("종료합니다...");
                    closeAllFrames(); // 열린 모든 Swing 창 닫기
                    return;
                }
                default -> System.out.println("잘못된 선택입니다. 다시 시도하세요.");
            }
        }
    }

    private void closeAllFrames() {
        for (JFrame frame : openFrames) {
            frame.dispose(); // 모든 창 닫기
        }
        openFrames.clear(); // 리스트 비우기
    }

    public static void main(String[] args) {
        try {
            ProductInventoryUI ui = new ProductInventoryUI();
            ui.start();
        } finally {
            DatabaseConnection.close(); // 데이터베이스 연결 종료
        }
    }
}