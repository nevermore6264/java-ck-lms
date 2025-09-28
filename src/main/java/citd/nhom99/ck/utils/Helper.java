package citd.nhom99.ck.utils;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.constant.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Helper {
    public static boolean isAdmin(Role role) {
        return role == Role.ADMIN;
    }

    public static String codeGenerate(int id, Role role) {
        String code = "";
        switch (role) {
            case STUDENT:
                code = "HS" + String.format("%06d", id);
                break;
            case TEACHER:
                code = "GV" + String.format("%06d", id);
                break;
        }
        return code;
    }

    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
