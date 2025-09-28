package citd.nhom99.ck.view;

import citd.nhom99.ck.controller.AppController;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.model.User;

import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {
    public SidebarPanel(DashboardFrame parent, User user, AppController controller) {
        setLayout(new GridLayout(8, 1, 0, 10));
        setBackground(new Color(50, 50, 70));
        setPreferredSize(new Dimension(200, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getFullName());
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(welcomeLabel);

        addMenuButton("Trang chủ", parent, null);

        if (user.getRole() == Role.ADMIN) {
            addMenuButton("Quản lý Học sinh", parent, null);
            addMenuButton("Quản lý Giáo viên", parent, null);
            addMenuButton("Quản lý Lớp học", parent, null);
        } else if (user.getRole() == Role.TEACHER) {
            addMenuButton("Lớp học của tôi", parent, null);
            addMenuButton("Sinh viên của tôi", parent, null);
        } else if (user.getRole() == Role.STUDENT) {
            addMenuButton("Lớp học của tôi", parent, null);
            addMenuButton("Điểm số", parent, null);
        }

        addMenuButton("Tài khoản", parent, null);
        addMenuButton("Đăng xuất", parent, controller::logout);
    }

    private void addMenuButton(String text, DashboardFrame parent, Runnable action) {
        JButton btn = new JButton(text);
        btn.addActionListener(e -> {
            if (action != null) {
                action.run();
            } else {
                parent.updateMainContent(text);
            }
        });
        add(btn);
    }
}
