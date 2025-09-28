package citd.nhom99.ck.model;

public class Student {
    private int userId;
    private User user;
    private String studentCode;
    private int studentGradeId;
    private StudentGrade studentGrade;
    private int classroomId;
    private Classroom classroom;

    public Student() {
    }

    public Student(User user, String studentCode) {
        this.user = user;
        this.studentCode = studentCode;
    }

    public Student(int userId, String studentCode) {
        this.userId = userId;
        this.studentCode = studentCode;
    }

    public Student(User user, String studentCode, StudentGrade studentGrade, Classroom classroom) {
        this.user = user;
        this.studentCode = studentCode;
        this.studentGrade = studentGrade;
        this.classroom = classroom;
    }

    public Student(int userId, String studentCode, int studentGradeId, int classroomId) {
        this.userId = userId;
        this.studentCode = studentCode;
        this.studentGradeId = studentGradeId;
        this.classroomId = classroomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user != null ? user : new User();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public int getStudentGradeId() {
        return studentGradeId;
    }

    public void setStudentGradeId(int studentGradeId) {
        this.studentGradeId = studentGradeId;
    }

    public StudentGrade getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(StudentGrade averageGrade) {
        this.studentGrade = averageGrade;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return "Student:" +
                "userId=" + user.getUserId() +
                "studentCode='" + studentCode +
                "user=" + user.getFullName() +
                "email=" + user.getEmail() +
                "grade=" + studentGrade.toString();
    }
}
