package citd.nhom99.ck.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.Classroom;
import citd.nhom99.ck.model.Student;
import citd.nhom99.ck.model.Teacher;

public class ClassroomDAO {

    private final TeacherDAO teacherDAO = new TeacherDAO();
    private final StudentDAO studentDAO = new StudentDAO();

    public int createClassroom(Classroom newClassroom) {
        String sql = "INSERT INTO classrooms(class_name, gvcn_id) VALUES(?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, newClassroom.getClassName());
            if (newClassroom.getTeacherId() != 0) {
                pstmt.setInt(2, newClassroom.getTeacherId());
            }
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating classroom failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to add classroom: " + e.getMessage(), e);
        }
    }

    public Classroom getClassroomById(int id) {
        String sql = "SELECT * FROM classrooms WHERE class_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractClassroomFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Classroom> getAllClassrooms() {
        String sql = "SELECT * FROM classrooms";
        List<Classroom> classrooms = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                classrooms.add(extractClassroomFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return classrooms;
    }

    private Classroom extractClassroomFromResultSet(ResultSet rs) throws SQLException {
        int classId = rs.getInt("class_id");
        String className = rs.getString("class_name");
        int gvcnId = rs.getInt("gvcn_id");

        Classroom classroom = new Classroom(classId, className, gvcnId);
        
        // Load teacher information if gvcnId > 0
        if (gvcnId > 0) {
            try {
                Teacher teacher = teacherDAO.getTeacherById(gvcnId);
                classroom.setTeacher(teacher);
            } catch (Exception e) {
                System.out.println("Error loading teacher for classroom " + classId + ": " + e.getMessage());
            }
        }
        
        // Load students for this classroom
        try {
            List<Student> students = getStudentsForClassroom(String.valueOf(classId));
            classroom.setStudents(students);
        } catch (Exception e) {
            System.out.println("Error loading students for classroom " + classId + ": " + e.getMessage());
            classroom.setStudents(new ArrayList<>());
        }

        return classroom;
    }

    public void updateClassroom(Classroom classroom) {
//        String sql = "UPDATE classrooms SET class_name = ?, gvcn_id = ? WHERE class_id = ?";
//        try (Connection conn = DBConfig.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, classroom.getClassName());
//            pstmt.setString(2, classroom.getTeacherId());
//            pstmt.setString(3, classroom.getClassId());
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    }

    public void deleteClassroom(String classroomId) {
        String sql = "DELETE FROM classrooms WHERE class_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, classroomId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<Student> getStudentsForClassroom(String classId) {
        String sql = "SELECT * FROM students WHERE class_id = ?";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, classId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(studentDAO.getStudentById(rs.getInt("user_id")));
            }
        } catch (SQLException e) {
            System.out.println("Error loading students for classroom " + classId + ": " + e.getMessage());
        }
        return students;
    }
}