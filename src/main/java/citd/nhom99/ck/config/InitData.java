package citd.nhom99.ck.config;

import citd.nhom99.ck.model.*;
import citd.nhom99.ck.model.constant.Gender;
import citd.nhom99.ck.model.constant.Role;
import citd.nhom99.ck.model.dao.ClassroomDAO;
import citd.nhom99.ck.utils.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InitData {

    public static void seedDatabaseIfEmpty() {
        if (isDatabaseEmpty()) {
            System.out.println("Database is empty. Seeding with sample data...");
            createSampleData();
        } else {
            System.out.println("Database already contains data. Skipping seeding.");
        }
    }

    private static boolean isDatabaseEmpty() {
        String sql = "SELECT COUNT(user_id) FROM users";
        try (Connection conn = DBConfig.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            return true;
        }
        return false;
    }

    private static void createSampleData() {
        // Admin
        User admin = new User("admin", "admin", "Admin", "0987654321", "admin@admin.edu.vn", Gender.MALE, Role.ADMIN);
        QueryHelper.insertUser(admin, Role.ADMIN);

        // Thêm vào bảng môn học
        QueryHelper.insertSubject("Math");
        QueryHelper.insertSubject("Physics");
        QueryHelper.insertSubject("Chemistry");
        QueryHelper.insertSubject("Biology");
        QueryHelper.insertSubject("Foreign Language");
        QueryHelper.insertSubject("Literature");
        QueryHelper.insertSubject("History");
        QueryHelper.insertSubject("Geography");

        // Thêm vào bảng học sinh
        User student1 = new User("cuong.lv", "123456789", "Lê Văn Cường", "0911111111", "cuong.lv@student.edu.vn", Gender.MALE, Role.STUDENT);
        User student2 = new User("dung.mt", "123456789", "Mai Thị Dung", "0911111112", "dung.mt@student.edu.vn", Gender.FEMALE, Role.STUDENT);
        User student3 = new User("hoa.tt", "123456789", "Trần Thị Hoa", "0911111113", "hoa.tt@student.edu.vn", Gender.FEMALE, Role.STUDENT);
        User student4 = new User("nam.nv", "123456789", "Nguyễn Văn Nam", "0911111114", "nam.nv@student.edu.vn", Gender.MALE, Role.STUDENT);
        User student5 = new User("linh.ht", "123456789", "Hoàng Thị Linh", "0911111115", "linh.ht@student.edu.vn", Gender.FEMALE, Role.STUDENT);
        User student6 = new User("hai.pv", "123456789", "Phạm Văn Hải", "0911111116", "hai.pv@student.edu.vn", Gender.MALE, Role.STUDENT);
        User student7 = new User("trang.lt", "123456789", "Lê Thị Trang", "0911111117", "trang.lt@student.edu.vn", Gender.FEMALE, Role.STUDENT);
        User student8 = new User("son.tv", "123456789", "Trần Văn Sơn", "0911111118", "son.tv@student.edu.vn", Gender.MALE, Role.STUDENT);
        User student9 = new User("thao.tt", "123456789", "Nguyễn Thị Thảo", "0911111119", "thao.nt@student.edu.vn", Gender.FEMALE, Role.STUDENT);
        User student10 = new User("tuan.hv", "123456789", "Hoàng Văn Tuấn", "0911111120", "tuan.hv@student.edu.vn", Gender.MALE, Role.STUDENT);

        QueryHelper.insertStudent(student1);
        QueryHelper.insertStudent(student2);
        QueryHelper.insertStudent(student3);
        QueryHelper.insertStudent(student4);
        QueryHelper.insertStudent(student5);
        QueryHelper.insertStudent(student6);
        QueryHelper.insertStudent(student7);
        QueryHelper.insertStudent(student8);
        QueryHelper.insertStudent(student9);
        QueryHelper.insertStudent(student10);

        // Thêm vào bảng giáo viên
        User teacher1 = new User("teacher1", "teacher1", "Nguyễn Văn An", "0901111111", "an.nv@edu.vn", Gender.MALE, Role.TEACHER);
        User teacher2 = new User("teacher2", "teacher2", "Trần Thị Bình", "0901111112", "binh.tt@edu.vn", Gender.FEMALE, Role.TEACHER);
        User teacher3 = new User("teacher3", "teacher3", "Lê Văn Cường", "0901111113", "cuong.lv@edu.vn", Gender.MALE, Role.TEACHER);
        User teacher4 = new User("teacher4", "teacher4", "Phạm Thị Dung", "0901111114", "dung.pt@edu.vn", Gender.FEMALE, Role.TEACHER);
        User teacher5 = new User("teacher5", "teacher5", "Hoàng Văn Hùng", "0901111115", "hung.hv@edu.vn", Gender.MALE, Role.TEACHER);
        User teacher6 = new User("teacher6", "teacher6", "Ngô Thị Lan", "0901111116", "lan.nt@edu.vn", Gender.FEMALE, Role.TEACHER);
        User teacher7 = new User("teacher7", "teacher7", "Vũ Văn Minh", "0901111117", "minh.vv@edu.vn", Gender.MALE, Role.TEACHER);
        User teacher8 = new User("teacher8", "teacher8", "Đào Thị Nga", "0901111118", "nga.dt@edu.vn", Gender.FEMALE, Role.TEACHER);

        QueryHelper.insertTeacher(teacher1, 1);
        QueryHelper.insertTeacher(teacher2, 2);
        QueryHelper.insertTeacher(teacher3, 3);
        QueryHelper.insertTeacher(teacher4, 4);
        QueryHelper.insertTeacher(teacher5, 5);
        QueryHelper.insertTeacher(teacher6, 6);
        QueryHelper.insertTeacher(teacher7, 7);
        QueryHelper.insertTeacher(teacher8, 8);

        // Thêm lớp
        ClassroomDAO classroomDAO = new ClassroomDAO();

        Classroom class10A1 = new Classroom("10A1", 12);
        Classroom class10A2 = new Classroom("10A2");
        Classroom class10A3 = new Classroom("10A3");
        Classroom class11B1 = new Classroom("11B1");
        Classroom class11B2 = new Classroom("11B2", 13);
        Classroom class11B3 = new Classroom("11B3");
        Classroom class12C1 = new Classroom("12C1");
        Classroom class12C2 = new Classroom("12C2");
        Classroom class12C3 = new Classroom("12C3", 14);

        classroomDAO.createClassroom(class10A1);
        classroomDAO.createClassroom(class10A2);
        classroomDAO.createClassroom(class10A3);
        classroomDAO.createClassroom(class11B1);
        classroomDAO.createClassroom(class11B2);
        classroomDAO.createClassroom(class11B3);
        classroomDAO.createClassroom(class12C1);
        classroomDAO.createClassroom(class12C2);
        classroomDAO.createClassroom(class12C3);
    }
}