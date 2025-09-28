package citd.nhom99.ck.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.Schedule;

public class ScheduleDAO {
    
    public ScheduleDAO() {
    }

    public List<Schedule> getScheduleByClassroomId(int classroomId, int semester, int academicYear) {
        String sql = "SELECT s.*, c.class_name, sub.subject_name, t.teacher_code, u.full_name " +
                    "FROM schedules s " +
                    "LEFT JOIN classrooms c ON s.classroom_id = c.class_id " +
                    "LEFT JOIN subjects sub ON s.subject_id = sub.subject_id " +
                    "LEFT JOIN teachers t ON s.teacher_id = t.user_id " +
                    "LEFT JOIN users u ON t.user_id = u.user_id " +
                    "WHERE s.classroom_id = ? AND s.semester = ? AND s.academic_year = ? " +
                    "ORDER BY s.day_of_week, s.period";
        
        List<Schedule> schedules = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, classroomId);
            pstmt.setInt(2, semester);
            pstmt.setInt(3, academicYear);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Schedule schedule = extractScheduleFromResultSet(rs);
                schedules.add(schedule);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return schedules;
    }

    public List<Schedule> getScheduleByTeacherId(int teacherId, int semester, int academicYear) {
        String sql = "SELECT s.*, c.class_name, sub.subject_name, t.teacher_code, u.full_name " +
                    "FROM schedules s " +
                    "LEFT JOIN classrooms c ON s.classroom_id = c.class_id " +
                    "LEFT JOIN subjects sub ON s.subject_id = sub.subject_id " +
                    "LEFT JOIN teachers t ON s.teacher_id = t.user_id " +
                    "LEFT JOIN users u ON t.user_id = u.user_id " +
                    "WHERE s.teacher_id = ? AND s.semester = ? AND s.academic_year = ? " +
                    "ORDER BY s.day_of_week, s.period";
        
        List<Schedule> schedules = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teacherId);
            pstmt.setInt(2, semester);
            pstmt.setInt(3, academicYear);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Schedule schedule = extractScheduleFromResultSet(rs);
                schedules.add(schedule);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return schedules;
    }

    public boolean createSchedule(Schedule schedule) {
        String sql = "INSERT INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, schedule.getClassroomId());
            pstmt.setInt(2, schedule.getSubjectId());
            pstmt.setInt(3, schedule.getTeacherId());
            pstmt.setString(4, schedule.getDayOfWeek());
            pstmt.setInt(5, schedule.getPeriod());
            pstmt.setInt(6, schedule.getSemester());
            pstmt.setInt(7, schedule.getAcademicYear());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSchedule(Schedule schedule) {
        String sql = "UPDATE schedules SET classroom_id = ?, subject_id = ?, teacher_id = ?, " +
                    "day_of_week = ?, period = ?, semester = ?, academic_year = ? WHERE id = ?";
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, schedule.getClassroomId());
            pstmt.setInt(2, schedule.getSubjectId());
            pstmt.setInt(3, schedule.getTeacherId());
            pstmt.setString(4, schedule.getDayOfWeek());
            pstmt.setInt(5, schedule.getPeriod());
            pstmt.setInt(6, schedule.getSemester());
            pstmt.setInt(7, schedule.getAcademicYear());
            pstmt.setInt(8, schedule.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSchedule(int scheduleId) {
        String sql = "DELETE FROM schedules WHERE id = ?";
        
        try (Connection conn = DBConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, scheduleId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Schedule extractScheduleFromResultSet(ResultSet rs) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setId(rs.getInt("id"));
        schedule.setClassroomId(rs.getInt("classroom_id"));
        schedule.setSubjectId(rs.getInt("subject_id"));
        schedule.setTeacherId(rs.getInt("teacher_id"));
        schedule.setDayOfWeek(rs.getString("day_of_week"));
        schedule.setPeriod(rs.getInt("period"));
        schedule.setSemester(rs.getInt("semester"));
        schedule.setAcademicYear(rs.getInt("academic_year"));
        
        // Tạo các đối tượng liên quan nếu có dữ liệu
        if (rs.getString("class_name") != null) {
            schedule.setClassroom(new citd.nhom99.ck.model.Classroom(rs.getInt("classroom_id"), rs.getString("class_name"), 0));
        }
        
        if (rs.getString("subject_name") != null) {
            schedule.setSubject(new citd.nhom99.ck.model.Subject(rs.getInt("subject_id"), rs.getString("subject_name")));
        }
        
        if (rs.getString("teacher_code") != null && rs.getString("full_name") != null) {
            citd.nhom99.ck.model.User user = new citd.nhom99.ck.model.User();
            user.setUserId(rs.getInt("teacher_id"));
            user.setFullName(rs.getString("full_name"));
            
            citd.nhom99.ck.model.Teacher teacher = new citd.nhom99.ck.model.Teacher();
            teacher.setUserId(rs.getInt("teacher_id"));
            teacher.setTeacherCode(rs.getString("teacher_code"));
            teacher.setUser(user);
            
            schedule.setTeacher(teacher);
        }
        
        return schedule;
    }
}
