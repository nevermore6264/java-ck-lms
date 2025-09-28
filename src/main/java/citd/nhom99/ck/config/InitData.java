package citd.nhom99.ck.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class InitData {

    public static void initializeSampleData() {
        System.out.println("Initializing sample data...");
        
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            
            insertSampleUsers(stmt);
            
            insertSampleSubjects(stmt);
            
            insertSampleClassrooms(stmt);
            
            insertSampleTeachers(stmt);
            
            insertSampleStudents(stmt);
            
            insertSampleSchedules(stmt);
            
            System.out.println("Sample data initialized successfully.");
            
            System.out.println("Debug: Checking created users...");
            try (ResultSet rs = stmt.executeQuery("SELECT username, password, role FROM users")) {
                while (rs.next()) {
                    System.out.println("User: " + rs.getString("username") + 
                                     " | Password: " + rs.getString("password") + 
                                     " | Role: " + rs.getString("role"));
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error initializing sample data:");
            e.printStackTrace();
        }
    }
    
    private static void insertSampleSubjects(Statement stmt) throws Exception {
        String[] subjects = {
            "INSERT OR IGNORE INTO subjects (subject_id, subject_name) VALUES (1, 'Toán')",
            "INSERT OR IGNORE INTO subjects (subject_id, subject_name) VALUES (2, 'Lý')",
            "INSERT OR IGNORE INTO subjects (subject_id, subject_name) VALUES (3, 'Hóa')",
            "INSERT OR IGNORE INTO subjects (subject_id, subject_name) VALUES (4, 'Sinh')",
            "INSERT OR IGNORE INTO subjects (subject_id, subject_name) VALUES (5, 'Văn')",
            "INSERT OR IGNORE INTO subjects (subject_id, subject_name) VALUES (6, 'Sử')",
            "INSERT OR IGNORE INTO subjects (subject_id, subject_name) VALUES (7, 'Địa')",
            "INSERT OR IGNORE INTO subjects (subject_id, subject_name) VALUES (8, 'Anh')"
        };
        
        for (String sql : subjects) {
            stmt.execute(sql);
        }
    }
    
    private static void insertSampleUsers(Statement stmt) throws Exception {
        String adminSql = "INSERT OR IGNORE INTO users (user_id, username, password, full_name, phone_number, email, gender, role) VALUES (1, 'admin', 'admin', 'Nguyễn Văn Admin', '0123456789', 'admin@school.edu.vn', 'MALE', 'ADMIN')";
        stmt.execute(adminSql);
        
        System.out.println("Admin account created/verified: admin/admin");
    }
    
    private static void insertSampleClassrooms(Statement stmt) throws Exception {
        String[] classrooms = {
            "INSERT OR IGNORE INTO classrooms (class_id, class_name, gvcn_id) VALUES (1, '10A1', 2)",
            "INSERT OR IGNORE INTO classrooms (class_id, class_name, gvcn_id) VALUES (2, '10A2', 3)",
            "INSERT OR IGNORE INTO classrooms (class_id, class_name, gvcn_id) VALUES (3, '11A1', 4)"
        };
        
        for (String sql : classrooms) {
            stmt.execute(sql);
        }
    }
    
    private static void insertSampleTeachers(Statement stmt) throws Exception {
        String[] teachers = {
            "INSERT OR IGNORE INTO teachers (user_id, teacher_code, subject_id, classroom_id) VALUES (2, 'GV001', 1, 1)",
            "INSERT OR IGNORE INTO teachers (user_id, teacher_code, subject_id, classroom_id) VALUES (3, 'GV002', 1, 2)",
            "INSERT OR IGNORE INTO teachers (user_id, teacher_code, subject_id, classroom_id) VALUES (4, 'GV003', 2, 3)"
        };
        
        for (String sql : teachers) {
            stmt.execute(sql);
        }
    }
    
    private static void insertSampleStudents(Statement stmt) throws Exception {
        String[] students = {
            "INSERT OR IGNORE INTO students (user_id, student_code, grade_id, class_id) VALUES (5, 'HS001', 1, 1)",
            "INSERT OR IGNORE INTO students (user_id, student_code, grade_id, class_id) VALUES (6, 'HS002', 1, 1)",
            "INSERT OR IGNORE INTO students (user_id, student_code, grade_id, class_id) VALUES (7, 'HS003', 1, 1)"
        };
        
        for (String sql : students) {
            stmt.execute(sql);
        }
    }
    
    private static void insertSampleSchedules(Statement stmt) throws Exception {
        String[] schedules = {
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 1, 2, 'MONDAY', 1, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 2, 3, 'MONDAY', 2, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 3, 4, 'MONDAY', 3, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 5, 2, 'MONDAY', 4, 1, 10)",
            
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 1, 2, 'TUESDAY', 1, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 4, 3, 'TUESDAY', 2, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 8, 4, 'TUESDAY', 3, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 6, 2, 'TUESDAY', 4, 1, 10)",
            
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 2, 3, 'WEDNESDAY', 1, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 3, 4, 'WEDNESDAY', 2, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 7, 2, 'WEDNESDAY', 3, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 5, 3, 'WEDNESDAY', 4, 1, 10)",
            
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 1, 2, 'THURSDAY', 1, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 4, 3, 'THURSDAY', 2, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 8, 4, 'THURSDAY', 3, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 6, 2, 'THURSDAY', 4, 1, 10)",
            
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 2, 3, 'FRIDAY', 1, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 3, 4, 'FRIDAY', 2, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 7, 2, 'FRIDAY', 3, 1, 10)",
            "INSERT OR IGNORE INTO schedules (classroom_id, subject_id, teacher_id, day_of_week, period, semester, academic_year) VALUES (1, 5, 3, 'FRIDAY', 4, 1, 10)"
        };
        
        for (String sql : schedules) {
            stmt.execute(sql);
        }
    }
}