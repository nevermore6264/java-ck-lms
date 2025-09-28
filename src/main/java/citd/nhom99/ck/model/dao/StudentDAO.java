package citd.nhom99.ck.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.Student;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.utils.Helper;

public class StudentDAO {
    private final UserDAO userDAO = new UserDAO();
    private StudentGradeDAO studentGradeDAO = new StudentGradeDAO();

    public StudentDAO() {
    }

    public void createStudent(User student) {
        String sql = "INSERT INTO students (user_id, student_code) VALUES(?, ?)";
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
                Student student = extractStudentFromResultSet(rs);
                
                // Load classroom information if class_id > 0
                if (student.getClassroomId() > 0) {
                    try {
                        ClassroomDAO classroomDAO = new ClassroomDAO();
                        student.setClassroom(classroomDAO.getClassroomById(student.getClassroomId()));
                    } catch (Exception e) {
                        System.out.println("Error loading classroom for student " + userId + ": " + e.getMessage());
                    }
                }
                
                return student;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Student getStudentByUserId(int userId) {
        return getStudentById(userId);
    }

    public List<Student> getAllStudents() {
        String sql = "SELECT s.*, c.class_name " +
                    "FROM students s " +
                    "LEFT JOIN classrooms c ON s.class_id = c.class_id";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Student student = extractStudentFromResultSet(rs);
                
                // Set classroom name directly from JOIN result
                String className = rs.getString("class_name");
                if (className != null && student.getClassroomId() > 0) {
                    // Create a simple classroom object with just the name
                    citd.nhom99.ck.model.Classroom classroom = new citd.nhom99.ck.model.Classroom(
                        student.getClassroomId(), className, 0);
                    student.setClassroom(classroom);
                }
                
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return students;
    }

    public void updateStudent(User student) {
        // Update user information first
        userDAO.updateUser(student);
        
        // Update student-specific information if needed
        String sql = "UPDATE students SET student_code = ? WHERE user_id = ?";
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, Helper.codeGenerate(student.getUserId(), student.getRole()));
            pstmt.setInt(2, student.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("DAO: Updated student " + student.getUserId());
            } else {
                System.out.println("DAO: No student found with ID " + student.getUserId());
            }
            
        } catch (SQLException e) {
            System.out.println("DAO: Error updating student: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void updateStudentClassroom(int studentId, int classroomId) {
        String sql = "UPDATE students SET class_id = ? WHERE user_id = ?";
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, classroomId);
            pstmt.setInt(2, studentId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("DAO: Updated classroom for student " + studentId + " to classroom " + classroomId);
            } else {
                System.out.println("DAO: No student found with ID " + studentId);
            }
            
        } catch (SQLException e) {
            System.out.println("DAO: Error updating student classroom: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteStudent(int userId) {
        userDAO.deleteUser(userId);
    }

    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setUser(userDAO.getUserById(rs.getInt("user_id")));
        student.setStudentCode(rs.getString("student_code"));
        student.setStudentGradeId(rs.getInt("grade_id"));
        student.setClassroomId(rs.getInt("class_id"));
        
        // Load student grade information
        if (rs.getInt("grade_id") > 0) {
            student.setStudentGrade(studentGradeDAO.getStudentGradeById(rs.getInt("grade_id")));
        }

        return student;
    }
    
    // Method to get student without loading classroom (used by ClassroomDAO to avoid circular dependency)
    public Student getStudentByIdWithoutClassroom(int userId) {
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
}