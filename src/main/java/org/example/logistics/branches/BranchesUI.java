package org.example.logistics.branches;


import org.example.logistics.productStatistics.ProductInventoryVO;
import org.example.logistics.service.CRUDLogger;
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
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class BranchesUI {
    private static BranchesDAO branchesDAO;
    private static Scanner scanner;

    public BranchesUI() throws SQLException, ClassNotFoundException {
        this.branchesDAO = new BranchesDAO();
        this.scanner = new Scanner(System.in);

    }

    public void start() {
        while (true) {
            System.out.println("\n=== 지점 정보 관리 ===");
            System.out.println("1. 지점 추가");
            System.out.println("2. 전체 지점 목록");
            System.out.println("3. 지점 수정");
            System.out.println("4. 지점 삭제");
            System.out.println("5. 지점 상세 정보 조회");
            System.out.println("0. 이전 화면으로 이동");
            System.out.print("메뉴를 선택하세요 >> ");

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
                        updateBranch();
                        break;
                    case 4:
                        deleteBranch();
                        break;
                    case 5:
                        branchesSelect();
                        break;
                    case 0:
                        System.out.println("이전 화면으로 이동");
                        return;
                    default:
                        System.out.println("메뉴를 다시 입력하세요.");
                }
            }catch (Exception e) {
                System.out.println("오류가 발생하였습니다.\nError: " + e.getMessage());
            }
        }
    }

    public void branchesStatistic() {
        while (true) {
            System.out.println("\n==== 지점 통계 ====");
            System.out.println("1. 지점별 총 판매량(막대 그래프)");
            System.out.println("2. 지점별 총 판매량(원형 그래프)");
            System.out.println("3. 상품별 지점 판매량(막대 그래프)");
            System.out.println("4. 상품별 지점 판매량(원형 그래프)");
            System.out.println("0. 이전 창으로 돌아가기");
            System.out.print("메뉴를 선택하세요 >> ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        branchSalesBarChart();
                        break;
                    case 2:
                        branchSalesPieChart();
                        break;
                    case 3:
                        System.out.print("상품 ID: ");
                        int productId = scanner.nextInt();
                        displayTableAndBarChart(productId);
                        break;
                    case 4:
                        System.out.print("상품 ID: ");
                        int productId2 = scanner.nextInt();
                        displayPieChart(productId2);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("메뉴를 다시 입력하세요.");
                }
            }catch (Exception e) {
                System.out.println("오류가 발생하였습니다.\nError: " + e.getMessage());
            }
        }
    }

    public void branchesSelect() {
        while (true) {
            System.out.println("\n==== 지점 검색 ====");
            System.out.println("1. 특정 지점 정보 검색");
            System.out.println("2. 이름순 지점 리스트");
            System.out.println("3. 총 판매량순 리스트");
            System.out.println("4. 상품별 지점 판매량순 리스트");
            System.out.println("5. 통계 보기");
            System.out.println("0. 이전 창으로 돌아가기");
            System.out.print("메뉴를 선택하세요 >> ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        viewBranchById();
                        break;
                    case 2:
                        sortingBranchNames();
                        break;
                    case 3:
                        sortingBranchSales();
                        break;
                    case 4:
                        sortingBranchProduct();
                        break;
                    case 5:
                        branchesStatistic();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("메뉴를 다시 입력하세요.");
                }
            }catch (Exception e) {
                System.out.println("오류가 발생하였습니다.\nError: " + e.getMessage());
            }
        }
    }


    // 메뉴 1번. 지점 추가
    private void addBranch() throws SQLException {
        System.out.print("지점 이름: ");
        String name = scanner.nextLine();
        System.out.print("지점 주소: ");
        String location = scanner.nextLine();

        BranchesVO branch = new BranchesVO();
        branch.setName(name);
        branch.setLocation(location);
        branchesDAO.addBranch(branch);

        System.out.println("지점이 추가되었습니다.");
    }

    private void viewAllBranches() throws SQLException {
        List<BranchesVO> branches = branchesDAO.getAllBranches();
        if (branches.isEmpty()) {
            System.out.println("지점이 없습니다.");
        } else {
            System.out.println("\n=== 전체 지점 목록 ===");
            for (BranchesVO branch : branches) {
                System.out.println("지점 ID: " + branch.getBranchId() +
                        ", 지점 이름: " + branch.getName() +
                        ", 지점 주소: " + branch.getLocation());
            }
        }
    }

    private void viewBranchById() throws SQLException {
        System.out.print("정보 확인할 지점 ID: ");
        int id = scanner.nextInt();

        BranchesVO branch = branchesDAO.getBranchById(id);
        if (branch == null) {
            System.out.println("해당하는 지점이 없습니다.");
        } else {
            System.out.println("지점 ID: " + branch.getBranchId());
            System.out.println("지점 이름: " + branch.getName());
            System.out.println("지점 주소: " + branch.getLocation());
        }
    }

    private void updateBranch() throws SQLException {
        System.out.print("업데이트할 지점 ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("새로운 지점 이름: ");
        String name = scanner.nextLine();
        System.out.print("새로운 지점 주소: ");
        String location = scanner.nextLine();

        BranchesVO branch = new BranchesVO();
        branch.setBranchId(id);
        branch.setName(name);
        branch.setLocation(location);
        branchesDAO.updateBranch(branch);

        System.out.println("지점이 수정되었습니다.");
    }

    private void deleteBranch() throws SQLException {
        System.out.print("삭제할 지점 ID: ");
        int id = scanner.nextInt();
        branchesDAO.deleteBranch(id);
        System.out.println("지점이 삭제되었습니다.");
    }

    public static void sortingBranchSales() throws SQLException{

        List<BranchesOutgoingOrdersVO> branches = branchesDAO.sortingBranchSales();

        if (branches.isEmpty()) {
            System.out.println("지점을 찾지 못했습니다.");
        } else {
            System.out.println("\n=== 지점별 총 판매량 순 정렬 ===");
            for (BranchesOutgoingOrdersVO branch : branches) {
                System.out.println("지점 이름: " + branch.getName() +
                        ", 총 판매량: " + branch.getQuantity());
            }
        }
    }

    public static void sortingBranchNames() throws SQLException {
        List<BranchesVO> branches = branchesDAO.sortingBranchNames();

        if (branches.isEmpty()) {
            System.out.println("지점을 찾지 못했습니다.");
        }else {
            System.out.println("\n=== 지점 이름 순 정렬 ===");
            for (BranchesVO branch : branches) {
                System.out.println("ID: " + branch.getBranchId() +
                        ", 지점 이름: " + branch.getName() +
                        ", 주소: " + branch.getLocation());
            }
        }
    }

    public static void sortingBranchProduct() throws SQLException {
        System.out.print("검색하고자 하는 상품 ID: ");
        int productId = scanner.nextInt(); // 사용자로부터 상품 ID 입력받기
        List<BranchesOutgoingOrdersProductsVO> branches = branchesDAO.sortingBranchProduct(productId);

        if (branches.isEmpty()) {
            System.out.println("지점을 찾지 못했습니다.");
        } else {
            System.out.println("\n=== 상품별 가맹점 판매량 ===");
            for (BranchesOutgoingOrdersProductsVO branch : branches) {
                System.out.println("지점 이름: " + branch.getBranch_name() +
                        ", 상품 이름: " + branch.getProduct_name() +
                        ", 총 판매량: " + branch.getQuantity());
            }
        }
    }


    private void branchSalesBarChart() throws SQLException {
        List<BranchesOutgoingOrdersVO> branchesList = branchesDAO.sortingBranchSales(); // 데이터 조회

        // JTable 데이터 준비
        String[] columnNames = {"지점명", "총 판매량"};
        Object[][] data = new Object[branchesList.size()][2];
        for (int i = 0; i < branchesList.size(); i++) {
            BranchesOutgoingOrdersVO branch = branchesList.get(i);
            data[i][0] = branch.getName();
            data[i][1] = branch.getQuantity();
        }
        JTable table = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // 막대 그래프 데이터 준비
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (BranchesOutgoingOrdersVO branch : branchesList) {
            dataset.addValue(branch.getQuantity(), "총 판매량", branch.getName());
        }
        JFreeChart barChart = ChartFactory.createBarChart(
                "지점별 총 판매량 그래프", "지점명", "총 판매량", dataset
        );
        ChartPanel chartPanel = new ChartPanel(barChart);

        // JFrame에 JTable과 그래프 추가
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("지점 목록 + 총 판매량 그래프");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            frame.add(tableScrollPane, BorderLayout.CENTER);  // 테이블 중앙
            frame.add(chartPanel, BorderLayout.SOUTH);        // 그래프 아래

            frame.pack();
            frame.setVisible(true);
        });
    }

    private void branchSalesPieChart() throws SQLException {
        List<BranchesOutgoingOrdersVO> branchesList = branchesDAO.sortingBranchSales(); // 데이터 조회

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (BranchesOutgoingOrdersVO branch : branchesList) {
            dataset.setValue(branch.getName(), branch.getQuantity());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "지점별 총 판매량 분포", dataset, true, true, false
        );

        PiePlot plot = (PiePlot) pieChart.getPlot();
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0.00%")
        );
        plot.setLabelGenerator(labelGenerator);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("지점 총 판매량 원형 그래프");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new ChartPanel(pieChart));
            frame.pack();
            frame.setVisible(true);
        });
    }

    private void displayTableAndBarChart(int productId) throws SQLException {
        List<BranchesOutgoingOrdersProductsVO> branchesList = branchesDAO.sortingBranchProduct(productId);


        // JTable 데이터 준비
        String[] columnNames = {"지점명", "제품명", "총 판매량"};
        Object[][] data = new Object[branchesList.size()][3];
        for (int i = 0; i < branchesList.size(); i++) {
            BranchesOutgoingOrdersProductsVO branch = branchesList.get(i);
            data[i][0] = branch.getBranch_name();
            data[i][1] = branch.getProduct_name();
            data[i][2] = branch.getQuantity();
        }
        JTable table = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // 막대 그래프 데이터 준비
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (BranchesOutgoingOrdersProductsVO branch : branchesList) {
            dataset.addValue(branch.getQuantity(), "총 판매량", branch.getBranch_name());
        }
        JFreeChart barChart = ChartFactory.createBarChart(
                "지점별 특정 제품 총 판매량 (막대 그래프)", "지점명", "총 판매량", dataset
        );
        ChartPanel chartPanel = new ChartPanel(barChart);

        // JFrame에 JTable과 그래프 추가
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("지점별 특정 제품 총 판매량");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            frame.add(tableScrollPane, BorderLayout.CENTER);  // 테이블 중앙
            frame.add(chartPanel, BorderLayout.SOUTH);        // 그래프 아래

            frame.pack();
            frame.setVisible(true);
        });
    }

    private void displayPieChart(int productId2) throws SQLException {
        List<BranchesOutgoingOrdersProductsVO> branchesList = branchesDAO.sortingBranchProduct(productId2);

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (BranchesOutgoingOrdersProductsVO branch : branchesList) {
            dataset.setValue(branch.getBranch_name(), branch.getQuantity());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "지점별 특정 제품 총 판매량 (원형 그래프)", dataset, true, true, false
        );

        PiePlot plot = (PiePlot) pieChart.getPlot();
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0.00%")
        );
        plot.setLabelGenerator(labelGenerator);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("지점별 특정 제품 총 판매량 원형 그래프");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new ChartPanel(pieChart));
            frame.pack();
            frame.setVisible(true);
        });
    }


    public static void function() {
        try {
            BranchesUI ui = new BranchesUI();
            ui.start();
        } catch (Exception e) {
            System.out.println("에러가 발생했습니다.\nError: " + e.getMessage());
        }
    }
}