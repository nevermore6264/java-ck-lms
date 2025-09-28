package citd.nhom99.ck.model.dao;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.model.Teacher;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.utils.Helper;
import citd.nhom99.ck.utils.QueryHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {
    private final UserDAO userDAO = new UserDAO();

    public TeacherDAO() {
    }

    public void createTeacher(User teacher) {
        String sql = "INSERT INTO teachers(user_id, teacher_code) VALUES(?, ?)";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            userDAO.createUser(teacher, Role.TEACHER);

            pstmt.setInt(1, teacher.getUserId());
            pstmt.setString(2, Helper.codeGenerate(teacher.getUserId(), teacher.getRole()));
            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add teacher: " + e.getMessage(), e);
        }
    }

    public void createTeacher(User newUser, int subjectId) {
        String sql = "INSERT INTO teachers(user_id, teacher_code, subject_id) VALUES(?, ?, ?)";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            userDAO.createUser(newUser, Role.TEACHER);

            pstmt.setInt(1, newUser.getUserId());
            pstmt.setString(2, Helper.codeGenerate(newUser.getUserId(), newUser.getRole()));
            pstmt.setInt(3, subjectId);
            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add teacher: " + e.getMessage(), e);
        }
    }

    public Teacher getTeacherById(int teacherId) {
        String sql = "SELECT * FROM teachers WHERE teacher_code = ?";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractTeacherFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Teacher> getAllTeachers() {
        String sql = "SELECT * FROM teachers";
        List<Teacher> teachers = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                teachers.add(extractTeacherFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return teachers;
    }

    public void updateTeacher(Teacher teacher) {
        userDAO.updateUser(teacher.getUser());

        String sql = "UPDATE teachers SET subject_id = ? WHERE teacher_code = ?";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teacher.getSubjectId());
            pstmt.setString(2, teacher.getTeacherCode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteTeacher(int userId) {
        userDAO.deleteUser(userId);
    }

    private Teacher extractTeacherFromResultSet(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTeacherCode(rs.getString("teacher_code"));
        teacher.setUser(userDAO.getUserById(rs.getInt("user_id")));
        teacher.setSubjectId(rs.getInt("subject_id"));

        return teacher;
    }
}