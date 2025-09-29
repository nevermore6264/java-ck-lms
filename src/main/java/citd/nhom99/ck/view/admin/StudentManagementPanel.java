package citd.nhom99.ck.view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import citd.nhom99.ck.controller.StudentController;
import citd.nhom99.ck.model.Classroom;
import citd.nhom99.ck.model.Student;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.constant.Gender;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.model.dao.ClassroomDAO;
import citd.nhom99.ck.model.dao.UserDAO;
import citd.nhom99.ck.utils.CustomDialog;

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
        
        // Set custom renderer for "Lớp" and "Điểm" columns to make them look like links
        studentTable.getColumn("Lớp").setCellRenderer(new LinkCellRenderer());
        studentTable.getColumn("Điểm").setCellRenderer(new LinkCellRenderer());
        
        // Add mouse listener for table clicks
        studentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = studentTable.rowAtPoint(evt.getPoint());
                int col = studentTable.columnAtPoint(evt.getPoint());
                
                if (row >= 0 && col >= 0) {
                    String columnName = studentTable.getColumnName(col);
                    if ("Lớp".equals(columnName)) {
                        handleClassClick(row);
                    } else if ("Điểm".equals(columnName)) {
                        handleGradeClick(row);
                    }
                }
            }
        });
        
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

    // Custom cell renderer to make text look like links
    private class LinkCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null && !value.toString().equals("Chưa có lớp") && !value.toString().equals("Chưa có điểm")) {
                // Make it look like a link
                c.setForeground(new Color(0, 102, 204)); // Blue color
                c.setFont(c.getFont().deriveFont(Font.BOLD)); // Bold
                // Note: Java Swing doesn't support underline directly, but we can use HTML
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setText("<html><u>" + value.toString() + "</u></html>");
                }
            } else {
                // Regular text for "Chưa có lớp" or "Chưa có điểm"
                c.setForeground(Color.BLACK);
                c.setFont(c.getFont().deriveFont(Font.PLAIN));
            }
            
            return c;
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(248, 249, 250));

        JButton addButton = createStyledButton("Thêm học sinh", new Color(46, 204, 113));
        JButton editButton = createStyledButton("Sửa thông tin", new Color(52, 152, 219));
        JButton deleteButton = createStyledButton("Xóa học sinh", new Color(231, 76, 60));
        JButton assignClassButton = createStyledButton("Phân lớp", new Color(230, 126, 34));
        JButton viewDetailsButton = createStyledButton("Xem chi tiết", new Color(155, 89, 182));
        JButton refreshButton = createStyledButton("Làm mới", new Color(149, 165, 166));

        // Thêm action listeners
        addButton.addActionListener(e -> handleCreateStudent());
        editButton.addActionListener(e -> handleEditStudent());
        deleteButton.addActionListener(e -> handleDeleteStudent());
        assignClassButton.addActionListener(e -> handleAssignClass());
        viewDetailsButton.addActionListener(e -> handleViewStudentDetails());
        refreshButton.addActionListener(e -> loadStudentData());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(assignClassButton);
        buttonPanel.add(viewDetailsButton);
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
        JComboBox<String> genderField = createGenderComboBox();

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
                Gender gender = getGenderFromVietnamese((String) genderField.getSelectedItem());

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
            CustomDialog.showWarningDialog(this, "Vui lòng chọn học sinh cần chỉnh sửa!", "Cảnh báo");
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

                // Lấy thông tin user hiện tại từ database
                User currentUser = userDAO.getUserById(userId);
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(editStudentDialog, "Không tìm thấy thông tin học sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Cập nhật thông tin học sinh
                User updatedUser = new User();
                updatedUser.setUserId(userId);
                updatedUser.setUsername(currentUser.getUsername()); // Giữ nguyên username
                updatedUser.setPassword(currentUser.getPassword()); // Giữ nguyên password
                updatedUser.setFullName(newFullName);
                updatedUser.setEmail(newEmail);
                updatedUser.setPhoneNumber(newPhoneNumber);
                updatedUser.setGender(currentUser.getGender()); // Giữ nguyên gender
                updatedUser.setRole(currentUser.getRole()); // Giữ nguyên role

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
            CustomDialog.showWarningDialog(this, "Vui lòng chọn học sinh cần xóa!", "Cảnh báo");
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

        boolean confirm = CustomDialog.showConfirmDialog(this, message, "Xác nhận xóa học sinh");
        
        if (confirm) {
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
                        getGenderInVietnamese(student.getUser().getGender()),
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

    private void handleClassClick(int row) {
        if (row >= 0 && row < allStudents.size()) {
            Student student = allStudents.get(row);
            if (student.getClassroom() != null) {
                try {
                    // Load đầy đủ thông tin lớp học từ database
                    ClassroomDAO classroomDAO = new ClassroomDAO();
                    Classroom classroom = classroomDAO.getClassroomById(student.getClassroom().getClassId());
                    
                    if (classroom != null) {
                        // Lấy thông tin giáo viên chủ nhiệm
                        String teacherName = "Chưa có GVCN";
                        if (classroom.getTeacher() != null && classroom.getTeacher().getUser() != null) {
                            teacherName = classroom.getTeacher().getUser().getFullName();
                        }
                        
                        // Lấy sĩ số lớp
                        int studentCount = classroom.getStudents() != null ? classroom.getStudents().size() : 0;
                        
                        // Create custom dialog for class information
                        JDialog classInfoDialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Thông tin lớp học", true);
                        classInfoDialog.setSize(500, 400);
                        classInfoDialog.setLocationRelativeTo(this);
                        classInfoDialog.setResizable(false);
                        
                        // Main panel
                        JPanel mainPanel = new JPanel(new BorderLayout());
                        mainPanel.setBackground(Color.WHITE);
                        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
                        
                        // Header panel
                        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                        headerPanel.setBackground(Color.WHITE);
                        
                        JLabel titleLabel = new JLabel("Thông tin lớp học");
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
                            {"Tên lớp:", classroom.getClassName()},
                            {"ID lớp:", String.valueOf(classroom.getClassId())},
                            {"Sĩ số:", String.valueOf(studentCount) + " học sinh"},
                            {"GVCN:", teacherName}
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
                        
                        closeButton.addActionListener(e -> classInfoDialog.dispose());
                        
                        buttonPanel.add(closeButton);
                        
                        mainPanel.add(headerPanel, BorderLayout.NORTH);
                        mainPanel.add(contentPanel, BorderLayout.CENTER);
                        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
                        
                        classInfoDialog.add(mainPanel);
                        classInfoDialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Không thể tải thông tin lớp học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi tải thông tin lớp học: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Học sinh chưa được phân lớp!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void handleGradeClick(int row) {
        if (row >= 0 && row < allStudents.size()) {
            Student student = allStudents.get(row);
            if (student.getStudentGrade() != null) {
                String message = String.format(
                    "Điểm số học sinh:\n\n" +
                    "• Họ tên: %s\n" +
                    "• Mã SV: %s\n" +
                    "• Điểm thường xuyên: %.2f\n" +
                    "• Điểm giữa kỳ: %.2f\n" +
                    "• Điểm cuối kỳ: %.2f\n" +
                    "• Điểm trung bình: %.2f\n" +
                    "• Xếp loại: %s",
                    student.getUser().getFullName(),
                    student.getStudentCode(),
                    student.getStudentGrade().getRegularGrade(),
                    student.getStudentGrade().getMidtermGrade(),
                    student.getStudentGrade().getFinalGrade(),
                    student.getStudentGrade().getAverageGrade(),
                    student.getStudentGrade().getClassified() != null ? 
                        student.getStudentGrade().getClassified().toString() : "Chưa xếp loại"
                );
                
                JOptionPane.showMessageDialog(
                    this, 
                    message, 
                    "Điểm số học sinh", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(this, "Học sinh chưa có điểm số!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void handleViewStudentDetails() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            CustomDialog.showWarningDialog(this, "Vui lòng chọn học sinh để xem chi tiết!", "Cảnh báo");
            return;
        }

        if (selectedRow >= 0 && selectedRow < allStudents.size()) {
            Student student = allStudents.get(selectedRow);
            
            // Create custom dialog for student details
            JDialog detailDialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết học sinh", true);
            detailDialog.setSize(500, 400);
            detailDialog.setLocationRelativeTo(this);
            detailDialog.setResizable(false);
            
            // Main panel
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(Color.WHITE);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            
            // Header panel
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            headerPanel.setBackground(Color.WHITE);
            
            JLabel titleLabel = new JLabel("Thông tin chi tiết học sinh");
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
            
            // Student information
            String[][] infoData = {
                {"ID:", String.valueOf(student.getUser().getUserId())},
                {"Mã SV:", student.getStudentCode()},
                {"Họ tên:", student.getUser().getFullName()},
                {"Email:", student.getUser().getEmail()},
                {"Số điện thoại:", student.getUser().getPhoneNumber()},
                {"Giới tính:", getGenderInVietnamese(student.getUser().getGender())},
                {"Lớp:", student.getClassroom() != null ? student.getClassroom().getClassName() : "Chưa phân lớp"},
                {"Điểm TB:", student.getStudentGrade() != null ? String.format("%.2f", student.getStudentGrade().getAverageGrade()) : "Chưa có điểm"},
                {"Xếp loại:", student.getStudentGrade() != null && student.getStudentGrade().getClassified() != null ? 
                    getClassifiedInVietnamese(student.getStudentGrade().getClassified()) : "Chưa xếp loại"}
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
            
            closeButton.addActionListener(e -> detailDialog.dispose());
            
            buttonPanel.add(closeButton);
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            detailDialog.add(mainPanel);
            detailDialog.setVisible(true);
        }
    }

    private void handleAssignClass() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            CustomDialog.showWarningDialog(this, "Vui lòng chọn học sinh để phân lớp!", "Cảnh báo");
            return;
        }

        if (selectedRow >= 0 && selectedRow < allStudents.size()) {
            Student student = allStudents.get(selectedRow);
            
            // Create assign class dialog
            JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Phân lớp cho học sinh", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());
            
            // Header
            JPanel headerPanel = new JPanel();
            headerPanel.setBackground(new Color(52, 58, 64));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            
            JLabel titleLabel = new JLabel("Phân lớp cho: " + student.getUser().getFullName());
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel);
            
            // Content panel
            JPanel contentPanel = new JPanel(new java.awt.GridBagLayout());
            contentPanel.setBackground(Color.WHITE);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
            gbc.insets = new java.awt.Insets(10, 10, 10, 10);
            gbc.anchor = java.awt.GridBagConstraints.WEST;
            
            // Current class info
            gbc.gridx = 0; gbc.gridy = 0;
            contentPanel.add(createStyledLabel("Lớp hiện tại:"), gbc);
            
            gbc.gridx = 1;
            String currentClass = student.getClassroom() != null ? student.getClassroom().getClassName() : "Chưa phân lớp";
            JLabel currentClassLabel = new JLabel(currentClass);
            currentClassLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            currentClassLabel.setForeground(new Color(100, 100, 100));
            contentPanel.add(currentClassLabel, gbc);
            
            // New class selection
            gbc.gridx = 0; gbc.gridy = 1;
            contentPanel.add(createStyledLabel("Lớp mới:"), gbc);
            
            gbc.gridx = 1; gbc.fill = java.awt.GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            
            // Load available classrooms
            ClassroomDAO classroomDAO = new ClassroomDAO();
            java.util.List<citd.nhom99.ck.model.Classroom> classrooms = classroomDAO.getAllClassrooms();
            String[] classNames = new String[classrooms.size()];
            for (int i = 0; i < classrooms.size(); i++) {
                classNames[i] = classrooms.get(i).getClassName();
            }
            
            JComboBox<String> classComboBox = createStyledComboBox(classNames);
            contentPanel.add(classComboBox, gbc);
            
            // Button panel
            JPanel buttonPanel = new JPanel(new java.awt.FlowLayout());
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            
            JButton assignButton = createStyledButton("Phân lớp", new Color(46, 204, 113));
            JButton cancelButton = createStyledButton("Hủy", new Color(149, 165, 166));
            
            assignButton.addActionListener(e -> {
                try {
                    String selectedClassName = (String) classComboBox.getSelectedItem();
                    if (selectedClassName != null) {
                        // Find classroom by name
                        citd.nhom99.ck.model.Classroom selectedClassroom = null;
                        for (citd.nhom99.ck.model.Classroom classroom : classrooms) {
                            if (classroom.getClassName().equals(selectedClassName)) {
                                selectedClassroom = classroom;
                                break;
                            }
                        }
                        
                        if (selectedClassroom != null) {
                            // Update student's classroom in database
                            studentController.updateStudentClassroom(student.getUser().getUserId(), selectedClassroom.getClassId());
                            
                            // Update student object
                            student.setClassroomId(selectedClassroom.getClassId());
                            student.setClassroom(selectedClassroom);
                            
                            JOptionPane.showMessageDialog(dialog, "Phân lớp thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                            dialog.dispose();
                            loadStudentData(); // Refresh the table
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi phân lớp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(assignButton);
            buttonPanel.add(cancelButton);
            
            dialog.add(headerPanel, BorderLayout.NORTH);
            dialog.add(contentPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        }
    }
    
    // Helper method to convert classified to Vietnamese
    private String getClassifiedInVietnamese(citd.nhom99.ck.model.constant.Classified classified) {
        if (classified == null) {
            return "Chưa xếp loại";
        }
        switch (classified) {
            case XUAT_SAC:
                return "Xuất sắc";
            case GIOI:
                return "Giỏi";
            case KHA:
                return "Khá";
            case TRUNG_BINH:
                return "Trung bình";
            case YEU:
                return "Yếu";
            default:
                return "Chưa xếp loại";
        }
    }
    
    // Helper method to create gender combobox with Vietnamese options
    private JComboBox<String> createGenderComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Nam", "Nữ"});
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return comboBox;
    }
    
    // Helper method to convert Vietnamese gender string to Gender enum
    private Gender getGenderFromVietnamese(String vietnameseGender) {
        return switch (vietnameseGender) {
            case "Nam" -> Gender.MALE;
            case "Nữ" -> Gender.FEMALE;
            default -> Gender.MALE; // Default to MALE if invalid
        };
    }
}
