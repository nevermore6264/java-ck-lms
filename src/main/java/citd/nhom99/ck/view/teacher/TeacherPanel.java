package citd.nhom99.ck.view.teacher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import citd.nhom99.ck.controller.AppController;
import citd.nhom99.ck.model.Schedule;
import citd.nhom99.ck.model.StudentGrade;
import citd.nhom99.ck.model.Teacher;
import citd.nhom99.ck.model.dao.ScheduleDAO;
import citd.nhom99.ck.model.dao.StudentGradeDAO;
import citd.nhom99.ck.utils.SubjectTranslator;

public class TeacherPanel extends JPanel {
    private final AppController controller;
    private final Teacher teacher;
    private final StudentGradeDAO gradeDAO;
    private final ScheduleDAO scheduleDAO;
    
    private JTabbedPane tabbedPane;
    private JTable scheduleTable;
    private JTable gradesTable;
    private JComboBox<String> semesterCombo;
    private JComboBox<String> academicYearCombo;
    private JComboBox<String> gradesSemesterCombo;
    private JComboBox<String> gradesAcademicYearCombo;
    private JComboBox<String> subjectCombo;

    public TeacherPanel(Teacher teacher, AppController controller) {
        this.teacher = teacher;
        this.controller = controller;
        this.gradeDAO = new StudentGradeDAO();
        this.scheduleDAO = new ScheduleDAO();
        
        initializeComponents();
        setupLayout();
        loadScheduleData();
        loadGradesData();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Tạo tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Tab lịch dạy học
        JPanel schedulePanel = createSchedulePanel();
        tabbedPane.addTab("Lịch dạy học", schedulePanel);
        
        // Tab nhập điểm
        JPanel gradesPanel = createGradesPanel();
        tabbedPane.addTab("Nhập điểm", gradesPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel điều khiển
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel semesterLabel = new JLabel("Học kỳ:");
        semesterCombo = new JComboBox<>(new String[]{"1", "2"});
        semesterCombo.setSelectedIndex(0);
        
        JLabel academicYearLabel = new JLabel("Khối:");
        academicYearCombo = new JComboBox<>(new String[]{"10", "11", "12"});
        academicYearCombo.setSelectedIndex(0);
        
        JButton refreshButton = new JButton("Làm mới");
        refreshButton.addActionListener(e -> loadScheduleData());
        
        controlPanel.add(semesterLabel);
        controlPanel.add(semesterCombo);
        controlPanel.add(academicYearLabel);
        controlPanel.add(academicYearCombo);
        controlPanel.add(refreshButton);
        
        // Bảng lịch dạy học
        String[] scheduleColumns = {"Thứ", "Tiết", "Lớp", "Môn học", "Phòng học"};
        DefaultTableModel scheduleModel = new DefaultTableModel(scheduleColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scheduleTable = new JTable(scheduleModel);
        scheduleTable.setRowHeight(30);
        scheduleTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scheduleScrollPane = new JScrollPane(scheduleTable);
        scheduleScrollPane.setPreferredSize(new Dimension(800, 400));
        
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scheduleScrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel điều khiển
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel semesterLabel = new JLabel("Học kỳ:");
        gradesSemesterCombo = new JComboBox<>(new String[]{"1", "2"});
        gradesSemesterCombo.setSelectedIndex(0);
        
        JLabel academicYearLabel = new JLabel("Khối:");
        gradesAcademicYearCombo = new JComboBox<>(new String[]{"10", "11", "12"});
        gradesAcademicYearCombo.setSelectedIndex(0);
        
        JLabel subjectLabel = new JLabel("Môn học:");
        subjectCombo = new JComboBox<>(new String[]{"Toán", "Lý", "Hóa", "Sinh", "Văn", "Sử", "Địa", "Anh"});
        subjectCombo.setSelectedIndex(0);
        
        JButton refreshButton = new JButton("Làm mới");
        refreshButton.addActionListener(e -> loadGradesData());
        
        JButton saveButton = new JButton("Lưu điểm");
        saveButton.addActionListener(e -> saveGrades());
        
        controlPanel.add(semesterLabel);
        controlPanel.add(gradesSemesterCombo);
        controlPanel.add(academicYearLabel);
        controlPanel.add(gradesAcademicYearCombo);
        controlPanel.add(subjectLabel);
        controlPanel.add(subjectCombo);
        controlPanel.add(refreshButton);
        controlPanel.add(saveButton);
        
        // Bảng điểm số
        String[] gradesColumns = {"Mã HS", "Họ tên", "Điểm thường xuyên", "Điểm giữa kỳ", "Điểm cuối kỳ", "Điểm TB", "Xếp loại"};
        DefaultTableModel gradesModel = new DefaultTableModel(gradesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho phép chỉnh sửa cột điểm
                return column >= 2 && column <= 4;
            }
        };
        gradesTable = new JTable(gradesModel);
        gradesTable.setRowHeight(30);
        gradesTable.getTableHeader().setReorderingAllowed(false);
        
        // Thiết lập editor cho các cột điểm
        gradesTable.getColumnModel().getColumn(2).setCellEditor(new SpinnerNumberEditor(0.0, 10.0, 0.1));
        gradesTable.getColumnModel().getColumn(3).setCellEditor(new SpinnerNumberEditor(0.0, 10.0, 0.1));
        gradesTable.getColumnModel().getColumn(4).setCellEditor(new SpinnerNumberEditor(0.0, 10.0, 0.1));
        
        JScrollPane gradesScrollPane = new JScrollPane(gradesTable);
        gradesScrollPane.setPreferredSize(new Dimension(800, 400));
        
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(gradesScrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void setupLayout() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void loadScheduleData() {
        int semester = Integer.parseInt((String) semesterCombo.getSelectedItem());
        int academicYear = Integer.parseInt((String) academicYearCombo.getSelectedItem());
        
        try {
            List<Schedule> schedules = scheduleDAO.getScheduleByTeacherId(
                teacher.getUserId(), semester, academicYear);
            
            DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();
            model.setRowCount(0);
            
            for (Schedule schedule : schedules) {
                Object[] row = {
                    getDayOfWeekVietnamese(schedule.getDayOfWeek()),
                    schedule.getPeriod(),
                    schedule.getClassroom() != null ? schedule.getClassroom().getClassName() : "N/A",
                    schedule.getSubject() != null ? SubjectTranslator.convertToVietnamese(schedule.getSubject().getSubjectName()) : "N/A",
                    "Phòng " + schedule.getClassroomId() // Có thể thay bằng thông tin phòng thực tế
                };
                model.addRow(row);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải lịch dạy học: " + e.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadGradesData() {
        int semester = Integer.parseInt((String) gradesSemesterCombo.getSelectedItem());
        int academicYear = Integer.parseInt((String) gradesAcademicYearCombo.getSelectedItem());
        String subjectName = (String) subjectCombo.getSelectedItem();
        
        // Lấy subjectId từ tên môn học (có thể cải thiện bằng cách lấy từ database)
        int subjectId = getSubjectIdByName(subjectName);
        
        try {
            List<StudentGrade> grades = gradeDAO.getGradesByClassroomId(
                teacher.getClassroomId(), subjectId, semester, academicYear);
            
            DefaultTableModel model = (DefaultTableModel) gradesTable.getModel();
            model.setRowCount(0);
            
            for (StudentGrade grade : grades) {
                Object[] row = {
                    grade.getStudent() != null ? grade.getStudent().getStudentCode() : "N/A",
                    grade.getStudent() != null ? grade.getStudent().getUser().getFullName() : "N/A",
                    grade.getRegularGrade(),
                    grade.getMidtermGrade(),
                    grade.getFinalGrade(),
                    String.format("%.1f", grade.getAverageGrade()),
                    grade.getClassified() != null ? getClassifiedVietnamese(grade.getClassified()) : "Chưa xếp loại"
                };
                model.addRow(row);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải điểm số: " + e.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void saveGrades() {
        DefaultTableModel model = (DefaultTableModel) gradesTable.getModel();
        int semester = Integer.parseInt((String) gradesSemesterCombo.getSelectedItem());
        int academicYear = Integer.parseInt((String) gradesAcademicYearCombo.getSelectedItem());
        String subjectName = (String) subjectCombo.getSelectedItem();
        int subjectId = getSubjectIdByName(subjectName);
        
        int successCount = 0;
        int totalCount = model.getRowCount();
        
        for (int i = 0; i < totalCount; i++) {
            try {
                String studentCode = (String) model.getValueAt(i, 0);
                double regularGrade = Double.parseDouble(model.getValueAt(i, 2).toString());
                double midtermGrade = Double.parseDouble(model.getValueAt(i, 3).toString());
                double finalGrade = Double.parseDouble(model.getValueAt(i, 4).toString());
                
                // Tìm studentId từ studentCode (có thể cải thiện)
                int studentId = getStudentIdByCode(studentCode);
                
                if (studentId > 0) {
                    StudentGrade grade = new StudentGrade();
                    grade.setStudentId(studentId);
                    grade.setSubjectId(subjectId);
                    grade.setRegularGrade(regularGrade);
                    grade.setMidtermGrade(midtermGrade);
                    grade.setFinalGrade(finalGrade);
                    grade.setSemester(semester);
                    grade.setAcademicYear(academicYear);
                    
                    // Tính điểm trung bình
                    grade.setAverageGrade(grade.averageGradeCalculate());
                    
                    if (gradeDAO.createOrUpdateGrade(grade)) {
                        successCount++;
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        JOptionPane.showMessageDialog(this, 
            String.format("Đã lưu %d/%d bản ghi điểm số.", successCount, totalCount),
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        
        // Làm mới dữ liệu
        loadGradesData();
    }

    private int getSubjectIdByName(String subjectName) {
        // Mapping đơn giản - có thể cải thiện bằng cách lấy từ database
        switch (subjectName) {
            case "Toán": return 1;
            case "Lý": return 2;
            case "Hóa": return 3;
            case "Sinh": return 4;
            case "Văn": return 5;
            case "Sử": return 6;
            case "Địa": return 7;
            case "Anh": return 8;
            default: return 1;
        }
    }

    private int getStudentIdByCode(String studentCode) {
        // Có thể cải thiện bằng cách lấy từ database
        // Tạm thời return một giá trị mặc định
        return 1;
    }

    private String getDayOfWeekVietnamese(String dayOfWeek) {
        switch (dayOfWeek) {
            case "MONDAY": return "Thứ 2";
            case "TUESDAY": return "Thứ 3";
            case "WEDNESDAY": return "Thứ 4";
            case "THURSDAY": return "Thứ 5";
            case "FRIDAY": return "Thứ 6";
            case "SATURDAY": return "Thứ 7";
            case "SUNDAY": return "Chủ nhật";
            default: return dayOfWeek;
        }
    }

    private String getClassifiedVietnamese(citd.nhom99.ck.model.constant.Classified classified) {
        switch (classified) {
            case XUAT_SAC: return "Xuất sắc";
            case GIOI: return "Giỏi";
            case KHA: return "Khá";
            case TRUNG_BINH: return "Trung bình";
            case YEU: return "Yếu";
            default: return classified.toString();
        }
    }

    // Custom cell editor cho số
    private static class SpinnerNumberEditor extends DefaultCellEditor {
        public SpinnerNumberEditor(double min, double max, double step) {
            super(new JTextField());
        }
    }
}
