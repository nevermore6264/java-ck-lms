package citd.nhom99.ck.model.dao;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    
    public SubjectDAO() {
    }

    public Subject getSubjectById(int subjectId) {
        String sql = "SELECT * FROM subjects WHERE subject_id = ?";
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractSubjectFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Subject> getAllSubjects() {
        String sql = "SELECT * FROM subjects ORDER BY subject_name";
        List<Subject> subjects = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                subjects.add(extractSubjectFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return subjects;
    }

    public boolean createSubject(Subject subject) {
        String sql = "INSERT INTO subjects (subject_name) VALUES (?)";
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subject.getSubjectName());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE subjects SET subject_name = ? WHERE subject_id = ?";
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subject.getSubjectName());
            pstmt.setInt(2, subject.getSubjectId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSubject(int subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Subject extractSubjectFromResultSet(ResultSet rs) throws SQLException {
        int subjectId = rs.getInt("subject_id");
        String subjectName = rs.getString("subject_name");
        
        return new Subject(subjectId, subjectName);
    }
}
