package citd.nhom99.ck.view.teacher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import citd.nhom99.ck.controller.ClassroomController;
import citd.nhom99.ck.model.Classroom;
import citd.nhom99.ck.model.Student;
import citd.nhom99.ck.model.dao.StudentDAO;

public class MyClassroomPanel extends JPanel {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private final ClassroomController classroomController = new ClassroomController();
    private final StudentDAO studentDAO = new StudentDAO();
    private JTextField searchField;
    private List<Student> allStudents;
    private Classroom currentClassroom;

    public MyClassroomPanel() {
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
        loadClassroomData();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(248, 249, 250));
        
        JLabel titleLabel = new JLabel("Lớp học của tôi");
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
                "ID", "Mã SV", "Họ và tên", "Email", "Số điện thoại", "Giới tính", "Điểm TB"
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

        JButton viewDetailsButton = createStyledButton("Xem chi tiết", new Color(52, 152, 219));
        JButton exportButton = createStyledButton("Xuất danh sách", new Color(46, 204, 113));
        JButton refreshButton = createStyledButton("Làm mới", new Color(149, 165, 166));

        // Thêm action listeners
        viewDetailsButton.addActionListener(e -> handleViewDetails());
        exportButton.addActionListener(e -> handleExport());
        refreshButton.addActionListener(e -> loadClassroomData());

        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(exportButton);
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

    private void loadClassroomData() {
        tableModel.setRowCount(0);

        try {
            // TODO: Lấy classroom của teacher hiện tại
            // Hiện tại sẽ load tất cả students để demo
            allStudents = studentDAO.getAllStudents();
            displayStudents(allStudents);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu lớp học: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayStudents(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            if (student.getUser() != null) {
                Object[] rowData = {
                        student.getUser().getUserId(),
                        student.getStudentCode(),
                        student.getUser().getFullName(),
                        student.getUser().getEmail(),
                        student.getUser().getPhoneNumber(),
                        student.getUser().getGender(),
                        student.getStudentGrade() != null ? String.format("%.2f", student.getStudentGrade().getAverageGrade()) : "Chưa có điểm"
                };
                tableModel.addRow(rowData);
            }
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
                    student.getUser() != null && (
                        student.getUser().getFullName().toLowerCase().contains(searchText) ||
                        student.getStudentCode().toLowerCase().contains(searchText) ||
                        student.getUser().getEmail().toLowerCase().contains(searchText) ||
                        student.getUser().getPhoneNumber().toLowerCase().contains(searchText)
                    )
                )
                .collect(java.util.stream.Collectors.toList());

        displayStudents(filteredStudents);
    }

    private void handleViewDetails() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học sinh để xem chi tiết!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy thông tin học sinh
        int userId = (int) studentTable.getValueAt(selectedRow, 0);
        String studentCode = (String) studentTable.getValueAt(selectedRow, 1);
        String fullName = (String) studentTable.getValueAt(selectedRow, 2);
        String email = (String) studentTable.getValueAt(selectedRow, 3);
        String phoneNumber = (String) studentTable.getValueAt(selectedRow, 4);
        String gender = studentTable.getValueAt(selectedRow, 5).toString();
        String averageGrade = (String) studentTable.getValueAt(selectedRow, 6);

        // Tạo thông báo chi tiết
        String message = String.format(
            "Thông tin chi tiết học sinh:\n\n" +
            "• ID: %d\n" +
            "• Mã SV: %s\n" +
            "• Họ tên: %s\n" +
            "• Email: %s\n" +
            "• SĐT: %s\n" +
            "• Giới tính: %s\n" +
            "• Điểm TB: %s",
            userId, studentCode, fullName, email, phoneNumber, gender, averageGrade
        );

        JOptionPane.showMessageDialog(
            this, 
            message, 
            "Chi tiết học sinh", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void handleExport() {
        JOptionPane.showMessageDialog(this, "Chức năng xuất danh sách sẽ được phát triển trong tương lai!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
