package org.example.LogisticsControl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LogisticsControlUI {
    private static DefaultTableModel tableModel; // 전역 변수로 선언
    private static String currentBranchId = "aaa"; // 초기 가맹점 ID

    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("창고 입출고 관리 시스템");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // 상단 메뉴 패널
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(1, 6));

        JButton btnBranchReceipt = new JButton("가맹점 발주관리");
        JButton btnDeliveryManagement = new JButton("물류 발주관리");
        JButton btnInOutManagement = new JButton("입출고 관리");
        JButton btnCompanyManagement = new JButton("업체관리");
        JButton btnBranchManagement = new JButton("가맹점관리");
        JButton btnSalesManagement = new JButton("매출관리");

        menuPanel.add(btnBranchReceipt);
        menuPanel.add(btnDeliveryManagement);
        menuPanel.add(btnInOutManagement);
        menuPanel.add(btnCompanyManagement);
        menuPanel.add(btnBranchManagement);
        menuPanel.add(btnSalesManagement);

        // 중앙 테이블 패널
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());

        // 테이블 생성 및 초기화
        String[] columnNames = {"가맹점 ID", "PW", "점주명", "연락처", "사업자번호", "주소", "별칭"};
        tableModel = new DefaultTableModel(null, columnNames); // 여기서 초기화
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();

        JButton btnAdd = new JButton("추가");
        JButton btnEdit = new JButton("수정");
        JButton btnDelete = new JButton("삭제");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // 추가 버튼 클릭 시 팝업 창 표시
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddPopup();
            }
        });

        // 프레임에 구성 요소 추가
        frame.add(menuPanel, BorderLayout.NORTH);
        frame.add(tablePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // 프레임 표시
        frame.setVisible(true);
    }

    private static void showAddPopup() {
        // 팝업 창 생성
        JFrame popupFrame = new JFrame("가맹점 추가");
        popupFrame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));

        // 가맹점 ID (자동생성)
        panel.add(new JLabel("가맹점 ID:"));
        JTextField txtId = new JTextField(currentBranchId);
        txtId.setEditable(false); // 자동생성 필드 비활성화
        panel.add(txtId);

        // 비밀번호
        panel.add(new JLabel("비밀번호:"));
        JPasswordField txtPassword = new JPasswordField();
        panel.add(txtPassword);

        // 점주명
        panel.add(new JLabel("점주명:"));
        JTextField txtOwnerName = new JTextField();
        panel.add(txtOwnerName);

        // 연락처
        panel.add(new JLabel("연락처:"));
        JPanel contactPanel = new JPanel(new GridLayout(1, 3));
        JTextField txtContact1 = new JTextField();
        JTextField txtContact2 = new JTextField();
        JTextField txtContact3 = new JTextField();
        contactPanel.add(txtContact1);
        contactPanel.add(txtContact2);
        contactPanel.add(txtContact3);
        panel.add(contactPanel);

        // 사업자번호
        panel.add(new JLabel("사업자번호:"));
        JPanel businessNumberPanel = new JPanel(new GridLayout(1, 3));
        JTextField txtBusiness1 = new JTextField();
        JTextField txtBusiness2 = new JTextField();
        JTextField txtBusiness3 = new JTextField();
        businessNumberPanel.add(txtBusiness1);
        businessNumberPanel.add(txtBusiness2);
        businessNumberPanel.add(txtBusiness3);
        panel.add(businessNumberPanel);

        // 매장주소
        panel.add(new JLabel("매장주소:"));
        JComboBox<String> cmbAddress = new JComboBox<>(new String[]{"서울", "부산", "대구", "인천", "광주"});
        panel.add(cmbAddress);

        // 별칭
        panel.add(new JLabel("별칭:"));
        JTextField txtAlias = new JTextField();
        panel.add(txtAlias);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();

        JButton btnAddConfirm = new JButton("추가");
        JButton btnReset = new JButton("초기화");

        buttonPanel.add(btnAddConfirm);
        buttonPanel.add(btnReset);

        // 팝업 창에 구성 요소 추가
        popupFrame.setLayout(new BorderLayout());
        popupFrame.add(panel, BorderLayout.CENTER);
        popupFrame.add(buttonPanel, BorderLayout.SOUTH);

        // "추가" 버튼 클릭 이벤트
        btnAddConfirm.addActionListener(e -> {
            String password = new String(txtPassword.getPassword());
            String ownerName = txtOwnerName.getText();
            String contact = txtContact1.getText() + "-" + txtContact2.getText() + "-" + txtContact3.getText();
            String businessNumber = txtBusiness1.getText() + "-" + txtBusiness2.getText() + "-" + txtBusiness3.getText();
            String address = cmbAddress.getSelectedItem().toString();
            String alias = txtAlias.getText();

            // 테이블에 데이터 추가
            tableModel.addRow(new Object[]{currentBranchId, password, ownerName, contact, businessNumber, address, alias});

            // 가맹점 ID 업데이트
            currentBranchId = getNextBranchId(currentBranchId);

            // 팝업 창 닫기
            popupFrame.dispose();
        });

        // "초기화" 버튼 클릭 이벤트
        btnReset.addActionListener(e -> {
            txtPassword.setText("");
            txtOwnerName.setText("");
            txtContact1.setText("");
            txtContact2.setText("");
            txtContact3.setText("");
            txtBusiness1.setText("");
            txtBusiness2.setText("");
            txtBusiness3.setText("");
            cmbAddress.setSelectedIndex(0);
            txtAlias.setText("");
        });

        // 팝업 창 표시
        popupFrame.setVisible(true);
    }

    // 가맹점 ID 자동 증가 메서드
    private static String getNextBranchId(String currentId) {
        char[] chars = currentId.toCharArray();

        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] < 'z') {
                chars[i]++;
                break;
            } else {
                chars[i] = 'a';
                if (i == 0) {
                    return "a" + new String(chars); // 앞에 'a' 추가
                }
            }
        }

        return new String(chars);
    }
}