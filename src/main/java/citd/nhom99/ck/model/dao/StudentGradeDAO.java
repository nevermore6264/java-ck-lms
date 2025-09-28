package citd.nhom99.ck.model.dao;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.StudentGrade;
import citd.nhom99.ck.model.constant.Classified;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentGradeDAO {
//    private final StudentDAO studentDAO = new StudentDAO();
    public StudentGradeDAO() {
    }

    public void createStudentGrade(int studentId) {
        String sql = "INSERT INTO student_grades (student_code, user_id, class_id, grade_id) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (studentId < 0) {
                System.out.println("studentId is negative");
            }

            pstmt.setInt(1, studentId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public StudentGrade getStudentGradeByStudentId(int studentId) {
        String sql = "SELECT * FROM student_grades WHERE student_id = ?";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractStudentGradeFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getStudentGradeDetail(int studentId) {
        String sql = "SELECT * FROM student_grades WHERE student_id = ?";
    }

    private StudentGrade extractStudentGradeFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int studentId = rs.getInt("student_id");
        double regularGrade = rs.getDouble("regular_grade");
        double midtermGrade = rs.getDouble("midterm_grade");
        double finalGrade = rs.getDouble("final_grade");
        double averageGrade = rs.getDouble("average_grade");
        Classified classified = Classified.valueOf("classified");
        int semester = rs.getInt("semester");
        int academicYear = rs.getInt("academic_year");
        int subjectId = rs.getInt("subject_id");

        return new StudentGrade(id, studentId, regularGrade, midtermGrade, finalGrade, averageGrade, classified, semester, academicYear, subjectId);
    }
}
