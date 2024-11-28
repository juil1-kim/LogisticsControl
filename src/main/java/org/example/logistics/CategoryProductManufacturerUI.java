package org.example.logistics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class CategoryProductManufacturerUI extends JFrame {
    private JTable table;
    private CategoryProductManufacturerDAO dao;

    public CategoryProductManufacturerUI() {
        // DAO 초기화
        try {
            Connection connection = DatabaseConnection.getConnection();
            dao = new CategoryProductManufacturerDAO(connection);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB 연결 실패: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setTitle("Category-Product-Manufacturer");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 테이블 초기화
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadTableData());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 초기 데이터 로드
        loadTableData();
    }

    private void loadTableData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // DAO에서 데이터 가져오기
            List<CategoryProductManufacturerVO> data = dao.getCategoryProductManufacturers();

            // 테이블 모델 생성
            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Category Name", "Product Name", "Manufacturer Name", "Contact"}, 0
            );

            for (CategoryProductManufacturerVO vo : data) {
                model.addRow(new Object[]{
                        vo.getCategoryName(),
                        vo.getProductName(),
                        vo.getManufacturerName(),
                        vo.getManufacturerContact()
                });
            }

            table.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "데이터 로드 실패: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CategoryProductManufacturerUI().setVisible(true));
    }
}

