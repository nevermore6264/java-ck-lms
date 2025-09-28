package citd.nhom99.ck.model.dao;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.StudentGrade;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.model.Student;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.utils.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private final UserDAO userDAO = new UserDAO();
    private StudentGradeDAO studentGradeDAO = new StudentGradeDAO();

    public StudentDAO() {
    }

    public void createStudent(User student) {
        String sql = "INSERT INTO students (student_id, student_code) VALUES(?, ?)";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            userDAO.createUser(student, Role.STUDENT);

            pstmt.setInt(1, student.getUserId());
            pstmt.setString(2, Helper.codeGenerate(student.getUserId(), student.getRole()));
            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add student: " + e.getMessage(), e);
        }
    }

    public Student getStudentByCode(String studentCode) {
        String sql = "SELECT * FROM students WHERE student_code = ?";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Student getStudentById(int userId) {
        String sql = "SELECT * FROM students WHERE user_id = ?";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM students";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return students;
    }

    public void updateStudent(User student) {
        System.out.println("DAO: Update student" + student.toString());
    }

    public void deleteStudent(int userId) {
        userDAO.deleteUser(userId);
    }

    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setUser(userDAO.getUserById(rs.getInt("user_id")));
        student.setStudentCode(rs.getString("student_code"));
        student.setStudentGradeId(rs.getInt("grade_id"));
        ClassroomDAO classroomDAO = new ClassroomDAO();
        student.setClassroom(classroomDAO.getClassroomById(rs.getInt("class_id")));

        return student;
    }
}