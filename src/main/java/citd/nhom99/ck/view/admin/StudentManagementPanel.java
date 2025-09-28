package citd.nhom99.ck.view.admin;

import citd.nhom99.ck.controller.StudentController;
import citd.nhom99.ck.model.dao.StudentDAO;
import citd.nhom99.ck.model.dao.UserDAO;
import citd.nhom99.ck.model.constant.Gender;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.model.Student;
import citd.nhom99.ck.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentManagementPanel extends JPanel {
    private final UserDAO userDAO = new UserDAO();
    private final StudentController studentController = new StudentController();
    private JTable studentTable;
    private DefaultTableModel tableModel;


    public StudentManagementPanel() {
        JPanel studentManagementPanel;

        studentManagementPanel = new JPanel(new BorderLayout());
        studentManagementPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tạo header panel
        JPanel headerPanel = createHeaderPanel();
        studentManagementPanel.add(headerPanel, BorderLayout.NORTH);

        // Tạo table panel
        JPanel tablePanel = createTablePanel();
        studentManagementPanel.add(tablePanel, BorderLayout.CENTER);

        // Tạo button panel
        JPanel buttonPanel = createButtonPanel();
        studentManagementPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load dữ liệu ban đầu
        loadStudentData();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(studentManagementPanel);
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
                "ID", "Mã SV", "Họ và tên", "Email", "Số điện thoại", "Giới tính", "Lớp", "Điểm"
        };

        tableModel = new DefaultTableModel(columnNames, 0);

        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton, editButton, deleteButton, refreshButton;

        addButton = new JButton("Thêm học sinh");
        editButton = new JButton("Sửa thông tin");
        deleteButton = new JButton("Xóa học sinh");
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

    private void handleCreateStudent() {
        System.out.println("Add student clicked");
        // Tạo form Dialog để nhập thông tin học sinh
        JDialog addNewStudentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm học sinh mới", true);
        addNewStudentDialog.setLayout(new BorderLayout());
        addNewStudentDialog.setSize(400, 500);
        addNewStudentDialog.setLocationRelativeTo(null);

        // Tạo Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Tạo input field
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField fullNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneNumberField = new JTextField(20);
        JComboBox<Gender> genderField = new JComboBox<>(Gender.values());

        // Thêm label cho input field
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
        JButton cancelButton = new JButton("Hủy");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Thêm Dialog vào panel
        addNewStudentDialog.add(formPanel, BorderLayout.CENTER);
        addNewStudentDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Xử lý sự kiện lưu
        saveButton.addActionListener(e -> {
            System.out.println("Save student clicked");
            try {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String fullName = fullNameField.getText();
                String email = emailField.getText();
                String phoneNumber = phoneNumberField.getText();
                Gender gender = genderField.getItemAt(genderField.getSelectedIndex());

                User newStudent = new User(username, password, fullName, phoneNumber, email, gender, Role.STUDENT);

                studentController.createStudent(newStudent);
                JOptionPane.showMessageDialog(addNewStudentDialog, "Thêm học sinh thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("New student added," + newStudent);

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
        editStudentDialog.setSize(400, 350);
        editStudentDialog.setLocationRelativeTo(null);

        // Tạo Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Hiển thị thông tin không thể chỉnh sửa
        JLabel userIdLabel = new JLabel(userId.toString());
        JLabel studentCodeLabel = new JLabel(studentCode);

        // Tạo input field với dữ liệu hiện tại
        JTextField fullNameField = new JTextField(currentFullName, 20);
        JTextField emailField = new JTextField(currentEmail, 20);
        JTextField phoneNumberField = new JTextField(currentPhoneNumber, 20);

        // Thêm các field vào form
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(userIdLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Mã SV:"), gbc);
        gbc.gridx = 1;
        formPanel.add(studentCodeLabel, gbc);

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

        // Panel chứa form button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");

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

                // Cập nhật thông tin học sinh
                User updatedUser = new User();
                updatedUser.setUserId(userId);
                updatedUser.setFullName(newFullName);
                updatedUser.setEmail(newEmail);
                updatedUser.setPhoneNumber(newPhoneNumber);

                // Gọi phương thức update trong Controller
                studentController.updateStudent(updatedUser);

                // Cập nhật dữ liệu trong table
                studentTable.setValueAt(newFullName, selectedRow, 2);
                studentTable.setValueAt(newEmail, selectedRow, 3);
                studentTable.setValueAt(newPhoneNumber, selectedRow, 4);

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
            JOptionPane.showMessageDialog(this, "Xin vui lòng chọn dòng cần xóa trước.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (int) studentTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc xóa dữ liệu này?" + userId);
        if (confirm == JOptionPane.YES_OPTION) {
            studentController.deleteStudent(userId);
            loadStudentData();
        }
    }

    private void loadStudentData() {
        tableModel.setRowCount(0);

        try {
            List<Student> students = studentController.getAllStudents();
            for (Student student : students) {
                Object[] rowData = {
                        student.getUser().getUserId(),
                        student.getStudentCode(),
                        student.getUser().getFullName(),
                        student.getUser().getEmail(),
                        student.getUser().getPhoneNumber(),
                        student.getUser().getGender(),
                        student.getClassroom() != null ? student.getClassroom().getClassName() : "Chưa có lớp",
                        student.getStudentGrade() != null ? student.getStudentGrade().toString() : "Chưa có điểm"
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu sinh viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
