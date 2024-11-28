package org.example.Logistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogisticsLoginUI extends JFrame {
    public LogisticsLoginUI() {
        // 기본 창 설정
        setTitle("Logistics Login");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // 절대 위치로 설정

        // 배경 색상 설정
        getContentPane().setBackground(new Color(110, 170, 218)); // 큰 배경 파란색

        // 아이콘 추가
        ImageIcon userIcon = new ImageIcon("C:\\new_java\\Logistics\\user_icon.png");
        JLabel iconLabel = new JLabel(new ImageIcon(userIcon.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH)));
        iconLabel.setBounds(315, 48, 170, 170);
        add(iconLabel);

        // 로그인 버튼 (둥근 모서리)
        JButton loginButton = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(141, 229, 191)); // 연한 초록색 배경
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // 둥근 모서리 설정
                super.paintComponent(g);
            }

            @Override
            public void paintBorder(Graphics g) {
                // 테두리 제거
            }

            @Override
            public boolean isContentAreaFilled() {
                return false;
            }

            @Override
            public boolean isBorderPainted() {
                return false;
            }

            @Override
            public boolean isFocusPainted() {
                return false;
            }

        };
        loginButton.setFont(new Font("D2CodingLigature Nerd Font Mono", Font.BOLD, 36));
        loginButton.setForeground(Color.WHITE); // 글자 색상 흰색
        loginButton.setBounds(310, 437, 180, 52); // 버튼 위치 및 크기 설정
        add(loginButton);

        // 중간 배경 패널
        JPanel midPanel = new JPanel();
        midPanel.setBounds(136, 137, 528, 326);
        midPanel.setBackground(new Color(145, 197, 234)); // 연한 파란색
        midPanel.setLayout(null); // 절대 위치로 설정
        add(midPanel);

        // 폰트 설정
        Font font = new Font("D2CodingLigature Nerd Font Mono", Font.PLAIN, 30);

        // ID 라벨 및 텍스트 필드
        /*JLabel idLabel = new JLabel("ID");
        idLabel.setFont(font);
        idLabel.setBounds(88, 50, 50, 30);
        midPanel.add(idLabel);*/

        JTextField idField = new JTextField();
        idField.setFont(font);
        idField.setBounds(84, 95, 353, 54); // 요청된 크기


        // ID 텍스트 필드 기본값 설정
        idField.setText("ID");
        idField.setForeground(Color.GRAY); // 기본값 색상 설정
        idField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (idField.getText().equals("ID")) {
                    idField.setText(""); // 입력 시 기본값 제거
                    idField.setForeground(Color.BLACK); // 입력 색상 변경
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (idField.getText().isEmpty()) {
                    idField.setText("ID"); // 포커스 해제 시 기본값 복원
                    idField.setForeground(Color.GRAY); // 기본값 색상 복원
                }
            }
        });
        midPanel.add(idField);


        JPasswordField pwField = new JPasswordField();
        pwField.setFont(font);
        pwField.setBounds(84, 175, 353, 54); // 요청된 크기

// PW 텍스트 필드 기본값 설정
        pwField.setText("PW");
        pwField.setForeground(Color.GRAY); // 기본값 색상 설정
        pwField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (new String(pwField.getPassword()).equals("PW")) {
                    pwField.setText(""); // 입력 시 기본값 제거
                    pwField.setForeground(Color.BLACK); // 입력 색상 변경
                    pwField.setEchoChar('●'); // 비밀번호 입력 마스킹 활성화
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (new String(pwField.getPassword()).isEmpty()) {
                    pwField.setText("PW"); // 포커스 해제 시 기본값 복원
                    pwField.setForeground(Color.GRAY); // 기본값 색상 복원
                    pwField.setEchoChar((char) 0); // 마스킹 비활성화 (기본 텍스트 표시)
                }
            }
        });
        midPanel.add(pwField);


        // Remember me 체크박스
        JCheckBox rememberMeCheckBox = new JCheckBox("Remember me");
        rememberMeCheckBox.setFont(new Font("D2CodingLigature Nerd Font Mono", Font.PLAIN, 15));
        rememberMeCheckBox.setBackground(new Color(145, 197, 234)); // 배경 투명하게 설정
        rememberMeCheckBox.setBounds(86, 254, 135, 22);
        midPanel.add(rememberMeCheckBox);

        // Forgot Password? 버튼
        JButton forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setFont(new Font("D2CodingLigature Nerd Font Mono", Font.PLAIN, 15)); // 글씨 크기 줄임
        forgotPasswordButton.setBorderPainted(false); // 테두리 제거
        forgotPasswordButton.setContentAreaFilled(false); // 버튼 배경 제거
        forgotPasswordButton.setFocusPainted(false); // 포커스 테두리 제거
        forgotPasswordButton.setBounds(290, 254, 170, 22);
        forgotPasswordButton.setForeground(Color.GRAY); // 글자 색상 설정
        midPanel.add(forgotPasswordButton);



        // 이벤트 처리: Forgot Password 버튼 클릭 시 빈 화면 출력
        forgotPasswordButton.addActionListener(e -> {
            JFrame forgotPasswordFrame = new JFrame("Forgot Password");
            forgotPasswordFrame.setSize(400, 600);
            forgotPasswordFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            forgotPasswordFrame.setVisible(true);
            forgotPasswordFrame.getContentPane().setBackground(Color.WHITE); // 빈 화면 흰색 배경 설정
        });

        // 이벤트 처리: 로그인 버튼 클릭 시 관리자/사용자 확인 및 경고 메시지 출력
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String pw = new String(pwField.getPassword());

                if (id.equals("root") && pw.equals("1234")) {
                    JFrame adminFrame = new JFrame("Admin Screen");
                    adminFrame.setSize(800, 600);
                    adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    adminFrame.getContentPane().setBackground(Color.WHITE); // 빈 화면 흰색 배경 설정
                    adminFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "아이디나 비밀번호가 올바르지 않습니다. 다시 입력하십시오.",
                            "로그인 실패",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LogisticsLoginUI ui = new LogisticsLoginUI();
            ui.setVisible(true);
            ui.setLocationRelativeTo(null); // 화면 중앙에 창 배치
        });
    }
}
