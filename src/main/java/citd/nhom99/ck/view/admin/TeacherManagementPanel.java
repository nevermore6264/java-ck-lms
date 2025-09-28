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

import citd.nhom99.ck.controller.TeacherController;
import citd.nhom99.ck.model.Teacher;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.constant.Gender;
import citd.nhom99.ck.model.constant.Role;

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
        JButton refreshButton = createStyledButton("Làm mới", new Color(149, 165, 166));

        // Thêm action listeners
        addButton.addActionListener(e -> handleAddTeacher());
        editButton.addActionListener(e -> handleEditTeacher());
        deleteButton.addActionListener(e -> handleDeleteTeacher());
        viewDetailsButton.addActionListener(e -> handleViewTeacherDetails());
        refreshButton.addActionListener(e -> loadTeacherData());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
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
        tableModel.setRowCount(0);

        try {
            allTeachers = teacherController.getAllTeachers();
            displayTeachers(allTeachers);
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

    private void handleClassClick(int row) {
        if (row >= 0 && row < allTeachers.size()) {
            Teacher teacher = allTeachers.get(row);
            if (teacher.getClassroomId() != 0) {
                String message = String.format(
                    "Thông tin lớp chủ nhiệm:\n\n" +
                    "• Giáo viên: %s\n" +
                    "• Mã GV: %s\n" +
                    "• Lớp chủ nhiệm: %s\n" +
                    "• ID lớp: %d",
                    teacher.getUser().getFullName(),
                    teacher.getTeacherCode(),
                    "Lớp " + teacher.getClassroomId(), // Có thể load tên lớp từ database
                    teacher.getClassroomId()
                );
                
                JOptionPane.showMessageDialog(
                    this, 
                    message, 
                    "Thông tin lớp chủ nhiệm", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(this, "Giáo viên chưa được phân lớp chủ nhiệm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void handleSubjectClick(int row) {
        if (row >= 0 && row < allTeachers.size()) {
            Teacher teacher = allTeachers.get(row);
            if (teacher.getSubjectId() != 0) {
                String message = String.format(
                    "Thông tin môn dạy:\n\n" +
                    "• Giáo viên: %s\n" +
                    "• Mã GV: %s\n" +
                    "• Môn dạy: %s\n" +
                    "• ID môn: %d",
                    teacher.getUser().getFullName(),
                    teacher.getTeacherCode(),
                    "Môn " + teacher.getSubjectId(), // Có thể load tên môn từ database
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
            
            String message = String.format(
                "Thông tin chi tiết giáo viên:\n\n" +
                "• ID: %d\n" +
                "• Mã GV: %s\n" +
                "• Họ tên: %s\n" +
                "• Email: %s\n" +
                "• SĐT: %s\n" +
                "• Giới tính: %s\n" +
                "• Lớp chủ nhiệm: %s\n" +
                "• Môn dạy: %s",
                teacher.getUser().getUserId(),
                teacher.getTeacherCode(),
                teacher.getUser().getFullName(),
                teacher.getUser().getEmail(),
                teacher.getUser().getPhoneNumber(),
                teacher.getUser().getGender(),
                teacher.getClassroomId() != 0 ? "Lớp " + teacher.getClassroomId() : "Chưa phân lớp",
                teacher.getSubjectId() != 0 ? "Môn " + teacher.getSubjectId() : "Chưa phân môn"
            );

            JOptionPane.showMessageDialog(
                this, 
                message, 
                "Chi tiết giáo viên", 
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
