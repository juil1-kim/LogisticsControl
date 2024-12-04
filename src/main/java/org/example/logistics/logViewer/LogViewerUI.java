package org.example.logistics.logViewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Scanner;

/**
 * 로그 뷰어 UI 클래스
 */
public class LogViewerUI {
    private final LogViewerDAO logViewerDAO;
    private DefaultTableModel tableModel;

    public LogViewerUI() {
        this.logViewerDAO = new LogViewerDAO();
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("MongoDB 로그 뷰어");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());

        // 로그 출력 테이블 영역
        String[] columnNames = {"시간", "작업", "대상", "메시지"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable logTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(logTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // 입력 패널
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 3, 10, 10));

        JLabel operationLabel = new JLabel("동작 입력(CREATE, READ, UPDATE, DELETE, ERROR):");
        JTextField operationField = new JTextField();
        JButton operationButton = new JButton("검색");

        operationButton.addActionListener(e -> {
            String operation = operationField.getText();
            if (!operation.isEmpty()) {
                List<LogViewerVO> logs = logViewerDAO.getLogsByOperation(operation);
                displayLogs(logs);
            } else {
                JOptionPane.showMessageDialog(frame, "동작을 입력하세요.", "오류", JOptionPane.WARNING_MESSAGE);
            }
        });

        JLabel startDateLabel = new JLabel("시작 날짜 (yyyy-MM-dd HH:mm):");
        JTextField startDateField = new JTextField();
        JLabel endDateLabel = new JLabel("종료 날짜 (yyyy-MM-dd HH:mm):");
        JTextField endDateField = new JTextField();
        JButton dateRangeButton = new JButton("검색");

        dateRangeButton.addActionListener(e -> {
            String startDate = startDateField.getText();
            String endDate = endDateField.getText();
            if (!startDate.isEmpty() && !endDate.isEmpty()) {
                List<LogViewerVO> logs = logViewerDAO.getLogsByDateRange(startDate, endDate);
                displayLogs(logs);
            } else {
                JOptionPane.showMessageDialog(frame, "시작 날짜와 종료 날짜를 입력하세요.", "오류", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton allLogsButton = new JButton("모든 로그 보기");
        allLogsButton.addActionListener(e -> {
            List<LogViewerVO> logs = logViewerDAO.getAllLogs();
            displayLogs(logs);
        });

        inputPanel.add(operationLabel);
        inputPanel.add(operationField);
        inputPanel.add(operationButton);

        inputPanel.add(startDateLabel);
        inputPanel.add(startDateField);
        inputPanel.add(new JLabel());

        inputPanel.add(endDateLabel);
        inputPanel.add(endDateField);
        inputPanel.add(dateRangeButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(allLogsButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    /**
     * 로그 데이터를 JTable에 표시
     */
    private void displayLogs(List<LogViewerVO> logs) {
        // 기존 테이블 데이터 초기화
        tableModel.setRowCount(0);

        // 새로운 데이터 추가
        for (LogViewerVO log : logs) {
            tableModel.addRow(new Object[]{
                    log.getTimestamp(),
                    log.getOperation(),
                    log.getEntity(),
                    log.getMessage()
            });
        }

        // 데이터가 없을 경우 메시지 표시
        if (logs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "조회된 로그가 없습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== 로그 관리 시스템 ===");
            System.out.println("1. 로그 데이터 보기");
            System.out.println("0. 이전 페이지로 가기");
            System.out.print("옵션을 선택하세요 >> ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 소비

            switch (choice) {
                case 1:
                    // 로그 뷰어 실행
                    SwingUtilities.invokeLater(() -> new LogViewerUI().createAndShowGUI());
                    break; // 터미널 메뉴는 계속 유지됨
                case 0:
                    System.out.println("이전 페이지로 이동합니다.");
                    return; // 프로그램 종료 또는 이전 메뉴로 복귀
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도하세요.");
            }
        }
    }
}
