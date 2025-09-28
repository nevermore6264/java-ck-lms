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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import citd.nhom99.ck.controller.StudentController;
import citd.nhom99.ck.model.Student;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.constant.Gender;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.model.dao.UserDAO;

public class StudentManagementPanel extends JPanel {
    private final UserDAO userDAO = new UserDAO();
    private final StudentController studentController = new StudentController();
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private List<Student> allStudents;


    public StudentManagementPanel() {
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
        loadStudentData();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(248, 249, 250));
        
        JLabel titleLabel = new JLabel("Quản lý Học sinh");
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
                "ID", "Mã SV", "Họ và tên", "Email", "Số điện thoại", "Giới tính", "Lớp", "Điểm"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trong table
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setRowHeight(35);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 13));
        studentTable.setGridColor(new Color(220, 220, 220));
        studentTable.setShowGrid(true);
        studentTable.setIntercellSpacing(new Dimension(0, 1));
        
        // Customize table header
        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(52, 58, 64));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setPreferredSize(new Dimension(1000, 500));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(248, 249, 250));

        JButton addButton = createStyledButton("Thêm học sinh", new Color(46, 204, 113));
        JButton editButton = createStyledButton("Sửa thông tin", new Color(52, 152, 219));
        JButton deleteButton = createStyledButton("Xóa học sinh", new Color(231, 76, 60));
        JButton refreshButton = createStyledButton("Làm mới", new Color(149, 165, 166));

        // Thêm action listeners
        addButton.addActionListener(e -> handleCreateStudent());
        editButton.addActionListener(e -> handleEditStudent());
        deleteButton.addActionListener(e -> handleDeleteStudent());
        refreshButton.addActionListener(e -> loadStudentData());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
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

    private void handleCreateStudent() {
        System.out.println("Add student clicked");
        // Tạo form Dialog để nhập thông tin học sinh
        JDialog addNewStudentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm học sinh mới", true);
        addNewStudentDialog.setLayout(new BorderLayout());
        addNewStudentDialog.setSize(500, 650);
        addNewStudentDialog.setLocationRelativeTo(null);
        addNewStudentDialog.getContentPane().setBackground(new Color(248, 249, 250));

        // Tạo Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(248, 249, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Tạo input field với styling
        JTextField usernameField = createStyledTextField(30);
        JPasswordField passwordField = createStyledPasswordField(30);
        JTextField fullNameField = createStyledTextField(30);
        JTextField emailField = createStyledTextField(30);
        JTextField phoneNumberField = createStyledTextField(30);
        JComboBox<Gender> genderField = createStyledComboBox(Gender.values());

        // Thêm label cho input field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createStyledLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createStyledLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createStyledLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(createStyledLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(createStyledLabel("Giới tính:"), gbc);
        gbc.gridx = 1;
        formPanel.add(genderField, gbc);

        // Panel chứa form button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(248, 249, 250));
        JButton saveButton = createStyledButton("Lưu", new Color(46, 204, 113));
        JButton cancelButton = createStyledButton("Hủy", new Color(149, 165, 166));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Thêm Dialog vào panel
        addNewStudentDialog.add(formPanel, BorderLayout.CENTER);
        addNewStudentDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Xử lý sự kiện hủy
        cancelButton.addActionListener(e -> addNewStudentDialog.dispose());

        // Xử lý sự kiện lưu
        saveButton.addActionListener(e -> {
            System.out.println("Save student clicked");
            try {
                // Validation
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String fullName = fullNameField.getText().trim();
                String email = emailField.getText().trim();
                String phoneNumber = phoneNumberField.getText().trim();
                Gender gender = (Gender) genderField.getSelectedItem();

                // Kiểm tra các trường bắt buộc
                if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(addNewStudentDialog, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kiểm tra email format
                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    JOptionPane.showMessageDialog(addNewStudentDialog, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kiểm tra phone number format
                if (!phoneNumber.matches("^[0-9]{10,11}$")) {
                    JOptionPane.showMessageDialog(addNewStudentDialog, "Số điện thoại phải có 10-11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User newStudent = new User(username, password, fullName, phoneNumber, email, gender, Role.STUDENT);

                studentController.createStudent(newStudent);
                JOptionPane.showMessageDialog(addNewStudentDialog, "Thêm học sinh thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("New student added: " + newStudent);

                // Đóng dialog và refresh dữ liệu
                addNewStudentDialog.dispose();
                loadStudentData();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(addNewStudentDialog, "Lỗi khi thêm sinh viên: " + exception.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                exception.printStackTrace();
            }
        });

        addNewStudentDialog.setVisible(true);
    }

    private void handleEditStudent() {
        System.out.println("Edit student clicked");
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học sinh cần chỉnh sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy thông tin học sinh hiện tại từ table
        Integer userId = (Integer) studentTable.getValueAt(selectedRow, 0);
        String studentCode = (String) studentTable.getValueAt(selectedRow, 1);
        String currentFullName = (String) studentTable.getValueAt(selectedRow, 2);
        String currentEmail = (String) studentTable.getValueAt(selectedRow, 3);
        String currentPhoneNumber = (String) studentTable.getValueAt(selectedRow, 4);

        JDialog editStudentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa thông tin học sinh", true);
        editStudentDialog.setLayout(new BorderLayout());
        editStudentDialog.setSize(500, 550);
        editStudentDialog.setLocationRelativeTo(null);
        editStudentDialog.getContentPane().setBackground(new Color(248, 249, 250));

        // Tạo Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(248, 249, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Hiển thị thông tin không thể chỉnh sửa
        JLabel userIdLabel = new JLabel(userId.toString());
        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        userIdLabel.setForeground(new Color(100, 100, 100));
        
        JLabel studentCodeLabel = new JLabel(studentCode);
        studentCodeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        studentCodeLabel.setForeground(new Color(100, 100, 100));

        // Tạo input field với dữ liệu hiện tại
        JTextField fullNameField = createStyledTextField(30);
        fullNameField.setText(currentFullName);
        JTextField emailField = createStyledTextField(30);
        emailField.setText(currentEmail);
        JTextField phoneNumberField = createStyledTextField(30);
        phoneNumberField.setText(currentPhoneNumber);

        // Thêm các field vào form
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createStyledLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(userIdLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createStyledLabel("Mã SV:"), gbc);
        gbc.gridx = 1;
        formPanel.add(studentCodeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createStyledLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(createStyledLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneNumberField, gbc);

        // Panel chứa form button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(248, 249, 250));
        JButton saveButton = createStyledButton("Lưu", new Color(46, 204, 113));
        JButton cancelButton = createStyledButton("Hủy", new Color(149, 165, 166));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Thêm Dialog vào panel
        editStudentDialog.add(formPanel, BorderLayout.CENTER);
        editStudentDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Xử lý sự kiện hủy
        cancelButton.addActionListener(e -> editStudentDialog.dispose());

        // Xử lý sự kiện lưu
        saveButton.addActionListener(e -> {
            System.out.println("Save edit student clicked");
            try {
                // Lấy dữ liệu mới từ form
                String newFullName = fullNameField.getText().trim();
                String newEmail = emailField.getText().trim();
                String newPhoneNumber = phoneNumberField.getText().trim();

                // Validation
                if (newFullName.isEmpty() || newEmail.isEmpty() || newPhoneNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(editStudentDialog, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kiểm tra email format
                if (!newEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    JOptionPane.showMessageDialog(editStudentDialog, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kiểm tra phone number format
                if (!newPhoneNumber.matches("^[0-9]{10,11}$")) {
                    JOptionPane.showMessageDialog(editStudentDialog, "Số điện thoại phải có 10-11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Cập nhật thông tin học sinh
                User updatedUser = new User();
                updatedUser.setUserId(userId);
                updatedUser.setFullName(newFullName);
                updatedUser.setEmail(newEmail);
                updatedUser.setPhoneNumber(newPhoneNumber);

                // Gọi phương thức update trong Controller
                studentController.updateStudent(updatedUser);

                JOptionPane.showMessageDialog(editStudentDialog, "Cập nhật thông tin học sinh thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

                // Đóng dialog và refresh dữ liệu
                editStudentDialog.dispose();
                // Refresh toàn bộ dữ liệu từ database
                loadStudentData();

            } catch (Exception exception) {
                JOptionPane.showMessageDialog(editStudentDialog, "Lỗi khi cập nhật thông tin: " + exception.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                exception.printStackTrace();
            }
        });

        editStudentDialog.setVisible(true);
    }

    private void handleDeleteStudent() {
        System.out.println("Selected view row: " + studentTable.getSelectedRow());
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học sinh cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy thông tin học sinh
        int userId = (int) studentTable.getValueAt(selectedRow, 0);
        String studentCode = (String) studentTable.getValueAt(selectedRow, 1);
        String fullName = (String) studentTable.getValueAt(selectedRow, 2);
        String email = (String) studentTable.getValueAt(selectedRow, 3);
        String phoneNumber = (String) studentTable.getValueAt(selectedRow, 4);
        String gender = studentTable.getValueAt(selectedRow, 5).toString();
        String className = (String) studentTable.getValueAt(selectedRow, 6);

        // Tạo thông báo chi tiết
        String message = String.format(
            "Bạn có chắc chắn muốn xóa học sinh này?\n\n" +
            "Thông tin học sinh:\n" +
            "• ID: %d\n" +
            "• Mã SV: %s\n" +
            "• Họ tên: %s\n" +
            "• Email: %s\n" +
            "• SĐT: %s\n" +
            "• Giới tính: %s\n" +
            "• Lớp: %s\n\n" +
            "Hành động này không thể hoàn tác!",
            userId, studentCode, fullName, email, phoneNumber, gender, className
        );

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            message, 
            "Xác nhận xóa học sinh", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                studentController.deleteStudent(userId);
                JOptionPane.showMessageDialog(this, "Xóa học sinh thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadStudentData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa học sinh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void loadStudentData() {
        tableModel.setRowCount(0);

        try {
            allStudents = studentController.getAllStudents();
            displayStudents(allStudents);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu sinh viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayStudents(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            Object[] rowData = {
                    student.getUser().getUserId(),
                    student.getStudentCode(),
                    student.getUser().getFullName(),
                    student.getUser().getEmail(),
                    student.getUser().getPhoneNumber(),
                    student.getUser().getGender(),
                    student.getClassroom() != null ? student.getClassroom().getClassName() : "Chưa có lớp",
                    student.getStudentGrade() != null ? String.format("%.2f", student.getStudentGrade().getAverageGrade()) : "Chưa có điểm"
            };
            tableModel.addRow(rowData);
        }
    }

    private void performSearch() {
        if (allStudents == null) return;
        
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            displayStudents(allStudents);
            return;
        }

        List<Student> filteredStudents = allStudents.stream()
                .filter(student -> 
                    student.getUser().getFullName().toLowerCase().contains(searchText) ||
                    student.getStudentCode().toLowerCase().contains(searchText) ||
                    student.getUser().getEmail().toLowerCase().contains(searchText) ||
                    student.getUser().getPhoneNumber().toLowerCase().contains(searchText) ||
                    (student.getClassroom() != null && student.getClassroom().getClassName().toLowerCase().contains(searchText))
                )
                .collect(java.util.stream.Collectors.toList());

        displayStudents(filteredStudents);
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

    private <T> JComboBox<T> createStyledComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return comboBox;
    }
}
