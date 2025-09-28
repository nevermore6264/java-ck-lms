package citd.nhom99.ck.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaManager {

    private static final String CREATE_USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
                user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                full_name TEXT NOT NULL,
                phone_number TEXT,
                email TEXT UNIQUE,
                gender TEXT CHECK(gender IN ('MALE', 'FEMALE')),
                role TEXT NOT NULL CHECK(role IN ('STUDENT', 'TEACHER', 'ADMIN'))
            );
            """;

    private static final String CREATE_SUBJECTS_TABLE = """
            CREATE TABLE IF NOT EXISTS subjects (
                subject_id INTEGER PRIMARY KEY AUTOINCREMENT,
                subject_name TEXT NOT NULL UNIQUE
            );
            """;

    private static final String CREATE_TEACHERS_TABLE = """
            CREATE TABLE IF NOT EXISTS teachers (
                user_id INTEGER PRIMARY KEY,
                teacher_code TEXT NOT NULL UNIQUE,
                subject_id INTEGER,
                classroom_id INTEGER,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY (subject_id) REFERENCES subjects(subject_id),
                FOREIGN KEY (classroom_id) REFERENCES classrooms(class_id)
            );
            """;

    private static final String CREATE_CLASSROOMS_TABLE = """
            CREATE TABLE IF NOT EXISTS classrooms (
                class_id INTEGER PRIMARY KEY AUTOINCREMENT,
                class_name TEXT NOT NULL UNIQUE,
                gvcn_id INTEGER NULL,
                FOREIGN KEY (gvcn_id) REFERENCES teachers(user_id) ON DELETE CASCADE
            );
            """;

    private static final String CREATE_STUDENTS_TABLE = """
            CREATE TABLE IF NOT EXISTS students (
                user_id INTEGER PRIMARY KEY,
                student_code TEXT NOT NULL UNIQUE,
                grade_id INTEGER,
                class_id INTEGER,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY (class_id) REFERENCES classrooms(class_id) ON DELETE CASCADE,
                FOREIGN KEY (grade_id) REFERENCES student_grades(id) ON DELETE CASCADE
            );
            """;

    private static final String CREATE_STUDENT_GRADE_TABLE = """
            CREATE TABLE IF NOT EXISTS student_grades (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                regular_grade REAL DEFAULT 0.0 CHECK(regular_grade >= 0 AND regular_grade <= 10),
                midterm_grade REAL DEFAULT 0.0 CHECK(midterm_grade >= 0 AND midterm_grade <= 10),
                final_grade REAL DEFAULT 0.0 CHECK(final_grade >= 0 AND final_grade <= 10),
                average_grade REAL DEFAULT 0.0 CHECK(average_grade >= 0 AND average_grade <= 10),
                classified TEXT CHECK(classified IN ('YEU', 'TRUNG_BINH', 'KHA', 'GIOI', 'XUAT_SAC')),
                semester INTEGER CHECK(semester IN (1, 2)),
                academic_year INTEGER CHECK(academic_year IN (10, 11, 12)),
                student_id INTEGER NOT NULL,
                subject_id INTEGER NOT NULL,
                FOREIGN KEY (student_id) REFERENCES students(user_id) ON DELETE CASCADE,
                FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
                UNIQUE(student_id, subject_id, semester, academic_year)
            );
            """;

    public static void initializeDatabase() {
        System.out.println("Initializing database schema based on models...");
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(CREATE_USERS_TABLE);
            stmt.execute(CREATE_SUBJECTS_TABLE);
            stmt.execute(CREATE_TEACHERS_TABLE);
            stmt.execute(CREATE_CLASSROOMS_TABLE);
            stmt.execute(CREATE_STUDENTS_TABLE);
            stmt.execute(CREATE_STUDENT_GRADE_TABLE);

            System.out.println("Database schema initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database schema:");
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
}