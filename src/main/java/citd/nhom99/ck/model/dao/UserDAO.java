package citd.nhom99.ck.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.constant.Gender;
import citd.nhom99.ck.model.constant.Role;

public class UserDAO {

    public UserDAO() {
    }

    public void createUser(User user) {
        String sql = "INSERT INTO users(username, password, full_name, phone_number, email, gender, role) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getEmail());
            
            // Handle null gender
            if (user.getGender() != null) {
                pstmt.setString(6, user.getGender().name());
            } else {
                pstmt.setNull(6, Types.VARCHAR);
            }
            
            pstmt.setString(7, user.getRole().name());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to add user: " + e.getMessage(), e);
        }
    }

    public void createUser(User newUser, Role role) {
        String addUserSql = "INSERT INTO users(username, password, full_name, phone_number, email, gender, role) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(addUserSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, newUser.getUsername());
            pstmt.setString(2, newUser.getPassword());
            pstmt.setString(3, newUser.getFullName());
            pstmt.setString(4, newUser.getPhoneNumber());
            pstmt.setString(5, newUser.getEmail());
            
            // Handle null gender
            if (newUser.getGender() != null) {
                pstmt.setString(6, newUser.getGender().name());
            } else {
                pstmt.setNull(6, Types.VARCHAR);
            }
            
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

    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, full_name = ?, phone_number = ?, email = ?, gender = ?, role = ? WHERE user_id = ?";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getEmail());
            
            // Handle null gender
            if (user.getGender() != null) {
                pstmt.setString(6, user.getGender().name());
            } else {
                pstmt.setNull(6, Types.VARCHAR);
            }
            
            // Handle null role
            if (user.getRole() != null) {
                pstmt.setString(7, user.getRole().name());
            } else {
                pstmt.setNull(7, Types.VARCHAR);
            }
            
            pstmt.setInt(8, user.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setEmail(rs.getString("email"));
        
        // Handle null gender values
        String genderStr = rs.getString("gender");
        if (genderStr != null && !genderStr.trim().isEmpty()) {
            try {
                user.setGender(Gender.valueOf(genderStr));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid gender value: " + genderStr + ", setting to null");
                user.setGender(null);
            }
        } else {
            user.setGender(null);
        }
        
        // Handle null role values
        String roleStr = rs.getString("role");
        if (roleStr != null && !roleStr.trim().isEmpty()) {
            try {
                user.setRole(Role.valueOf(roleStr));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role value: " + roleStr + ", setting to null");
                user.setRole(null);
            }
        } else {
            user.setRole(null);
        }
        
        return user;
    }
}