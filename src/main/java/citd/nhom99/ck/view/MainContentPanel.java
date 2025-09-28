package citd.nhom99.ck.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import citd.nhom99.ck.controller.AppController;
import citd.nhom99.ck.model.Student;
import citd.nhom99.ck.model.Teacher;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.model.dao.StudentDAO;
import citd.nhom99.ck.model.dao.TeacherDAO;
import citd.nhom99.ck.view.admin.ClassroomManagementPanel;
import citd.nhom99.ck.view.admin.StudentManagementPanel;
import citd.nhom99.ck.view.admin.TeacherManagementPanel;
import citd.nhom99.ck.view.student.StudentPanel;
import citd.nhom99.ck.view.teacher.MyClassroomPanel;
import citd.nhom99.ck.view.teacher.TeacherPanel;

public class MainContentPanel extends JPanel {

    private static final String WELCOME_MESSAGE = "Chào mừng đến với LMS!";
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    private final JLabel titleLabel;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final User user;
    private final AppController controller;

    public MainContentPanel(User user, AppController controller) {
        this.user = user;
        this.controller = controller;
        this.titleLabel = new JLabel(WELCOME_MESSAGE, SwingConstants.CENTER);
        this.cardLayout = new CardLayout();
        this.contentPanel = new JPanel(this.cardLayout);

        initializePanels();

        setLayout(new BorderLayout());
        titleLabel.setFont(TITLE_FONT);
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void initializePanels() {
        contentPanel.add(new HomePanel(user, this::updateMainContent), "Trang chủ");
        contentPanel.add(new AccountPanel(user, controller), "Tài khoản");
        
        // Admin panels
        contentPanel.add(new ClassroomManagementPanel(), "Quản lý Lớp học");
        contentPanel.add(new StudentManagementPanel(), "Quản lý Học sinh");
        contentPanel.add(new TeacherManagementPanel(), "Quản lý Giáo viên");
        
        // Teacher panels
        contentPanel.add(new MyClassroomPanel(), "Lớp học của tôi");
        
        // Role-specific panels
        if (user.getRole() == Role.STUDENT) {
            Student student = getStudentByUserId(user.getUserId());
            if (student != null) {
                contentPanel.add(new StudentPanel(student, controller), "Thời khóa biểu");
                contentPanel.add(new StudentPanel(student, controller), "Điểm số");
            }
        } else if (user.getRole() == Role.TEACHER) {
            Teacher teacher = getTeacherByUserId(user.getUserId());
            if (teacher != null) {
                contentPanel.add(new TeacherPanel(teacher, controller), "Lịch dạy học");
                contentPanel.add(new TeacherPanel(teacher, controller), "Nhập điểm");
            }
        }
    }

    private Student getStudentByUserId(int userId) {
        try {
            StudentDAO studentDAO = new StudentDAO();
            return studentDAO.getStudentByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Teacher getTeacherByUserId(int userId) {
        try {
            TeacherDAO teacherDAO = new TeacherDAO();
            return teacherDAO.getTeacherByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateMainContent(String text) {
        this.cardLayout.show(contentPanel, text);
        this.titleLabel.setText(text);
    }
}