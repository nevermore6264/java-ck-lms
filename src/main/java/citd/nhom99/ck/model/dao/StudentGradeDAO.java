package citd.nhom99.ck.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.StudentGrade;
import citd.nhom99.ck.model.constant.Classified;

public class StudentGradeDAO {
    
    public StudentGradeDAO() {
    }

    public List<StudentGrade> getGradesByStudentId(int studentId, int semester, int academicYear) {
        String sql = "SELECT sg.*, s.subject_name, st.student_code, u.full_name " +
                    "FROM student_grades sg " +
                    "LEFT JOIN subjects s ON sg.subject_id = s.subject_id " +
                    "LEFT JOIN students st ON sg.student_id = st.user_id " +
                    "LEFT JOIN users u ON st.user_id = u.user_id " +
                    "WHERE sg.student_id = ? AND sg.semester = ? AND sg.academic_year = ? " +
                    "ORDER BY s.subject_name";
        
        List<StudentGrade> grades = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, semester);
            pstmt.setInt(3, academicYear);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                StudentGrade grade = extractStudentGradeFromResultSet(rs);
                grades.add(grade);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return grades;
    }

    public List<StudentGrade> getGradesByClassroomId(int classroomId, int subjectId, int semester, int academicYear) {
        String sql = "SELECT sg.*, s.subject_name, st.student_code, u.full_name " +
                    "FROM student_grades sg " +
                    "LEFT JOIN subjects s ON sg.subject_id = s.subject_id " +
                    "LEFT JOIN students st ON sg.student_id = st.user_id " +
                    "LEFT JOIN users u ON st.user_id = u.user_id " +
                    "WHERE st.class_id = ? AND sg.subject_id = ? AND sg.semester = ? AND sg.academic_year = ? " +
                    "ORDER BY st.student_code";
        
        List<StudentGrade> grades = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, classroomId);
            pstmt.setInt(2, subjectId);
            pstmt.setInt(3, semester);
            pstmt.setInt(4, academicYear);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                StudentGrade grade = extractStudentGradeFromResultSet(rs);
                grades.add(grade);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return grades;
    }

    public boolean createOrUpdateGrade(StudentGrade grade) {
        String sql = "INSERT OR REPLACE INTO student_grades " +
                    "(student_id, subject_id, regular_grade, midterm_grade, final_grade, average_grade, " +
                    "classified, semester, academic_year) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, grade.getStudentId());
            pstmt.setInt(2, grade.getSubjectId());
            pstmt.setDouble(3, grade.getRegularGrade());
            pstmt.setDouble(4, grade.getMidtermGrade());
            pstmt.setDouble(5, grade.getFinalGrade());
            pstmt.setDouble(6, grade.getAverageGrade());
            pstmt.setString(7, grade.getClassified() != null ? grade.getClassified().toString() : null);
            pstmt.setInt(8, grade.getSemester());
            pstmt.setInt(9, grade.getAcademicYear());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGrade(int gradeId, double regularGrade, double midtermGrade, double finalGrade) {
        // Tính điểm trung bình
        double averageGrade = (regularGrade + midtermGrade * 2 + finalGrade * 3) / 6;
        
        // Xác định xếp loại
        Classified classified = determineClassified(averageGrade);
        
        String sql = "UPDATE student_grades SET regular_grade = ?, midterm_grade = ?, final_grade = ?, " +
                    "average_grade = ?, classified = ? WHERE id = ?";
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, regularGrade);
            pstmt.setDouble(2, midtermGrade);
            pstmt.setDouble(3, finalGrade);
            pstmt.setDouble(4, averageGrade);
            pstmt.setString(5, classified.toString());
            pstmt.setInt(6, gradeId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Classified determineClassified(double averageGrade) {
        if (averageGrade >= 9.0) return Classified.XUAT_SAC;
        else if (averageGrade >= 8.0) return Classified.GIOI;
        else if (averageGrade >= 6.5) return Classified.KHA;
        else if (averageGrade >= 5.0) return Classified.TRUNG_BINH;
        else return Classified.YEU;
    }

    private StudentGrade extractStudentGradeFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int studentId = rs.getInt("student_id");
        double regularGrade = rs.getDouble("regular_grade");
        double midtermGrade = rs.getDouble("midterm_grade");
        double finalGrade = rs.getDouble("final_grade");
        double averageGrade = rs.getDouble("average_grade");
        
        String classifiedStr = rs.getString("classified");
        Classified classified = null;
        if (classifiedStr != null) {
            classified = Classified.valueOf(classifiedStr);
        }
        
        int semester = rs.getInt("semester");
        int academicYear = rs.getInt("academic_year");
        int subjectId = rs.getInt("subject_id");

        StudentGrade grade = new StudentGrade(id, studentId, regularGrade, midtermGrade, finalGrade, 
                                            averageGrade, classified, semester, academicYear, subjectId);
        
        // Tạo đối tượng Subject nếu có dữ liệu
        if (rs.getString("subject_name") != null) {
            grade.setSubject(new citd.nhom99.ck.model.Subject(subjectId, rs.getString("subject_name")));
        }
        
        // Tạo đối tượng Student nếu có dữ liệu
        if (rs.getString("student_code") != null && rs.getString("full_name") != null) {
            citd.nhom99.ck.model.User user = new citd.nhom99.ck.model.User();
            user.setUserId(studentId);
            user.setFullName(rs.getString("full_name"));
            
            citd.nhom99.ck.model.Student student = new citd.nhom99.ck.model.Student();
            student.setUserId(studentId);
            student.setStudentCode(rs.getString("student_code"));
            student.setUser(user);
            
            grade.setStudent(student);
        }

        return grade;
    }
}
