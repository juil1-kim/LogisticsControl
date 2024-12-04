package org.example.logistics.warehouseStatistics.warehouseStatistics;

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

public class WarehouseStatisticsUI {

    private final WarehouseStatisticsDAOInterface dao;

    public WarehouseStatisticsUI() {
        this.dao = new WarehouseStatisticsDAO();
    }

    // 창고 목록을 표시하고 선택한 창고의 그래프를 표시
    private void displayWarehouseSelection() {
        List<String> warehouseNames = dao.getWarehouseNames(); // DAO에서 창고 이름 가져오기

        if (warehouseNames.isEmpty()) {
            System.out.println("등록된 창고가 없습니다.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("\n==== 창고 선택 ====");
                for (int i = 0; i < warehouseNames.size(); i++) {
                    System.out.printf("%d. %s%n", i + 1, warehouseNames.get(i));
                }
                System.out.println("0. 이전 메뉴로 돌아가기");
                System.out.print("창고 번호를 선택하세요: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("숫자를 입력해주세요.");
                    scanner.next(); // 잘못된 입력을 버퍼에서 제거
                    continue;
                }

                int choice = scanner.nextInt();

                if (choice == 0) {
                    System.out.println("메인 메뉴로 돌아갑니다.");
                    break;
                }

                if (choice < 1 || choice > warehouseNames.size()) {
                    System.out.println("올바른 번호를 선택해주세요.");
                    continue;
                }

                String selectedWarehouse = warehouseNames.get(choice - 1);
                displayChartsForWarehouse(selectedWarehouse);
            } catch (Exception e) {
                System.out.println("예상치 못한 오류가 발생했습니다: " + e.getMessage());
                scanner.nextLine(); // 잘못된 입력 버퍼 비우기
            }
        }
    }

    private void displayChartsForWarehouse(String warehouseName) {
        List<WarehouseStatisticsVO> warehouseData = dao.getWarehouseDataByName(warehouseName);

        if (warehouseData.isEmpty()) {
            System.out.println("해당 창고에 데이터가 없습니다.");
            return;
        }

        // 원형 그래프 데이터 준비
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        for (WarehouseStatisticsVO vo : warehouseData) {
            pieDataset.setValue(vo.getProductName(), vo.getQuantity());
        }

        // 막대 그래프 데이터 준비
        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        for (WarehouseStatisticsVO vo : warehouseData) {
            barDataset.addValue(vo.getQuantity(), warehouseName, vo.getProductName());
        }

        // 원형 그래프 생성
        JFreeChart pieChart = ChartFactory.createPieChart(
                warehouseName + " 재고 분포", // 그래프 제목
                pieDataset,
                true,  // 범례 표시
                true,  // 툴팁 표시
                false  // URL 표시
        );

        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0.00%")
        );
        piePlot.setLabelGenerator(labelGenerator);

        // 막대 그래프 생성
        JFreeChart barChart = ChartFactory.createBarChart(
                warehouseName + " 재고 막대 그래프",
                "제품",
                "수량",
                barDataset
        );

        // 두 개의 그래프를 JFrame에 추가
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(warehouseName + " 재고 통계");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new GridLayout(2, 1)); // 상단: 원형 그래프, 하단: 막대 그래프

            frame.add(new ChartPanel(pieChart));  // 원형 그래프
            frame.add(new ChartPanel(barChart));  // 막대 그래프

            frame.setSize(1000, 800); // 창 크기 설정
            frame.setVisible(true);
        });
    }

    // 시작 메뉴
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("==== 창고별 재고 통계 ====");
                System.out.println("| 1. 창고별 그래프 보기 | 0. 이전 메뉴로 돌아가기 |");
                System.out.print("옵션을 선택하세요: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("숫자를 입력해주세요.");
                    scanner.next(); // 잘못된 입력을 버퍼에서 제거
                    continue;
                }

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> displayWarehouseSelection();
                    case 0 -> {
                        System.out.println("프로그램을 종료합니다.");
                        return;
                    }
                    default -> System.out.println("잘못된 선택입니다. 다시 시도하세요.");
                }
            } catch (Exception e) {
                System.out.println("예상치 못한 오류가 발생했습니다: " + e.getMessage());
                scanner.nextLine(); // 잘못된 입력 버퍼 비우기
            }
        }
    }

    public static void main(String[] args) {
        WarehouseStatisticsUI ui = new WarehouseStatisticsUI();
        ui.start();
    }
}
