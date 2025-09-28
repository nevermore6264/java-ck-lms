package citd.nhom99.ck.view.student;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import citd.nhom99.ck.controller.AppController;
import citd.nhom99.ck.model.Schedule;
import citd.nhom99.ck.model.Student;
import citd.nhom99.ck.model.StudentGrade;
import citd.nhom99.ck.model.dao.ScheduleDAO;
import citd.nhom99.ck.model.dao.StudentGradeDAO;

public class StudentPanel extends JPanel {
    private final AppController controller;
    private final Student student;
    private final StudentGradeDAO gradeDAO;
    private final ScheduleDAO scheduleDAO;
    
    private JTabbedPane tabbedPane;
    private JTable scheduleTable;
    private JTable gradesTable;
    private JComboBox<String> semesterCombo;
    private JComboBox<String> academicYearCombo;
    private JComboBox<String> gradesSemesterCombo;
    private JComboBox<String> gradesAcademicYearCombo;

    public StudentPanel(Student student, AppController controller) {
        this.student = student;
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
        
        // Tab thời khóa biểu
        JPanel schedulePanel = createSchedulePanel();
        tabbedPane.addTab("Thời khóa biểu", schedulePanel);
        
        // Tab điểm số
        JPanel gradesPanel = createGradesPanel();
        tabbedPane.addTab("Điểm số", gradesPanel);
        
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
        
        // Bảng thời khóa biểu
        String[] scheduleColumns = {"Thứ", "Tiết", "Môn học", "Giáo viên", "Phòng học"};
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
        
        JButton refreshButton = new JButton("Làm mới");
        refreshButton.addActionListener(e -> loadGradesData());
        
        controlPanel.add(semesterLabel);
        controlPanel.add(gradesSemesterCombo);
        controlPanel.add(academicYearLabel);
        controlPanel.add(gradesAcademicYearCombo);
        controlPanel.add(refreshButton);
        
        // Bảng điểm số
        String[] gradesColumns = {"Môn học", "Điểm thường xuyên", "Điểm giữa kỳ", "Điểm cuối kỳ", "Điểm TB", "Xếp loại"};
        DefaultTableModel gradesModel = new DefaultTableModel(gradesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gradesTable = new JTable(gradesModel);
        gradesTable.setRowHeight(30);
        gradesTable.getTableHeader().setReorderingAllowed(false);
        
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
            List<Schedule> schedules = scheduleDAO.getScheduleByClassroomId(
                student.getClassroomId(), semester, academicYear);
            
            DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();
            model.setRowCount(0);
            
            for (Schedule schedule : schedules) {
                Object[] row = {
                    getDayOfWeekVietnamese(schedule.getDayOfWeek()),
                    schedule.getPeriod(),
                    schedule.getSubject() != null ? schedule.getSubject().getSubjectName() : "N/A",
                    schedule.getTeacher() != null ? schedule.getTeacher().getUser().getFullName() : "N/A",
                    "Phòng " + schedule.getClassroomId() // Có thể thay bằng thông tin phòng thực tế
                };
                model.addRow(row);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải thời khóa biểu: " + e.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadGradesData() {
        int semester = Integer.parseInt((String) gradesSemesterCombo.getSelectedItem());
        int academicYear = Integer.parseInt((String) gradesAcademicYearCombo.getSelectedItem());
        
        try {
            List<StudentGrade> grades = gradeDAO.getGradesByStudentId(
                student.getUserId(), semester, academicYear);
            
            DefaultTableModel model = (DefaultTableModel) gradesTable.getModel();
            model.setRowCount(0);
            
            for (StudentGrade grade : grades) {
                Object[] row = {
                    grade.getSubject() != null ? grade.getSubject().getSubjectName() : "N/A",
                    String.format("%.1f", grade.getRegularGrade()),
                    String.format("%.1f", grade.getMidtermGrade()),
                    String.format("%.1f", grade.getFinalGrade()),
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
}
