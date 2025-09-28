package citd.nhom99.ck.view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
import citd.nhom99.ck.model.Schedule;
import citd.nhom99.ck.model.Teacher;
import citd.nhom99.ck.model.dao.ScheduleDAO;

public class ClassroomManagementPanel extends JPanel {
    private JTable classroomTable;
    private DefaultTableModel tableModel;
    private final ClassroomController classroomController = new ClassroomController();
    private JButton editButton, deleteButton, refreshButton;
    private JTextField searchField;
    private List<Classroom> allClassrooms;

    public ClassroomManagementPanel() {
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
        
        JLabel titleLabel = new JLabel("Quản lý Lớp học");
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

    private void loadClassroomData() {
        tableModel.setRowCount(0);
        try {
            allClassrooms = classroomController.getAllClassrooms();
            displayClassrooms(allClassrooms);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu lớp học: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayClassrooms(List<Classroom> classrooms) {
        tableModel.setRowCount(0);
        for (Classroom classroom : classrooms) {
            // Get teacher name
            String teacherName = "Chưa có GVCN";
            if (classroom.getTeacher() != null && classroom.getTeacher().getUser() != null) {
                teacherName = classroom.getTeacher().getUser().getFullName();
            }
            
            // Get student count
            int studentCount = classroom.getStudents() != null ? classroom.getStudents().size() : 0;
            
            Object[] rowData = {
                    classroom.getClassId(),
                    classroom.getClassName(),
                    teacherName,
                    studentCount
            };
            tableModel.addRow(rowData);
        }
    }

    private void performSearch() {
        if (allClassrooms == null) return;
        
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            displayClassrooms(allClassrooms);
            return;
        }

        List<Classroom> filteredClassrooms = allClassrooms.stream()
                .filter(classroom -> 
                    classroom.getClassName().toLowerCase().contains(searchText) ||
                    String.valueOf(classroom.getClassId()).contains(searchText) ||
                    String.valueOf(classroom.getTeacherId()).contains(searchText)
                )
                .collect(java.util.stream.Collectors.toList());

        displayClassrooms(filteredClassrooms);
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(248, 249, 250));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        String[] columnNames = {"ID Lớp", "Tên lớp", "GVCN", "Sĩ số", "Thời khóa biểu của lớp"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        classroomTable = new JTable(tableModel);
        classroomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        classroomTable.setRowHeight(35);
        classroomTable.setFont(new Font("Arial", Font.PLAIN, 13));
        classroomTable.setGridColor(new Color(220, 220, 220));
        classroomTable.setShowGrid(true);
        classroomTable.setIntercellSpacing(new Dimension(0, 1));
        
        // Customize table header
        JTableHeader header = classroomTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(52, 58, 64));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        JScrollPane scrollPane = new JScrollPane(classroomTable);
        scrollPane.setPreferredSize(new Dimension(1000, 500));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(248, 249, 250));

        editButton = createStyledButton("Sửa thông tin", new Color(52, 152, 219));
        deleteButton = createStyledButton("Xóa lớp học", new Color(231, 76, 60));
        JButton viewScheduleButton = createStyledButton("Xem thời khóa biểu", new Color(155, 89, 182));
        refreshButton = createStyledButton("Làm mới", new Color(149, 165, 166));

        editButton.addActionListener(e -> handleEditClassroom());
        deleteButton.addActionListener(e -> handleDeleteClassroom());
        viewScheduleButton.addActionListener(e -> handleViewSchedule());
        refreshButton.addActionListener(e -> loadClassroomData());

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewScheduleButton);
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

    private void handleAddClassroom() {
        JTextField classNameField = new JTextField();
        JComboBox<Teacher> classTeacherField = new JComboBox<>();
        classTeacherField.setEditable(true);
        final JComponent[] inputs = new JComponent[]{
                new JLabel("Tên lớp"),
                classNameField,
                new JLabel("Giáo viên chủ nhiệm"),
                classTeacherField
        };
        int result = JOptionPane.showConfirmDialog(this, inputs, "Thêm lớp học mới", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String classId = classNameField.getText();
            String className = (String) classTeacherField.getSelectedItem();
            if (classId != null && !classId.trim().isEmpty() && className != null && !className.trim().isEmpty()) {
                Classroom newClassroom = new Classroom(className);
                classroomController.createClassroom(newClassroom);
                loadClassroomData();
            } else {
                JOptionPane.showMessageDialog(this, "ID và tên lớp không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEditClassroom() {
        int selectedRow = classroomTable.getSelectedRow();
        if (selectedRow >= 0) {
            int classId = (int) tableModel.getValueAt(selectedRow, 0);
            Classroom classroom = classroomController.getClassroomById(classId);

            if (classroom != null) {
                String newClassName = JOptionPane.showInputDialog(this, "Nhập tên lớp mới:", classroom.getClassName());
                if (newClassName != null && !newClassName.trim().isEmpty()) {
                    classroom.setClassName(newClassName);
//                    classroomDAO.updateClassroom(classroom);
                    loadClassroomData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin lớp học.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDeleteClassroom() {
        int selectedRow = classroomTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa lớp học này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String classId = (String) tableModel.getValueAt(selectedRow, 0);
//                classroomDAO.deleteClassroom(classId);
                loadClassroomData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void handleViewSchedule() {
        int selectedRow = classroomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học để xem thời khóa biểu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (selectedRow >= 0 && selectedRow < allClassrooms.size()) {
            Classroom classroom = allClassrooms.get(selectedRow);
            
            try {
                // Load schedule for this classroom (current semester and academic year)
                ScheduleDAO scheduleDAO = new ScheduleDAO();
                List<Schedule> schedules = scheduleDAO.getScheduleByClassroomId(classroom.getClassId(), 1, 10); // Semester 1, Grade 10
                
                if (schedules != null && !schedules.isEmpty()) {
                    // Build schedule display
                    StringBuilder scheduleText = new StringBuilder();
                    scheduleText.append("Thời khóa biểu lớp ").append(classroom.getClassName()).append(":\n\n");
                    
                    // Group by day
                    String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
                    String[] dayNames = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
                    
                    for (int i = 0; i < days.length; i++) {
                        String day = days[i];
                        List<Schedule> daySchedules = schedules.stream()
                            .filter(s -> day.equals(s.getDayOfWeek()))
                            .sorted((s1, s2) -> Integer.compare(s1.getPeriod(), s2.getPeriod()))
                            .collect(java.util.stream.Collectors.toList());
                        
                        if (!daySchedules.isEmpty()) {
                            scheduleText.append(dayNames[i]).append(":\n");
                            for (Schedule schedule : daySchedules) {
                                scheduleText.append(String.format("  Tiết %d: %s - GV: %s\n", 
                                    schedule.getPeriod(),
                                    schedule.getSubject() != null ? schedule.getSubject().getSubjectName() : "Môn " + schedule.getSubjectId(),
                                    schedule.getTeacher() != null ? schedule.getTeacher().getUser().getFullName() : "GV " + schedule.getTeacherId()));
                            }
                            scheduleText.append("\n");
                        }
                    }
                    
                    JOptionPane.showMessageDialog(
                        this, 
                        scheduleText.toString(), 
                        "Thời khóa biểu lớp " + classroom.getClassName(), 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(this, "Lớp " + classroom.getClassName() + " chưa có thời khóa biểu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải thời khóa biểu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
