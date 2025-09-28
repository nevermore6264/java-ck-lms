package citd.nhom99.ck.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import citd.nhom99.ck.controller.AppController;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.constant.Role;

public class SidebarPanel extends JPanel {
    public SidebarPanel(DashboardFrame parent, User user, AppController controller) {
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 70));
        setPreferredSize(new Dimension(200, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 50, 70));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getFullName());
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        
        add(headerPanel, BorderLayout.NORTH);

        // Menu Panel
        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        menuPanel.setBackground(new Color(50, 50, 70));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        addMenuButton("Trang chủ", parent, null, menuPanel);

        if (user.getRole() == Role.ADMIN) {
            addMenuButton("Quản lý Học sinh", parent, null, menuPanel);
            addMenuButton("Quản lý Giáo viên", parent, null, menuPanel);
            addMenuButton("Quản lý Lớp học", parent, null, menuPanel);
        } else if (user.getRole() == Role.TEACHER) {
            addMenuButton("Lịch dạy học", parent, null, menuPanel);
            addMenuButton("Nhập điểm", parent, null, menuPanel);
            addMenuButton("Lớp học của tôi", parent, null, menuPanel);
        } else if (user.getRole() == Role.STUDENT) {
            addMenuButton("Thời khóa biểu", parent, null, menuPanel);
            addMenuButton("Điểm số", parent, null, menuPanel);
        }

        addMenuButton("Tài khoản", parent, null, menuPanel);
        
        add(menuPanel, BorderLayout.CENTER);

        // Logout Button Panel
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setBackground(new Color(50, 50, 70));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> controller.logout());
        
        // Add hover effect
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(200, 35, 51));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(220, 53, 69));
            }
        });
        
        logoutPanel.add(logoutButton, BorderLayout.CENTER);
        add(logoutPanel, BorderLayout.SOUTH);
    }

    private void addMenuButton(String text, DashboardFrame parent, Runnable action, JPanel panel) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 11));
        btn.setBackground(new Color(70, 70, 90));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(90, 90, 110));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(70, 70, 90));
            }
        });
        
        btn.addActionListener(e -> {
            if (action != null) {
                action.run();
            } else {
                parent.updateMainContent(text);
            }
        });
        panel.add(btn);
    }
}
