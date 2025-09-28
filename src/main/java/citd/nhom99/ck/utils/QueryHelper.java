package citd.nhom99.ck.utils;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.Classroom;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.model.User;

import java.sql.*;

public class QueryHelper {
    public QueryHelper() {
    }

    public static void insertSubject(String subjectName) {
        String insertSubjectSQL = "INSERT INTO subjects(subject_name) VALUES(?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSubjectSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, subjectName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to add subject: " + e.getMessage(), e);
        }
    }

    public static void insertUser(User newUser, Role role) {
        String addUserSql = "INSERT INTO users(username, password, full_name, phone_number, email, gender, role) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(addUserSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, newUser.getUsername());
            pstmt.setString(2, newUser.getPassword());
            pstmt.setString(3, newUser.getFullName());
            pstmt.setString(4, newUser.getPhoneNumber());
            pstmt.setString(5, newUser.getEmail());
            pstmt.setString(6, newUser.getGender().name());
            pstmt.setString(7, role.name());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newUser.setUserId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to add user: " + e.getMessage(), e);
        }
    }

    public static void insertTeacher(User newUser) {
        String sql = "INSERT INTO teachers(user_id, teacher_code) VALUES(?, ?)";
        try (Connection conn = DBConfig.getConnection()) {
            conn.setAutoCommit(false);

            QueryHelper.insertUser(newUser, Role.TEACHER);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, newUser.getUserId());
                pstmt.setString(2, Helper.codeGenerate(newUser.getUserId(), newUser.getRole()));
                pstmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add teacher: " + e.getMessage(), e);
        }
    }

    public static void insertTeacher(User newUser, int subjectId) {
        String sql = "INSERT INTO teachers(user_id, teacher_code, subject_id) VALUES(?, ?, ?)";
        try (Connection conn = DBConfig.getConnection()) {
            conn.setAutoCommit(false);

            QueryHelper.insertUser(newUser, Role.TEACHER);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, newUser.getUserId());
                pstmt.setString(2, Helper.codeGenerate(newUser.getUserId(), newUser.getRole()));
                pstmt.setInt(3, subjectId);
                pstmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add teacher: " + e.getMessage(), e);
        }
    }

    public static void insertStudent(User newUser) {
        String sql = "INSERT INTO students (student_code, user_id) VALUES(?, ?)";
        try (Connection conn = DBConfig.getConnection()) {
            conn.setAutoCommit(false);

            QueryHelper.insertUser(newUser, Role.STUDENT);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, Helper.codeGenerate(newUser.getUserId(), newUser.getRole()));
                pstmt.setInt(2, newUser.getUserId());
                pstmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add student: " + e.getMessage(), e);
        }
    }

    public static int insertClassroom(Classroom newClassroom) {
        String insertClassroomSQL = "INSERT INTO classrooms(class_name, gvcn_id) VALUES(?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertClassroomSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, newClassroom.getClassName());
            pstmt.setInt(2, newClassroom.getTeacherId());
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
}