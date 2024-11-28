package org.example.LogisticsControlPlusLogin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class LogisticsControlPlusLoginUI {
    private static DefaultTableModel tableModel; // 전역 변수로 선언
    private static JPanel centerPanel; // 중앙 패널
    private static String currentBranchId = "aaa"; // 초기 가맹점 ID

    public static void main(String[] args) {
        // 로그인 창 표시
        showLoginWindow();
    }

    private static void showLoginWindow() {
        // 로그인 창 생성
        JFrame loginFrame = new JFrame("로그인");
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(null); // 절대 위치 사용

        // 배경 패널
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(61, 89, 171)); // 코발트 블루 색상
        backgroundPanel.setBounds(0, 0, 400, 300);
        backgroundPanel.setLayout(null);

        // 사람 아이콘 추가 (크기 조정 포함)
        JLabel iconLabel = new JLabel();
        ImageIcon originalIcon = new ImageIcon("C:\\new_java\\Logistics\\user_icon.png"); // 원본 아이콘
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // 크기 조정
        iconLabel.setIcon(new ImageIcon(scaledImage)); // 새로운 아이콘 설정
        iconLabel.setBounds(150, 20, 100, 100); // 아이콘 위치 및 크기 설정
        backgroundPanel.add(iconLabel);

        // 아이디 입력 필드
        JLabel lblUsername = new JLabel("아이디:");
        lblUsername.setForeground(Color.WHITE); // 텍스트 색상 흰색
        lblUsername.setBounds(50, 130, 80, 25);
        JTextField txtUsername = new JTextField();
        txtUsername.setBounds(150, 130, 200, 25);

        // 비밀번호 입력 필드
        JLabel lblPassword = new JLabel("비밀번호:");
        lblPassword.setForeground(Color.WHITE); // 텍스트 색상 흰색
        lblPassword.setBounds(50, 170, 80, 25);
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 170, 200, 25);

        // 로그인 버튼
        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(150, 220, 100, 30);

        // 배경 패널에 컴포넌트 추가
        backgroundPanel.add(lblUsername);
        backgroundPanel.add(txtUsername);
        backgroundPanel.add(lblPassword);
        backgroundPanel.add(txtPassword);
        backgroundPanel.add(btnLogin);

        // 프레임에 배경 패널 추가
        loginFrame.add(backgroundPanel);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if ("root".equals(username) && "1234".equals(password)) {
                // 로그인 성공 시 메인 시스템 창 표시
                loginFrame.dispose(); // 로그인 창 닫기
                showMainSystemWindow();
            } else {
                // 로그인 실패 시 경고창 표시
                JOptionPane.showMessageDialog(loginFrame, "아이디나 비밀번호를 다시 입력하시오", "로그인 오류", JOptionPane.ERROR_MESSAGE);
            }
        });


        // 로그인 창 표시
        loginFrame.setVisible(true);
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

    private static void showEditPopup(String branchId, String password, String ownerName,
                                      String contact, String businessNumber,
                                      String address, String alias, int rowIndex) {
        JFrame popupFrame = new JFrame("가맹점 수정");
        popupFrame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));

        // 가맹점 ID (수정 불가)
        panel.add(new JLabel("가맹점 ID:"));
        JTextField txtId = new JTextField(branchId);
        txtId.setEditable(false); // ID는 수정 불가
        panel.add(txtId);

        // 비밀번호
        panel.add(new JLabel("비밀번호:"));
        JPasswordField txtPassword = new JPasswordField(password);
        panel.add(txtPassword);

        // 점주명
        panel.add(new JLabel("점주명:"));
        JTextField txtOwnerName = new JTextField(ownerName);
        panel.add(txtOwnerName);

        // 연락처
        panel.add(new JLabel("연락처:"));
        JPanel contactPanel = new JPanel(new GridLayout(1, 3));
        String[] contactParts = contact.split("-");
        JTextField txtContact1 = new JTextField(contactParts[0]);
        JTextField txtContact2 = new JTextField(contactParts[1]);
        JTextField txtContact3 = new JTextField(contactParts[2]);
        contactPanel.add(txtContact1);
        contactPanel.add(txtContact2);
        contactPanel.add(txtContact3);
        panel.add(contactPanel);

        // 사업자번호
        panel.add(new JLabel("사업자번호:"));
        JPanel businessNumberPanel = new JPanel(new GridLayout(1, 3));
        String[] businessParts = businessNumber.split("-");
        JTextField txtBusiness1 = new JTextField(businessParts[0]);
        JTextField txtBusiness2 = new JTextField(businessParts[1]);
        JTextField txtBusiness3 = new JTextField(businessParts[2]);
        businessNumberPanel.add(txtBusiness1);
        businessNumberPanel.add(txtBusiness2);
        businessNumberPanel.add(txtBusiness3);
        panel.add(businessNumberPanel);

        // 매장주소
        panel.add(new JLabel("매장주소:"));
        JComboBox<String> cmbAddress = new JComboBox<>(new String[]{"서울", "부산", "대구", "인천", "광주"});
        cmbAddress.setSelectedItem(address); // 기존 주소 선택
        panel.add(cmbAddress);

        // 별칭
        panel.add(new JLabel("별칭:"));
        JTextField txtAlias = new JTextField(alias);
        panel.add(txtAlias);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();

        JButton btnSave = new JButton("저장");
        JButton btnCancel = new JButton("취소");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        popupFrame.setLayout(new BorderLayout());
        popupFrame.add(panel, BorderLayout.CENTER);
        popupFrame.add(buttonPanel, BorderLayout.SOUTH);

        // 저장 버튼 클릭 이벤트
        btnSave.addActionListener(e -> {
            String updatedPassword = new String(txtPassword.getPassword());
            String updatedOwnerName = txtOwnerName.getText();
            String updatedContact = txtContact1.getText() + "-" + txtContact2.getText() + "-" + txtContact3.getText();
            String updatedBusinessNumber =
                    txtBusiness1.getText() + "-" + txtBusiness2.getText() + "-" + txtBusiness3.getText();
            String updatedAddress = cmbAddress.getSelectedItem().toString();
            String updatedAlias = txtAlias.getText();

            // 테이블 데이터 업데이트
            tableModel.setValueAt(updatedPassword, rowIndex, 1);
            tableModel.setValueAt(updatedOwnerName, rowIndex, 2);
            tableModel.setValueAt(updatedContact, rowIndex, 3);
            tableModel.setValueAt(updatedBusinessNumber, rowIndex, 4);
            tableModel.setValueAt(updatedAddress, rowIndex, 5);
            tableModel.setValueAt(updatedAlias, rowIndex, 6);

            popupFrame.dispose(); // 창 닫기
            JOptionPane.showMessageDialog(null,
                    "가맹점 정보가 성공적으로 수정되었습니다.",
                    "수정 완료",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // 취소 버튼 클릭 이벤트
        btnCancel.addActionListener(e -> popupFrame.dispose());

        popupFrame.setVisible(true);
    }




    private static void showMainSystemWindow() {
        JFrame frame = new JFrame("창고 관리자 계정");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        // 메인 레이아웃 설정
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255)); // 연한 파란색 배경

        // 상단 메뉴 패널
        JPanel menuPanel = new JPanel(new GridLayout(1, 6));
        menuPanel.setBackground(new Color(200, 220, 240)); // 밝은 회색-파란색 배경

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

        // 중앙 패널 (기본: 가맹점 관리 테이블)
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        String[] columnNames = {"가맹점 ID", "PW", "점주명", "연락처", "사업자번호", "주소", "별칭"};
        tableModel = new DefaultTableModel(null, columnNames);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(200, 220, 240));

        JButton btnAdd = new JButton("추가");
        JButton btnEdit = new JButton("수정");
        JButton btnDelete = new JButton("삭제");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // 수정 및 삭제 기능
        btnEdit.addActionListener(e -> {
            if (centerPanel.getComponent(0) instanceof JScrollPane) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // 선택된 행의 데이터 가져오기
                    String branchId = (String) tableModel.getValueAt(selectedRow, 0);
                    String password = (String) tableModel.getValueAt(selectedRow, 1);
                    String ownerName = (String) tableModel.getValueAt(selectedRow, 2);
                    String contact = (String) tableModel.getValueAt(selectedRow, 3);
                    String businessNumber = (String) tableModel.getValueAt(selectedRow, 4);
                    String address = (String) tableModel.getValueAt(selectedRow, 5);
                    String alias = (String) tableModel.getValueAt(selectedRow, 6);

                    // 수정 창 표시
                    showEditPopup(branchId, password, ownerName, contact, businessNumber, address, alias, selectedRow);
                } else {
                    JOptionPane.showMessageDialog(frame, "수정할 가맹점을 선택하세요.", "오류", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "가맹점 관리 화면에서만 수정할 수 있습니다.", "오류", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            if (centerPanel.getComponent(0) instanceof JScrollPane) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // 확인 다이얼로그 표시
                    int confirm = JOptionPane.showConfirmDialog(frame,
                            "선택한 가맹점을 삭제하시겠습니까?",
                            "삭제 확인",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // 선택된 행 삭제
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(frame, "가맹점 정보가 삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "삭제할 가맹점을 선택하세요.", "오류", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "가맹점 관리 화면에서만 삭제할 수 있습니다.", "오류", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 버튼 클릭 이벤트 (중앙 창 변경)
        ActionListener menuListener = e -> {
            centerPanel.removeAll();
            if (e.getSource() == btnBranchReceipt) {
                centerPanel.add(new JLabel("가맹점 발주관리 화면"), BorderLayout.CENTER);
            } else if (e.getSource() == btnDeliveryManagement) {
                centerPanel.add(new JLabel("물류 발주관리 화면"), BorderLayout.CENTER);
            } else if (e.getSource() == btnInOutManagement) {
                centerPanel.add(new JLabel("입출고 관리 화면"), BorderLayout.CENTER);
            } else if (e.getSource() == btnCompanyManagement) {
                centerPanel.add(new JLabel("업체관리 화면"), BorderLayout.CENTER);
            } else if (e.getSource() == btnBranchManagement) {
                centerPanel.add(scrollPane); // 가맹점 관리 테이블
            } else if (e.getSource() == btnSalesManagement) {
                centerPanel.add(new JLabel("매출관리 화면"), BorderLayout.CENTER);
            }
            centerPanel.revalidate();
            centerPanel.repaint();
        };

        btnBranchReceipt.addActionListener(menuListener);
        btnDeliveryManagement.addActionListener(menuListener);
        btnInOutManagement.addActionListener(menuListener);
        btnCompanyManagement.addActionListener(menuListener);
        btnBranchManagement.addActionListener(menuListener);
        btnSalesManagement.addActionListener(menuListener);

        // 추가 버튼 클릭 이벤트 (가맹점 관리에서만 작동)
        btnAdd.addActionListener(e -> {
            if (centerPanel.getComponent(0) instanceof JScrollPane) {
                showAddPopup(); // 추가 팝업 창 표시
            } else {
                JOptionPane.showMessageDialog(frame, "가맹점 관리에서만 추가할 수 있습니다.", "오류", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 메인 프레임 구성 요소 추가
        mainPanel.add(menuPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}