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
            
            insertSampleStudentGrades(stmt);
            
            System.out.println("Sample data initialized successfully.");
            
            System.out.println("\n=== DEBUG: Checking created data ===");
            
            // Check Users
            System.out.println("\n--- USERS ---");
            try (ResultSet rs = stmt.executeQuery("SELECT user_id, username, password, full_name, email, phone_number, gender, role FROM users ORDER BY user_id")) {
                while (rs.next()) {
                    System.out.println("User: " + rs.getString("username") + 
                                     " | Password: " + rs.getString("password") + 
                                     " | Full Name: " + rs.getString("full_name") +
                                     " | Email: " + rs.getString("email") +
                                     " | Phone: " + rs.getString("phone_number") +
                                     " | Gender: " + rs.getString("gender") +
                                     " | Role: " + rs.getString("role"));
                }
            }
            
            // Check Subjects
            System.out.println("\n--- SUBJECTS ---");
            try (ResultSet rs = stmt.executeQuery("SELECT subject_id, subject_name FROM subjects ORDER BY subject_id")) {
                while (rs.next()) {
                    System.out.println("Subject ID: " + rs.getInt("subject_id") + 
                                     " | Name: " + rs.getString("subject_name"));
                }
            }
            
            // Check Classrooms
            System.out.println("\n--- CLASSROOMS ---");
            try (ResultSet rs = stmt.executeQuery("SELECT class_id, class_name, gvcn_id FROM classrooms ORDER BY class_id")) {
                while (rs.next()) {
                    System.out.println("Class ID: " + rs.getInt("class_id") + 
                                     " | Name: " + rs.getString("class_name") +
                                     " | GVCN ID: " + rs.getInt("gvcn_id"));
                }
            }
            
            // Check Teachers
            System.out.println("\n--- TEACHERS ---");
            try (ResultSet rs = stmt.executeQuery("SELECT t.user_id, t.teacher_code, t.subject_id, t.classroom_id, u.full_name FROM teachers t LEFT JOIN users u ON t.user_id = u.user_id ORDER BY t.user_id")) {
                while (rs.next()) {
                    System.out.println("Teacher ID: " + rs.getInt("user_id") + 
                                     " | Code: " + rs.getString("teacher_code") +
                                     " | Subject ID: " + rs.getInt("subject_id") +
                                     " | Classroom ID: " + rs.getInt("classroom_id") +
                                     " | Full Name: " + rs.getString("full_name"));
                }
            }
            
            // Check Students
            System.out.println("\n--- STUDENTS ---");
            try (ResultSet rs = stmt.executeQuery("SELECT s.user_id, s.student_code, s.grade_id, s.class_id, u.full_name, c.class_name FROM students s LEFT JOIN users u ON s.user_id = u.user_id LEFT JOIN classrooms c ON s.class_id = c.class_id ORDER BY s.user_id")) {
                while (rs.next()) {
                    System.out.println("Student ID: " + rs.getInt("user_id") + 
                                     " | Code: " + rs.getString("student_code") +
                                     " | Grade ID: " + rs.getInt("grade_id") +
                                     " | Class ID: " + rs.getInt("class_id") +
                                     " | Class Name: " + rs.getString("class_name") +
                                     " | Full Name: " + rs.getString("full_name"));
                }
            }
            
            // Check Schedules
            System.out.println("\n--- SCHEDULES ---");
            try (ResultSet rs = stmt.executeQuery("SELECT s.id, s.classroom_id, c.class_name, s.subject_id, sub.subject_name, s.teacher_id, u.full_name as teacher_name, s.day_of_week, s.period, s.semester, s.academic_year FROM schedules s LEFT JOIN classrooms c ON s.classroom_id = c.class_id LEFT JOIN subjects sub ON s.subject_id = sub.subject_id LEFT JOIN users u ON s.teacher_id = u.user_id ORDER BY s.classroom_id, s.day_of_week, s.period")) {
                while (rs.next()) {
                    System.out.println("Schedule ID: " + rs.getInt("id") + 
                                     " | Class: " + rs.getString("class_name") + " (ID:" + rs.getInt("classroom_id") + ")" +
                                     " | Subject: " + rs.getString("subject_name") + " (ID:" + rs.getInt("subject_id") + ")" +
                                     " | Teacher: " + rs.getString("teacher_name") + " (ID:" + rs.getInt("teacher_id") + ")" +
                                     " | Day: " + rs.getString("day_of_week") +
                                     " | Period: " + rs.getInt("period") +
                                     " | Semester: " + rs.getInt("semester") +
                                     " | Academic Year: " + rs.getInt("academic_year"));
                }
            }
            
            // Check Student Grades
            System.out.println("\n--- STUDENT GRADES ---");
            try (ResultSet rs = stmt.executeQuery("SELECT sg.id, sg.student_id, s.student_code, u.full_name as student_name, sg.subject_id, sub.subject_name, sg.regular_grade, sg.midterm_grade, sg.final_grade, sg.average_grade, sg.classified, sg.semester, sg.academic_year FROM student_grades sg LEFT JOIN students s ON sg.student_id = s.user_id LEFT JOIN users u ON s.user_id = u.user_id LEFT JOIN subjects sub ON sg.subject_id = sub.subject_id ORDER BY sg.student_id, sg.subject_id")) {
                while (rs.next()) {
                    System.out.println("Grade ID: " + rs.getInt("id") + 
                                     " | Student: " + rs.getString("student_name") + " (" + rs.getString("student_code") + ")" +
                                     " | Subject: " + rs.getString("subject_name") + " (ID:" + rs.getInt("subject_id") + ")" +
                                     " | Regular: " + rs.getDouble("regular_grade") +
                                     " | Midterm: " + rs.getDouble("midterm_grade") +
                                     " | Final: " + rs.getDouble("final_grade") +
                                     " | Average: " + rs.getDouble("average_grade") +
                                     " | Classified: " + rs.getString("classified") +
                                     " | Semester: " + rs.getInt("semester") +
                                     " | Academic Year: " + rs.getInt("academic_year"));
                }
            }
            
            System.out.println("\n=== END DEBUG ===\n");
            
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
        String[] users = {
            "INSERT OR IGNORE INTO users (user_id, username, password, full_name, phone_number, email, gender, role) VALUES (1, 'admin', 'admin', 'Nguyễn Văn Admin', '0123456789', 'admin@school.edu.vn', 'MALE', 'ADMIN')",
            "INSERT OR IGNORE INTO users (user_id, username, password, full_name, phone_number, email, gender, role) VALUES (2, 'teacher1', 'teacher1', 'Nguyễn Văn Giáo viên 1', '0123456780', 'teacher1@school.edu.vn', 'MALE', 'TEACHER')",
            "INSERT OR IGNORE INTO users (user_id, username, password, full_name, phone_number, email, gender, role) VALUES (3, 'teacher2', 'teacher2', 'Trần Thị Giáo viên 2', '0123456781', 'teacher2@school.edu.vn', 'FEMALE', 'TEACHER')",
            "INSERT OR IGNORE INTO users (user_id, username, password, full_name, phone_number, email, gender, role) VALUES (4, 'teacher3', 'teacher3', 'Lê Văn Giáo viên 3', '0123456782', 'teacher3@school.edu.vn', 'MALE', 'TEACHER')",
            "INSERT OR IGNORE INTO users (user_id, username, password, full_name, phone_number, email, gender, role) VALUES (5, 'cuong.lv', '123456789', 'Lê Văn Cường', '0123456783', 'cuong.lv@school.edu.vn', 'MALE', 'STUDENT')",
            "INSERT OR IGNORE INTO users (user_id, username, password, full_name, phone_number, email, gender, role) VALUES (6, 'dung.mt', '123456789', 'Mai Thị Dung', '0123456784', 'dung.mt@school.edu.vn', 'FEMALE', 'STUDENT')",
            "INSERT OR IGNORE INTO users (user_id, username, password, full_name, phone_number, email, gender, role) VALUES (7, 'hoa.tt', '123456789', 'Trần Thị Hoa', '0123456785', 'hoa.tt@school.edu.vn', 'FEMALE', 'STUDENT')"
        };
        
        for (String sql : users) {
            stmt.execute(sql);
        }
        
        System.out.println("Sample users created/verified");
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
    
    private static void insertSampleStudentGrades(Statement stmt) throws Exception {
        String[] grades = {
            // Điểm cho học sinh 5 (cuong.lv) - lớp 10A1
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (8.5, 8.0, 9.0, 8.5, 'GIOI', 1, 10, 5, 1)",
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (7.5, 7.0, 8.0, 7.5, 'KHA', 1, 10, 5, 2)",
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (9.0, 8.5, 9.5, 9.0, 'GIOI', 1, 10, 5, 3)",
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (8.0, 7.5, 8.5, 8.0, 'GIOI', 1, 10, 5, 5)",
            
            // Điểm cho học sinh 6 (dung.mt) - lớp 10A1
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (9.5, 9.0, 9.5, 9.3, 'XUAT_SAC', 1, 10, 6, 1)",
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (8.0, 8.5, 8.0, 8.2, 'GIOI', 1, 10, 6, 2)",
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (7.0, 7.5, 7.0, 7.2, 'KHA', 1, 10, 6, 3)",
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (8.5, 8.0, 9.0, 8.5, 'GIOI', 1, 10, 6, 5)",
            
            // Điểm cho học sinh 7 (hoa.tt) - lớp 10A1
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (6.5, 6.0, 7.0, 6.5, 'TRUNG_BINH', 1, 10, 7, 1)",
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (7.0, 7.5, 7.0, 7.2, 'KHA', 1, 10, 7, 2)",
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (8.0, 8.5, 8.0, 8.2, 'GIOI', 1, 10, 7, 3)",
            "INSERT OR IGNORE INTO student_grades (regular_grade, midterm_grade, final_grade, average_grade, classified, semester, academic_year, student_id, subject_id) VALUES (9.0, 8.5, 9.5, 9.0, 'GIOI', 1, 10, 7, 5)"
        };
        
        for (String sql : grades) {
            stmt.execute(sql);
        }
    }
}