package citd.nhom99.ck.view;

import citd.nhom99.ck.controller.AppController;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.view.admin.ClassroomManagementPanel;
import citd.nhom99.ck.view.admin.StudentManagementPanel;
import citd.nhom99.ck.view.admin.TeacherManagementPanel;
import citd.nhom99.ck.view.teacher.MyClassroomPanel;

import javax.swing.*;
import java.awt.*;

public class MainContentPanel extends JPanel {

    private static final String WELCOME_MESSAGE = "Chào mừng đến với LMS!";
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    private final JLabel titleLabel;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public MainContentPanel(User user, AppController controller) {
        this.titleLabel = new JLabel(WELCOME_MESSAGE, SwingConstants.CENTER);
        this.cardLayout = new CardLayout();
        this.contentPanel = new JPanel(this.cardLayout);

        contentPanel.add(new HomePanel(), "Trang chủ");
        contentPanel.add(new AccountPanel(user, controller), "Tài khoản");
        contentPanel.add(new ClassroomManagementPanel(), "Quản lý Lớp học");
        contentPanel.add(new StudentManagementPanel(), "Quản lý Học sinh");
        contentPanel.add(new TeacherManagementPanel(), "Quản lý Giáo viên");
        contentPanel.add(new MyClassroomPanel(), "Lớp học của tôi");

        setLayout(new BorderLayout());
        titleLabel.setFont(TITLE_FONT);
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void updateMainContent(String text) {
        this.cardLayout.show(contentPanel, text);
        this.titleLabel.setText(text);
    }
}