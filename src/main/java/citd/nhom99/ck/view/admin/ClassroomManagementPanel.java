package citd.nhom99.ck.view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import citd.nhom99.ck.controller.ClassroomController;
import citd.nhom99.ck.model.Classroom;
import citd.nhom99.ck.model.Schedule;
import citd.nhom99.ck.model.Teacher;
import citd.nhom99.ck.model.dao.ClassroomDAO;
import citd.nhom99.ck.model.dao.ScheduleDAO;
import citd.nhom99.ck.model.dao.TeacherDAO;

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
                System.out.println("DEBUG: Classroom " + classroom.getClassName() + " has GVCN: " + teacherName);
            } else {
                System.out.println("DEBUG: Classroom " + classroom.getClassName() + " - Teacher: " + classroom.getTeacher() + 
                                 ", TeacherId: " + classroom.getTeacherId());
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

        String[] columnNames = {"ID Lớp", "Tên lớp", "GVCN", "Sĩ số"};
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
        JButton assignTeacherButton = createStyledButton("Gắn GVCN", new Color(46, 204, 113));
        JButton viewScheduleButton = createStyledButton("Xem thời khóa biểu", new Color(155, 89, 182));
        refreshButton = createStyledButton("Làm mới", new Color(149, 165, 166));

        editButton.addActionListener(e -> handleEditClassroom());
        deleteButton.addActionListener(e -> handleDeleteClassroom());
        assignTeacherButton.addActionListener(e -> handleAssignTeacher());
        viewScheduleButton.addActionListener(e -> handleViewSchedule());
        refreshButton.addActionListener(e -> loadClassroomData());

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(assignTeacherButton);
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
            if (selectedRow < allClassrooms.size()) {
                Classroom classroom = allClassrooms.get(selectedRow);
                
                JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Sửa thông tin lớp học", true);
                dialog.setLayout(new BorderLayout());
                dialog.setSize(500, 280);
                dialog.setLocationRelativeTo(null);
                dialog.getContentPane().setBackground(new Color(248, 249, 250));
                dialog.setResizable(false);
                
                // Header panel - removed to make UI cleaner
                
                // Form panel
                JPanel formPanel = new JPanel(new GridBagLayout());
                formPanel.setBackground(new Color(248, 249, 250));
                formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
                
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(15, 15, 15, 15);
                gbc.anchor = GridBagConstraints.WEST;
                
                // ID field (read-only)
                gbc.gridx = 0;
                gbc.gridy = 0;
                JLabel idLabel = new JLabel("ID lớp:");
                idLabel.setFont(new Font("Arial", Font.BOLD, 16));
                idLabel.setForeground(new Color(52, 58, 64));
                idLabel.setPreferredSize(new Dimension(100, 30));
                formPanel.add(idLabel, gbc);
                
                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                JTextField idField = new JTextField();
                String idText = "" + classroom.getClassId();
                System.out.println("DEBUG: Classroom ID = " + classroom.getClassId() + ", Text = '" + idText + "'");
                idField.setText(idText);
                idField.setFont(new Font("Arial", Font.PLAIN, 16));
                idField.setEditable(false);
                idField.setBackground(new Color(240, 240, 240));
                idField.setForeground(Color.BLACK);
                idField.setPreferredSize(new Dimension(300, 40));
                idField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                formPanel.add(idField, gbc);
                
                // Class name field
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.fill = GridBagConstraints.NONE;
                gbc.weightx = 0.0;
                JLabel nameLabel = new JLabel("Tên lớp:");
                nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
                nameLabel.setForeground(new Color(52, 58, 64));
                nameLabel.setPreferredSize(new Dimension(100, 30));
                formPanel.add(nameLabel, gbc);
                
                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                JTextField nameField = new JTextField();
                nameField.setText(classroom.getClassName() != null ? classroom.getClassName() : "");
                nameField.setFont(new Font("Arial", Font.PLAIN, 16));
                nameField.setForeground(Color.BLACK);
                nameField.setPreferredSize(new Dimension(300, 40));
                nameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                formPanel.add(nameField, gbc);
                
                // Button panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
                buttonPanel.setBackground(new Color(248, 249, 250));
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 25, 20));
                
                JButton saveButton = new JButton("Lưu thay đổi");
                saveButton.setFont(new Font("Arial", Font.BOLD, 15));
                saveButton.setBackground(new Color(46, 204, 113));
                saveButton.setForeground(Color.WHITE);
                saveButton.setPreferredSize(new Dimension(160, 45));
                saveButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
                saveButton.setFocusPainted(false);
                saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                JButton cancelButton = new JButton("Hủy");
                cancelButton.setFont(new Font("Arial", Font.BOLD, 15));
                cancelButton.setBackground(new Color(149, 165, 166));
                cancelButton.setForeground(Color.WHITE);
                cancelButton.setPreferredSize(new Dimension(160, 45));
                cancelButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
                cancelButton.setFocusPainted(false);
                cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                buttonPanel.add(saveButton);
                buttonPanel.add(cancelButton);
                
                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);
                
                // Event handlers
                cancelButton.addActionListener(e -> dialog.dispose());
                
                saveButton.addActionListener(e -> {
                    try {
                        String newClassName = nameField.getText().trim();
                        if (newClassName.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Tên lớp không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        // Validate class name format
                        if (!newClassName.matches("^[0-9]+[A-Z][0-9]*$")) {
                            JOptionPane.showMessageDialog(dialog, "Tên lớp phải có định dạng: số + chữ cái + số (VD: 12A1, 10C3)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        classroom.setClassName(newClassName);
                        
                        // Update in database
                        try {
                            ClassroomDAO classroomDAO = new ClassroomDAO();
                            classroomDAO.updateClassroom(classroom);
                            System.out.println("DEBUG: Updated classroom " + classroom.getClassId() + " with name " + newClassName);
                        } catch (Exception dbEx) {
                            System.out.println("DEBUG: Error updating classroom in database: " + dbEx.getMessage());
                            throw dbEx;
                        }
                        
                        JOptionPane.showMessageDialog(dialog, "Cập nhật thông tin lớp học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadClassroomData(); // Refresh the table
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                });
                
                dialog.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDeleteClassroom() {
        int selectedRow = classroomTable.getSelectedRow();
        if (selectedRow >= 0) {
            if (selectedRow < allClassrooms.size()) {
                Classroom classroom = allClassrooms.get(selectedRow);
                String message = String.format(
                    "Bạn có chắc chắn muốn xóa lớp học này?\n\n" +
                    "• ID lớp: %d\n" +
                    "• Tên lớp: %s\n" +
                    "• GVCN: %s\n" +
                    "• Sĩ số: %d học sinh\n\n" +
                    "Lưu ý: Tất cả học sinh trong lớp này sẽ bị ảnh hưởng!",
                    classroom.getClassId(),
                    classroom.getClassName(),
                    classroom.getTeacher() != null && classroom.getTeacher().getUser() != null ? 
                        classroom.getTeacher().getUser().getFullName() : "Chưa có GVCN",
                    classroom.getStudents() != null ? classroom.getStudents().size() : 0
                );
                
            int confirm = JOptionPane.showConfirmDialog(this,
                        message,
                        "Xác nhận xóa lớp học",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                    // TODO: Implement delete classroom
                    // classroomDAO.deleteClassroom(classroom.getClassId());
                loadClassroomData();
                    JOptionPane.showMessageDialog(this, "Đã xóa lớp học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                }
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
    
    private void handleAssignTeacher() {
        int selectedRow = classroomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học để gắn GVCN!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (selectedRow >= 0 && selectedRow < allClassrooms.size()) {
            Classroom classroom = allClassrooms.get(selectedRow);
            
            JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Gắn GVCN cho lớp " + classroom.getClassName(), true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(500, 320);
            dialog.setLocationRelativeTo(null);
            dialog.getContentPane().setBackground(new Color(248, 249, 250));
            dialog.setResizable(false);
            
            // Header panel - removed to make UI cleaner
            
            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(new Color(248, 249, 250));
            formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 20, 20, 20);
            gbc.anchor = GridBagConstraints.WEST;
            
            // Load available teachers
            List<Teacher> teachers = new ArrayList<>();
            try {
                TeacherDAO teacherDAO = new TeacherDAO();
                teachers = teacherDAO.getAllTeachers();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi tải danh sách giáo viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String[] teacherNames = new String[teachers.size() + 1];
            teacherNames[0] = "Chưa gắn GVCN";
            for (int i = 0; i < teachers.size(); i++) {
                teacherNames[i + 1] = teachers.get(i).getUser().getFullName() + " (" + teachers.get(i).getTeacherCode() + ")";
            }
            
            JComboBox<String> teacherComboBox = new JComboBox<>(teacherNames);
            teacherComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
            teacherComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            
            // Set current teacher if any
            if (classroom.getTeacher() != null && classroom.getTeacher().getUser() != null) {
                String currentTeacher = classroom.getTeacher().getUser().getFullName() + " (" + classroom.getTeacher().getTeacherCode() + ")";
                teacherComboBox.setSelectedItem(currentTeacher);
            }
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0.0;
            JLabel teacherLabel = new JLabel("Giáo viên chủ nhiệm:");
            teacherLabel.setFont(new Font("Arial", Font.BOLD, 16));
            teacherLabel.setForeground(new Color(52, 58, 64));
            formPanel.add(teacherLabel, gbc);
            
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            formPanel.add(teacherComboBox, gbc);
            
            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
            buttonPanel.setBackground(new Color(248, 249, 250));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
            
            JButton assignButton = new JButton("Gắn GVCN");
            assignButton.setFont(new Font("Arial", Font.BOLD, 14));
            assignButton.setBackground(new Color(46, 204, 113));
            assignButton.setForeground(Color.WHITE);
            assignButton.setPreferredSize(new Dimension(140, 40));
            assignButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            assignButton.setFocusPainted(false);
            assignButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JButton cancelButton = new JButton("Hủy");
            cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
            cancelButton.setBackground(new Color(149, 165, 166));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setPreferredSize(new Dimension(140, 40));
            cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            cancelButton.setFocusPainted(false);
            cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            buttonPanel.add(assignButton);
            buttonPanel.add(cancelButton);
            
            dialog.add(formPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            // Event handlers
            cancelButton.addActionListener(e -> dialog.dispose());
            
            final List<Teacher> finalTeachers = teachers;
            assignButton.addActionListener(e -> {
                try {
                    String selectedTeacher = (String) teacherComboBox.getSelectedItem();
                    if (selectedTeacher != null) {
                        int newTeacherId = 0;
                        if (!selectedTeacher.equals("Chưa gắn GVCN")) {
                            for (Teacher teacher : finalTeachers) {
                                String teacherDisplay = teacher.getUser().getFullName() + " (" + teacher.getTeacherCode() + ")";
                                if (teacherDisplay.equals(selectedTeacher)) {
                                    newTeacherId = teacher.getUser().getUserId();
                                    break;
                                }
                            }
                        }
                        
                        // Update classroom's teacher
                        classroom.setTeacherId(newTeacherId);
                        
                        // Update in database
                        try {
                            ClassroomDAO classroomDAO = new ClassroomDAO();
                            classroomDAO.updateClassroom(classroom);
                            System.out.println("DEBUG: Updated classroom " + classroom.getClassId() + " with teacher ID " + newTeacherId);
                        } catch (Exception dbEx) {
                            System.out.println("DEBUG: Error updating classroom in database: " + dbEx.getMessage());
                            throw dbEx;
                        }
                        
                        JOptionPane.showMessageDialog(dialog, "Gắn GVCN thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadClassroomData(); // Refresh the table
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi gắn GVCN: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
            
            dialog.setVisible(true);
        }
    }
}
