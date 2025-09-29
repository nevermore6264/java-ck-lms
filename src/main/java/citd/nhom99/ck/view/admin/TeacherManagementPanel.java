package citd.nhom99.ck.view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import citd.nhom99.ck.controller.TeacherController;
import citd.nhom99.ck.model.Classroom;
import citd.nhom99.ck.model.Student;
import citd.nhom99.ck.model.Subject;
import citd.nhom99.ck.model.Teacher;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.constant.Gender;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.model.dao.ClassroomDAO;
import citd.nhom99.ck.model.dao.SubjectDAO;
import citd.nhom99.ck.model.dao.TeacherDAO;
import citd.nhom99.ck.utils.CustomDialog;
import citd.nhom99.ck.utils.SubjectTranslator;

public class TeacherManagementPanel extends JPanel {
    private final TeacherController teacherController = new TeacherController();
    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private List<Teacher> allTeachers;

    public TeacherManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tạo header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tạo table panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        // Tạo button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // Load dữ liệu ban đầu
        loadTeacherData();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(248, 249, 250));

        JLabel titleLabel = new JLabel("Quản lý Giáo viên");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 58, 64));
        titlePanel.add(titleLabel);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(new Color(248, 249, 250));

        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchLabel.setForeground(new Color(60, 60, 60));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setFont(new Font("Arial", Font.BOLD, 12));
        searchButton.setBackground(new Color(52, 144, 220));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(41, 128, 185));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(52, 144, 220));
            }
        });

        // Add search functionality
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(248, 249, 250));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Tạo table model với các cột
        String[] columnNames = {
                "ID", "Mã GV", "Họ và tên", "Email", "Số điện thoại", "Giới tính", "Lớp chủ nhiệm", "Môn dạy"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trong table
            }
        };

        teacherTable = new JTable(tableModel);
        teacherTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teacherTable.setRowHeight(35);
        teacherTable.setFont(new Font("Arial", Font.PLAIN, 13));
        teacherTable.setGridColor(new Color(220, 220, 220));
        teacherTable.setShowGrid(true);
        teacherTable.setIntercellSpacing(new Dimension(0, 1));

        // Add mouse listener for table clicks
        teacherTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = teacherTable.rowAtPoint(evt.getPoint());
                int col = teacherTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col >= 0) {
                    String columnName = teacherTable.getColumnName(col);
                    if ("Lớp chủ nhiệm".equals(columnName)) {
                        handleClassClick(row);
                    } else if ("Môn dạy".equals(columnName)) {
                        handleSubjectClick(row);
                    }
                }
            }
        });

        // Customize table header
        JTableHeader header = teacherTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(52, 58, 64));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Set custom renderer for "Lớp chủ nhiệm" column to make it appear clickable
        teacherTable.getColumn("Lớp chủ nhiệm").setCellRenderer(new ClickableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(teacherTable);
        scrollPane.setPreferredSize(new Dimension(1000, 500));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(248, 249, 250));

        JButton addButton = createStyledButton("Thêm giáo viên", new Color(46, 204, 113));
        JButton editButton = createStyledButton("Sửa thông tin", new Color(52, 152, 219));
        JButton deleteButton = createStyledButton("Xóa giáo viên", new Color(231, 76, 60));
        JButton viewDetailsButton = createStyledButton("Xem chi tiết", new Color(155, 89, 182));
        JButton assignClassButton = createStyledButton("Phân lớp", new Color(230, 126, 34));
        JButton refreshButton = createStyledButton("Làm mới", new Color(149, 165, 166));

        // Thêm action listeners
        addButton.addActionListener(e -> handleAddTeacher());
        editButton.addActionListener(e -> handleEditTeacher());
        deleteButton.addActionListener(e -> handleDeleteTeacher());
        viewDetailsButton.addActionListener(e -> handleViewTeacherDetails());
        assignClassButton.addActionListener(e -> handleAssignClass());
        refreshButton.addActionListener(e -> loadTeacherData());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(assignClassButton);
        buttonPanel.add(refreshButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        Color hoverColor = new Color(
                Math.max(0, backgroundColor.getRed() - 20),
                Math.max(0, backgroundColor.getGreen() - 20),
                Math.max(0, backgroundColor.getBlue() - 20)
        );

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void handleAddTeacher() {
        JDialog addNewTeacherDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm giáo viên mới", true);
        addNewTeacherDialog.setLayout(new BorderLayout());
        addNewTeacherDialog.setSize(500, 600);
        addNewTeacherDialog.setLocationRelativeTo(null);
        addNewTeacherDialog.getContentPane().setBackground(new Color(248, 249, 250));
        addNewTeacherDialog.setResizable(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(248, 249, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField usernameField = createStyledTextField(30);
        JPasswordField passwordField = createStyledPasswordField(30);
        JTextField fullNameField = createStyledTextField(30);
        JTextField emailField = createStyledTextField(30);
        JTextField phoneNumberField = createStyledTextField(30);

        JComboBox<String> genderField = createGenderComboBox();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(phoneNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Giới tính:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(genderField, gbc);

        // Panel chứa form button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton saveButton = new JButton("Lưu");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setPreferredSize(new Dimension(120, 45));
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
        cancelButton.setPreferredSize(new Dimension(120, 45));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add hover effects
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(34, 139, 34));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(40, 167, 69));
            }
        });

        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(90, 98, 104));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(108, 117, 125));
            }
        });

        // Add cancel action
        cancelButton.addActionListener(e -> addNewTeacherDialog.dispose());

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
                Gender gender = getGenderFromVietnamese((String) genderField.getSelectedItem());

                User newTeacher = new User(username, password, fullName, phoneNumber, email, gender, Role.TEACHER);
                teacherController.createTeacher(newTeacher);
                CustomDialog.showInfoDialog(addNewTeacherDialog, "Thêm giáo viên thành công!", "Thành công");
                addNewTeacherDialog.dispose();
                loadTeacherData();

            } catch (Exception ex) {
                CustomDialog.showWarningDialog(addNewTeacherDialog, "Lỗi khi thêm giáo viên: " + ex.getMessage(), "Lỗi");
                System.err.println("Error adding teacher: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        addNewTeacherDialog.setVisible(true);
    }

    private void handleEditTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giáo viên cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Teacher teacher = allTeachers.get(selectedRow);
        if (teacher.getUser() == null) {
            JOptionPane.showMessageDialog(this, "Không thể sửa thông tin giáo viên này.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create edit dialog
        JDialog editDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa thông tin giáo viên", true);
        editDialog.setLayout(new BorderLayout());
        editDialog.setSize(500, 600);
        editDialog.setLocationRelativeTo(null);
        editDialog.getContentPane().setBackground(new Color(248, 249, 250));
        editDialog.setResizable(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(248, 249, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Get current user data from database
        User currentUser = teacher.getUser();

        JTextField usernameField = createStyledTextField(30);
        usernameField.setText(currentUser.getUsername());
        JPasswordField passwordField = createStyledPasswordField(30);
        passwordField.setText(currentUser.getPassword());
        JTextField fullNameField = createStyledTextField(30);
        fullNameField.setText(currentUser.getFullName());
        JTextField emailField = createStyledTextField(30);
        emailField.setText(currentUser.getEmail());
        JTextField phoneNumberField = createStyledTextField(30);
        phoneNumberField.setText(currentUser.getPhoneNumber());

        JComboBox<String> genderField = createGenderComboBox();
        genderField.setSelectedItem(getGenderInVietnamese(currentUser.getGender()));

        // Add fields to form
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(phoneNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(createStyledLabel("Giới tính:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(genderField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton saveButton = new JButton("Lưu");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setPreferredSize(new Dimension(120, 45));
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
        cancelButton.setPreferredSize(new Dimension(120, 45));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effects
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(34, 139, 34));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(40, 167, 69));
            }
        });

        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(90, 98, 104));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(108, 117, 125));
            }
        });

        saveButton.addActionListener(e -> {
            // Validation
            if (usernameField.getText().trim().isEmpty() ||
                    new String(passwordField.getPassword()).trim().isEmpty() ||
                    fullNameField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    phoneNumberField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(editDialog, "Vui lòng điền đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Email validation
            if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(editDialog, "Email không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Phone validation
            if (!phoneNumberField.getText().matches("^[0-9]{10,11}$")) {
                JOptionPane.showMessageDialog(editDialog, "Số điện thoại phải có 10-11 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Create updated user
                User updatedUser = new User();
                updatedUser.setUserId(currentUser.getUserId());
                updatedUser.setUsername(usernameField.getText().trim());
                updatedUser.setPassword(new String(passwordField.getPassword()).trim());
                updatedUser.setFullName(fullNameField.getText().trim());
                updatedUser.setEmail(emailField.getText().trim());
                updatedUser.setPhoneNumber(phoneNumberField.getText().trim());
                updatedUser.setGender(getGenderFromVietnamese((String) genderField.getSelectedItem()));
                updatedUser.setRole(Role.TEACHER);

                // Update teacher
                teacher.setUser(updatedUser);
                teacherController.updateTeacher(teacher);

                CustomDialog.showInfoDialog(editDialog, "Cập nhật thông tin giáo viên thành công!", "Thành công");
                editDialog.dispose();
                loadTeacherData();
            } catch (Exception ex) {
                CustomDialog.showWarningDialog(editDialog, "Lỗi khi cập nhật thông tin: " + ex.getMessage(), "Lỗi");
            }
        });

        cancelButton.addActionListener(e -> editDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        editDialog.add(formPanel, BorderLayout.CENTER);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);
        editDialog.setVisible(true);
    }

    private void handleDeleteTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            CustomDialog.showWarningDialog(this, "Vui lòng chọn giáo viên cần xóa!", "Cảnh báo");
            return;
        }

        Teacher teacher = allTeachers.get(selectedRow);
        if (teacher.getUser() == null) {
            CustomDialog.showWarningDialog(this, "Không thể xóa giáo viên này!", "Lỗi");
            return;
        }

        // Create detailed confirmation message
        String message = String.format(
                "Bạn có chắc chắn muốn xóa giáo viên này?\n\n" +
                        "• Mã GV: %s\n" +
                        "• Họ và tên: %s\n" +
                        "• Email: %s\n" +
                        "• Số điện thoại: %s\n" +
                        "• Giới tính: %s\n" +
                        "• Lớp chủ nhiệm: %s",
                teacher.getTeacherCode(),
                teacher.getUser().getFullName(),
                teacher.getUser().getEmail(),
                teacher.getUser().getPhoneNumber(),
                teacher.getUser().getGender() != null ? getGenderInVietnamese(teacher.getUser().getGender()) : "Không xác định",
                teacher.getClassroomId() != 0 ? "Lớp " + teacher.getClassroomId() : "Không chủ nhiệm"
        );

        boolean confirm = CustomDialog.showConfirmDialog(this, message, "Xác nhận xóa giáo viên");
        
        if (confirm) {
            try {
                teacherController.deleteTeacher(teacher.getUser().getUserId());
                loadTeacherData();
                CustomDialog.showInfoDialog(this, "Đã xóa giáo viên thành công!", "Thành công");
            } catch (Exception e) {
                CustomDialog.showWarningDialog(this, "Lỗi khi xóa giáo viên: " + e.getMessage(), "Lỗi");
            }
        }
    }

    private void loadTeacherData() {
        tableModel.setRowCount(0);

        try {
            allTeachers = teacherController.getAllTeachers();
            displayTeachers(allTeachers);

            // Check for data inconsistencies (only in debug mode)
            if (System.getProperty("debug") != null) {
                ClassroomDAO classroomDAO = new ClassroomDAO();
                classroomDAO.checkDataConsistency();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu giáo viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayTeachers(List<Teacher> teachers) {
        tableModel.setRowCount(0);
        for (Teacher teacher : teachers) {
            if (teacher.getUser() != null) {
                // Get classroom name by finding classroom where this teacher is GVCN
                String classroomName = "Không chủ nhiệm";
                try {
                    ClassroomDAO classroomDAO = new ClassroomDAO();
                    List<Classroom> allClassrooms = classroomDAO.getAllClassrooms();
                    for (Classroom classroom : allClassrooms) {
                        if (classroom.getTeacherId() == teacher.getUser().getUserId()) {
                            classroomName = classroom.getClassName();
                            break;
                        }
                    }
                } catch (Exception e) {
                    classroomName = "Không chủ nhiệm";
                }

                // Get subject name by subject ID
                String subjectName = "Chưa phân môn";
                if (teacher.getSubjectId() != 0) {
                    try {
                        SubjectDAO subjectDAO = new SubjectDAO();
                        Subject subject = subjectDAO.getSubjectById(teacher.getSubjectId());
                        if (subject != null) {
                            subjectName = SubjectTranslator.convertToVietnamese(subject.getSubjectName());
                        }
                    } catch (Exception e) {
                        subjectName = "Môn " + teacher.getSubjectId();
                    }
                }

                Object[] rowData = {
                        teacher.getUser().getUserId(),
                        teacher.getTeacherCode(),
                        teacher.getUser().getFullName(),
                        teacher.getUser().getEmail(),
                        teacher.getUser().getPhoneNumber(),
                        getGenderInVietnamese(teacher.getUser().getGender()),
                        classroomName,
                        subjectName
                };
                tableModel.addRow(rowData);
            }
        }
    }

    private void performSearch() {
        if (allTeachers == null) return;

        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            displayTeachers(allTeachers);
            return;
        }

        List<Teacher> filteredTeachers = allTeachers.stream()
                .filter(teacher ->
                        teacher.getUser() != null && (
                                teacher.getUser().getFullName().toLowerCase().contains(searchText) ||
                                        teacher.getTeacherCode().toLowerCase().contains(searchText) ||
                                        teacher.getUser().getEmail().toLowerCase().contains(searchText) ||
                                        teacher.getUser().getPhoneNumber().toLowerCase().contains(searchText)
                        )
                )
                .collect(java.util.stream.Collectors.toList());

        displayTeachers(filteredTeachers);
    }

    // Helper method to convert gender to Vietnamese
    private String getGenderInVietnamese(Gender gender) {
        if (gender == null) {
            return "Không xác định";
        }
        return switch (gender) {
            case MALE -> "Nam";
            case FEMALE -> "Nữ";
            default -> "Không xác định";
        };
    }
    
    private JComboBox<String> createGenderComboBox() {
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Nam", "Nữ"});
        genderComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        genderComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return genderComboBox;
    }
    
    private Gender getGenderFromVietnamese(String vietnameseGender) {
        return switch (vietnameseGender) {
            case "Nam" -> Gender.MALE;
            case "Nữ" -> Gender.FEMALE;
            default -> Gender.MALE;
        };
    }
    
    // Helper methods for styled components
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        label.setPreferredSize(new Dimension(120, 25));
        return label;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return textField;
    }

    private JPasswordField createStyledPasswordField(int columns) {
        JPasswordField passwordField = new JPasswordField(columns);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return passwordField;
    }

    private void handleClassClick(int row) {
        if (row >= 0 && row < allTeachers.size()) {
            Teacher teacher = allTeachers.get(row);

            // Find classroom where this teacher is GVCN
            ClassroomDAO classroomDAO = new ClassroomDAO();
            List<Classroom> allClassrooms = classroomDAO.getAllClassrooms();
            Classroom classroom = null;
            for (Classroom c : allClassrooms) {
                if (c.getTeacherId() == teacher.getUser().getUserId()) {
                    classroom = c;
                    break;
                }
            }

            if (classroom != null) {

                // Build student list
                StringBuilder studentList = new StringBuilder();
                if (classroom.getStudents() != null && !classroom.getStudents().isEmpty()) {
                    studentList.append("Danh sách học sinh:\n");
                    for (int i = 0; i < classroom.getStudents().size(); i++) {
                        Student student = classroom.getStudents().get(i);
                        if (student != null && student.getUser() != null) {
                            studentList.append(String.format("%d. %s (%s)\n",
                                    i + 1,
                                    student.getUser().getFullName(),
                                    student.getStudentCode()));
                        } else {
                            studentList.append(String.format("%d. [Dữ liệu không hợp lệ]\n", i + 1));
                        }
                    }
                } else {
                    studentList.append("Lớp chưa có học sinh nào.");
                }

                String message = String.format(
                        "Thông tin lớp chủ nhiệm:\n\n" +
                                "• Giáo viên: %s\n" +
                                "• Mã GV: %s\n" +
                                "• Lớp chủ nhiệm: %s\n" +
                                "• Sĩ số: %d học sinh\n\n" +
                                "%s",
                        teacher.getUser().getFullName(),
                        teacher.getTeacherCode(),
                        classroom.getClassName(),
                        classroom.getStudents() != null ? classroom.getStudents().size() : 0,
                        studentList.toString()
                );

                // Create custom dialog for class details
                JDialog classDialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Thông tin lớp chủ nhiệm", true);
                classDialog.setSize(500, 400);
                classDialog.setLocationRelativeTo(this);
                classDialog.setResizable(false);
                
                // Main panel
                JPanel mainPanel = new JPanel(new BorderLayout());
                mainPanel.setBackground(Color.WHITE);
                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
                
                // Header panel
                JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                headerPanel.setBackground(Color.WHITE);
                
                JLabel titleLabel = new JLabel("Thông tin lớp chủ nhiệm");
                titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
                titleLabel.setForeground(new Color(52, 144, 220));
                headerPanel.add(titleLabel);
                
                // Content panel
                JPanel contentPanel = new JPanel(new java.awt.GridBagLayout());
                contentPanel.setBackground(Color.WHITE);
                contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
                
                java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
                gbc.insets = new java.awt.Insets(8, 10, 8, 10);
                gbc.anchor = java.awt.GridBagConstraints.WEST;
                
                // Class information
                String[][] infoData = {
                    {"Giáo viên:", teacher.getUser().getFullName()},
                    {"Mã GV:", teacher.getTeacherCode()},
                    {"Lớp chủ nhiệm:", classroom.getClassName()},
                    {"Sĩ số:", String.valueOf(classroom.getStudents() != null ? classroom.getStudents().size() : 0) + " học sinh"}
                };
                
                for (int i = 0; i < infoData.length; i++) {
                    gbc.gridx = 0;
                    gbc.gridy = i;
                    gbc.fill = java.awt.GridBagConstraints.NONE;
                    gbc.weightx = 0.0;
                    JLabel labelLabel = new JLabel(infoData[i][0]);
                    labelLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    labelLabel.setForeground(new Color(52, 58, 64));
                    contentPanel.add(labelLabel, gbc);
                    
                    gbc.gridx = 1;
                    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                    gbc.weightx = 1.0;
                    JLabel valueLabel = new JLabel(infoData[i][1]);
                    valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    valueLabel.setForeground(Color.BLACK);
                    contentPanel.add(valueLabel, gbc);
                }
                
                // Student list
                if (classroom.getStudents() != null && !classroom.getStudents().isEmpty()) {
                    gbc.gridx = 0;
                    gbc.gridy = infoData.length;
                    gbc.gridwidth = 2;
                    gbc.fill = java.awt.GridBagConstraints.BOTH;
                    gbc.weightx = 1.0;
                    gbc.weighty = 1.0;
                    
                    JLabel studentListLabel = new JLabel("<html><b>Danh sách học sinh:</b><br/>");
                    studentListLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    studentListLabel.setForeground(new Color(52, 58, 64));
                    
                    StringBuilder studentListHtml = new StringBuilder();
                    for (int i = 0; i < classroom.getStudents().size(); i++) {
                        Student student = classroom.getStudents().get(i);
                        if (student != null && student.getUser() != null) {
                            studentListHtml.append(String.format("%d. %s (%s)<br/>",
                                    i + 1,
                                    student.getUser().getFullName(),
                                    student.getStudentCode()));
                        } else {
                            studentListHtml.append(String.format("%d. [Dữ liệu không hợp lệ]<br/>", i + 1));
                        }
                    }
                    studentListLabel.setText("<html><b>Danh sách học sinh:</b><br/>" + studentListHtml.toString() + "</html>");
                    
                    contentPanel.add(studentListLabel, gbc);
                } else {
                    gbc.gridx = 0;
                    gbc.gridy = infoData.length;
                    gbc.gridwidth = 2;
                    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                    gbc.weightx = 1.0;
                    
                    JLabel noStudentLabel = new JLabel("Lớp chưa có học sinh nào.");
                    noStudentLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                    noStudentLabel.setForeground(new Color(108, 117, 125));
                    contentPanel.add(noStudentLabel, gbc);
                }
                
                // Button panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                buttonPanel.setBackground(Color.WHITE);
                
                JButton closeButton = new JButton("Hủy");
                closeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
                closeButton.setBackground(new Color(108, 117, 125));
                closeButton.setForeground(Color.WHITE);
                closeButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
                closeButton.setFocusPainted(false);
                closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                closeButton.setPreferredSize(new Dimension(100, 40));
                
                // Add hover effect
                closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        closeButton.setBackground(new Color(90, 98, 104));
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        closeButton.setBackground(new Color(108, 117, 125));
                    }
                });
                
                closeButton.addActionListener(e -> classDialog.dispose());
                
                buttonPanel.add(closeButton);
                
                mainPanel.add(headerPanel, BorderLayout.NORTH);
                mainPanel.add(contentPanel, BorderLayout.CENTER);
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                classDialog.add(mainPanel);
                classDialog.setVisible(true);

            } else {
                CustomDialog.showInfoDialog(this, "Giáo viên chưa được phân lớp chủ nhiệm!", "Thông báo");
            }
        }
    }

    private void handleSubjectClick(int row) {
        if (row >= 0 && row < allTeachers.size()) {
            Teacher teacher = allTeachers.get(row);
            if (teacher.getSubjectId() != 0) {
                // Get subject name
                String subjectName = "Môn " + teacher.getSubjectId();
                try {
                    SubjectDAO subjectDAO = new SubjectDAO();
                    Subject subject = subjectDAO.getSubjectById(teacher.getSubjectId());
                    if (subject != null) {
                        subjectName = SubjectTranslator.convertToVietnamese(subject.getSubjectName());
                    }
                } catch (Exception e) {
                    subjectName = "Môn " + teacher.getSubjectId();
                }

                String message = String.format(
                        "Thông tin môn dạy:\n\n" +
                                "• Giáo viên: %s\n" +
                                "• Mã GV: %s\n" +
                                "• Môn dạy: %s\n" +
                                "• ID môn: %d",
                        teacher.getUser().getFullName(),
                        teacher.getTeacherCode(),
                        subjectName,
                        teacher.getSubjectId()
                );

                JOptionPane.showMessageDialog(
                        this,
                        message,
                        "Thông tin môn dạy",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(this, "Giáo viên chưa được phân môn dạy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void handleViewTeacherDetails() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giáo viên để xem chi tiết!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedRow >= 0 && selectedRow < allTeachers.size()) {
            Teacher teacher = allTeachers.get(selectedRow);

            // Create custom dialog
            JDialog detailsDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết giáo viên", true);
            detailsDialog.setSize(500, 400);
            detailsDialog.setLocationRelativeTo(this);
            detailsDialog.setResizable(false);

            // Main panel
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(Color.WHITE);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            
            // Header panel
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            headerPanel.setBackground(Color.WHITE);
            
            JLabel titleLabel = new JLabel("Thông tin chi tiết giáo viên");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(new Color(52, 144, 220));
            headerPanel.add(titleLabel);
            
            // Content panel
            JPanel contentPanel = new JPanel(new java.awt.GridBagLayout());
            contentPanel.setBackground(Color.WHITE);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            
            java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
            gbc.insets = new java.awt.Insets(8, 10, 8, 10);
            gbc.anchor = java.awt.GridBagConstraints.WEST;

            // Get classroom name
            String classroomName = "Chưa phân lớp";
            if (teacher.getClassroomId() != 0) {
                try {
                    ClassroomDAO classroomDAO = new ClassroomDAO();
                    Classroom classroom = classroomDAO.getClassroomById(teacher.getClassroomId());
                    if (classroom != null) {
                        classroomName = classroom.getClassName();
                    }
                } catch (Exception e) {
                    classroomName = "Lớp " + teacher.getClassroomId();
                }
            }

            // Get subject name
            String subjectName = "Chưa phân môn";
            if (teacher.getSubjectId() != 0) {
                try {
                    SubjectDAO subjectDAO = new SubjectDAO();
                    Subject subject = subjectDAO.getSubjectById(teacher.getSubjectId());
                    if (subject != null) {
                        subjectName = SubjectTranslator.convertToVietnamese(subject.getSubjectName());
                    }
                } catch (Exception e) {
                    subjectName = "Môn " + teacher.getSubjectId();
                }
            }

            // Teacher information
            String[][] infoData = {
                {"Mã GV:", teacher.getTeacherCode()},
                {"Họ tên:", teacher.getUser().getFullName()},
                {"Email:", teacher.getUser().getEmail()},
                {"Số điện thoại:", teacher.getUser().getPhoneNumber()},
                {"Giới tính:", getGenderInVietnamese(teacher.getUser().getGender())},
                {"Lớp chủ nhiệm:", classroomName},
                {"Môn dạy:", subjectName}
            };
            
            for (int i = 0; i < infoData.length; i++) {
                gbc.gridx = 0;
                gbc.gridy = i;
                gbc.fill = java.awt.GridBagConstraints.NONE;
                gbc.weightx = 0.0;
                JLabel labelLabel = new JLabel(infoData[i][0]);
                labelLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                labelLabel.setForeground(new Color(52, 58, 64));
                contentPanel.add(labelLabel, gbc);
                
                gbc.gridx = 1;
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                JLabel valueLabel = new JLabel(infoData[i][1]);
                valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                valueLabel.setForeground(Color.BLACK);
                contentPanel.add(valueLabel, gbc);
            }

            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            buttonPanel.setBackground(Color.WHITE);
            
            JButton closeButton = new JButton("Hủy");
            closeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            closeButton.setBackground(new Color(108, 117, 125));
            closeButton.setForeground(Color.WHITE);
            closeButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            closeButton.setFocusPainted(false);
            closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            closeButton.setPreferredSize(new Dimension(100, 40));
            
            // Add hover effect
            closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    closeButton.setBackground(new Color(90, 98, 104));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    closeButton.setBackground(new Color(108, 117, 125));
                }
            });
            
            closeButton.addActionListener(e -> detailsDialog.dispose());
            
            buttonPanel.add(closeButton);

            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            detailsDialog.add(mainPanel);
            detailsDialog.setVisible(true);
        }
    }

    private void handleAssignClass() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giáo viên để phân lớp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedRow >= 0 && selectedRow < allTeachers.size()) {
            Teacher teacher = allTeachers.get(selectedRow);

            JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Phân lớp cho giáo viên", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(null);
            dialog.getContentPane().setBackground(new Color(248, 249, 250));

            // Header panel
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            headerPanel.setBackground(new Color(248, 249, 250));
            JLabel headerLabel = new JLabel("Phân lớp cho: " + teacher.getUser().getFullName());
            headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
            headerLabel.setForeground(new Color(52, 58, 64));
            headerPanel.add(headerLabel);

            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(new Color(248, 249, 250));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;

            // Load available classrooms
            List<Classroom> classrooms = new ArrayList<>();
            try {
                ClassroomDAO classroomDAO = new ClassroomDAO();
                classrooms = classroomDAO.getAllClassrooms();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi tải danh sách lớp học: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] classNames = new String[classrooms.size() + 1];
            classNames[0] = "Chưa phân lớp";
            for (int i = 0; i < classrooms.size(); i++) {
                classNames[i + 1] = classrooms.get(i).getClassName();
            }

            JComboBox<String> classComboBox = new JComboBox<>(classNames);
            classComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
            classComboBox.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));

            // Set current classroom if any
            if (teacher.getClassroomId() != 0) {
                for (Classroom classroom : classrooms) {
                    if (classroom.getClassId() == teacher.getClassroomId()) {
                        classComboBox.setSelectedItem(classroom.getClassName());
                        break;
                    }
                }
            }

            gbc.gridx = 0;
            gbc.gridy = 0;
            JLabel classLabel = new JLabel("Lớp học:");
            classLabel.setFont(new Font("Arial", Font.BOLD, 14));
            classLabel.setForeground(new Color(60, 60, 60));
            formPanel.add(classLabel, gbc);

            gbc.gridx = 1;
            formPanel.add(classComboBox, gbc);

            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            buttonPanel.setBackground(new Color(248, 249, 250));

            JButton assignButton = new JButton("Phân lớp");
            assignButton.setFont(new Font("Arial", Font.BOLD, 13));
            assignButton.setBackground(new Color(46, 204, 113));
            assignButton.setForeground(Color.WHITE);
            assignButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            assignButton.setFocusPainted(false);
            assignButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JButton cancelButton = new JButton("Hủy");
            cancelButton.setFont(new Font("Arial", Font.BOLD, 13));
            cancelButton.setBackground(new Color(149, 165, 166));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            cancelButton.setFocusPainted(false);
            cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            buttonPanel.add(assignButton);
            buttonPanel.add(cancelButton);

            dialog.add(headerPanel, BorderLayout.NORTH);
            dialog.add(formPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            // Event handlers
            cancelButton.addActionListener(e -> dialog.dispose());

            final List<Classroom> finalClassrooms = classrooms;
            assignButton.addActionListener(e -> {
                try {
                    String selectedClassName = (String) classComboBox.getSelectedItem();
                    if (selectedClassName != null) {
                        int newClassroomId = 0;
                        Classroom selectedClassroom = null;

                        if (!selectedClassName.equals("Chưa phân lớp")) {
                            for (Classroom classroom : finalClassrooms) {
                                if (classroom.getClassName().equals(selectedClassName)) {
                                    newClassroomId = classroom.getClassId();
                                    selectedClassroom = classroom;
                                    break;
                                }
                            }

                            // Check if the classroom already has a homeroom teacher
                            if (selectedClassroom != null) {
                                try {
                                    ClassroomDAO classroomDAO = new ClassroomDAO();
                                    // Get fresh data from database to ensure accuracy
                                    Classroom freshClassroom = classroomDAO.getClassroomById(selectedClassroom.getClassId());

                                    if (freshClassroom != null && freshClassroom.getTeacherId() != 0) {
                                        // Get the current homeroom teacher's name
                                        String currentTeacherName = "Giáo viên ID " + freshClassroom.getTeacherId();
                                        try {
                                            TeacherDAO teacherDAO = new TeacherDAO();
                                            Teacher currentTeacher = teacherDAO.getTeacherByUserId(freshClassroom.getTeacherId());
                                            if (currentTeacher != null && currentTeacher.getUser() != null) {
                                                currentTeacherName = currentTeacher.getUser().getFullName() + " (" + currentTeacher.getTeacherCode() + ")";
                                            }
                                        } catch (Exception ex) {
                                            // Use default name if can't get teacher info
                                        }

                                        String errorMessage = String.format(
                                                "❌ Không thể phân lớp!\n\n" +
                                                        "Lớp %s đã có giáo viên chủ nhiệm: %s.\n\n" +
                                                        "Một lớp chỉ có thể có một giáo viên chủ nhiệm duy nhất.\n\n" +
                                                        "Vui lòng chọn lớp khác hoặc gỡ bỏ giáo viên hiện tại khỏi lớp %s trước.",
                                                freshClassroom.getClassName(),
                                                currentTeacherName,
                                                freshClassroom.getClassName()
                                        );

                                        JOptionPane.showMessageDialog(dialog, errorMessage, "Lỗi - Lớp đã có giáo viên chủ nhiệm", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(dialog, "Lỗi khi kiểm tra thông tin lớp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                        }

                        // Update teacher's classroom
                        teacher.setClassroomId(newClassroomId);
                        teacherController.updateTeacher(teacher);

                        // Also update classroom's gvcn_id to maintain consistency
                        if (selectedClassroom != null) {
                            selectedClassroom.setTeacherId(teacher.getUser().getUserId());
                            ClassroomDAO classroomDAO = new ClassroomDAO();
                            classroomDAO.updateClassroom(selectedClassroom);
                            System.out.println("DEBUG: Updated classroom " + selectedClassroom.getClassId() + " with teacher ID " + teacher.getUser().getUserId());
                        }

                        JOptionPane.showMessageDialog(dialog, "Phân lớp thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadTeacherData(); // Refresh the table
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi phân lớp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            dialog.setVisible(true);
        }
    }

    // Custom cell renderer for clickable columns
    private class ClickableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String cellValue = value != null ? value.toString() : "";

            // Only apply clickable styling if the teacher has a homeroom class
            if (!cellValue.equals("Không chủ nhiệm") && !cellValue.isEmpty()) {
                // Set blue color, bold, and italic for clickable appearance
                setForeground(new Color(0, 100, 200)); // Blue color
                setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 13)); // Bold and italic

                // Add underline to make it more obvious it's clickable
                setText("<html><u>" + cellValue + "</u></html>");

                // Set cursor to hand cursor when hovering
                setCursor(new Cursor(Cursor.HAND_CURSOR));

                // Add subtle background highlight for better visibility
                if (!isSelected) {
                    setBackground(new Color(240, 248, 255)); // Light blue background
                }
            } else {
                // Normal styling for "Không chủ nhiệm" or empty values
                setForeground(Color.BLACK);
                setFont(new Font("Arial", Font.PLAIN, 13));
                setText(cellValue);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                if (!isSelected) {
                    setBackground(Color.WHITE);
                }
            }

            return this;
        }
    }
}
