package citd.nhom99.ck.view.admin;

import citd.nhom99.ck.controller.TeacherController;
import citd.nhom99.ck.model.constant.Gender;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.model.Teacher;
import citd.nhom99.ck.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeacherManagementPanel extends JPanel {
    private final TeacherController teacherController = new TeacherController();
    private JTable teacherTable;
    private DefaultTableModel tableModel;

    public TeacherManagementPanel() {
        JPanel teacherManagementPanel = new JPanel(new BorderLayout());
        teacherManagementPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = createHeaderPanel();
        teacherManagementPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel tablePanel = createTablePanel();
        teacherManagementPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        teacherManagementPanel.add(buttonPanel, BorderLayout.SOUTH);

        loadTeacherData();

        setLayout(new BorderLayout());
        add(teacherManagementPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Tìm");

//        searchButton.addActionListener(e -> performSearch());
//        searchField.addActionListener(e -> performSearch());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        headerPanel.add(searchPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());

        // Tạo table model với các cột
        String[] columnNames = {
                "ID", "Mã GV", "Họ và tên", "Email", "Số điện thoại", "Giới tính", "Lớp chủ nhiệm", "Môn dạy"
        };

        tableModel = new DefaultTableModel(columnNames, 0);

        teacherTable = new JTable(tableModel);
        teacherTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(teacherTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JButton addButton, editButton, deleteButton, refreshButton;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addButton = new JButton("Thêm giáo viên");
        editButton = new JButton("Sửa thông tin");
        deleteButton = new JButton("Xóa giáo viên");
        refreshButton = new JButton("Làm mới");

        // Thiết lập màu sắc cho buttons
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);

        editButton.setBackground(new Color(52, 152, 219));
        editButton.setForeground(Color.WHITE);

        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);

        refreshButton.setBackground(new Color(149, 165, 166));
        refreshButton.setForeground(Color.WHITE);

        addButton.addActionListener(e -> handleAddTeacher());
        editButton.addActionListener(e -> handleEditTeacher());
        deleteButton.addActionListener(e -> handleDeleteTeacher());
        refreshButton.addActionListener(e -> loadTeacherData());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        return buttonPanel;
    }

    private void handleAddTeacher() {
        JDialog addNewTeacherDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm giáo viên mới", true);
        addNewTeacherDialog.setLayout(new BorderLayout());
        addNewTeacherDialog.setSize(400, 500);
        addNewTeacherDialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField fullNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneNumberField = new JTextField(20);
        JComboBox<Gender> genderField = new JComboBox<>(Gender.values());

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1;
        formPanel.add(genderField, gbc);

        // Panel chứa form button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Lưu");

        buttonPanel.add(saveButton);

        // Thêm Dialog vào panel
        addNewTeacherDialog.add(formPanel, BorderLayout.CENTER);
        addNewTeacherDialog.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            System.out.println("Save teacher clicked");

            try {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String fullName = fullNameField.getText();
                String email = emailField.getText();
                String phoneNumber = phoneNumberField.getText();
                Gender gender = (Gender) genderField.getSelectedItem();

                User newTeacher = new User(username, password, fullName, phoneNumber, email, gender, Role.TEACHER);
                teacherController.createTeacher(newTeacher);
                JOptionPane.showMessageDialog(addNewTeacherDialog, "Thêm giáo viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                addNewTeacherDialog.dispose();
                loadTeacherData();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addNewTeacherDialog, "Lỗi khi thêm giáo viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error adding teacher: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        addNewTeacherDialog.setVisible(true);
    }

    private void handleEditTeacher() {
        System.out.println("Handle edit teacher clicked");
    }

    private void handleDeleteTeacher() {
        System.out.println("Handle delete teacher clicked");
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Xin vui lòng chọn dòng cần xóa trước.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int teacherUserId = (int) teacherTable.getValueAt(selectedRow, 0);
        try {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc xóa dữ liệu này?" + teacherUserId);
            if (confirm == JOptionPane.YES_OPTION) {
                teacherController.deleteTeacher(teacherUserId);
                loadTeacherData();
                JOptionPane.showMessageDialog(this, "Đã xóa ID: " + teacherUserId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTeacherData() {
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);

        try {
            List<Teacher> teachers = teacherController.getAllTeachers();
            for (Teacher teacher : teachers) {
                if (teacher.getUser() != null) {

                    Object[] rowData = {
                            teacher.getUser().getUserId(),
                            teacher.getTeacherCode(),
                            teacher.getUser().getFullName(),
                            teacher.getUser().getEmail(),
                            teacher.getUser().getPhoneNumber(),
                            teacher.getUser().getGender(),
                            teacher.getClassroomId() != 0 ? teacher.getClassroomId() : "Không chủ nhiệm",
                            teacher.getSubjectId()
                    };
                    tableModel.addRow(rowData);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu giáo viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
