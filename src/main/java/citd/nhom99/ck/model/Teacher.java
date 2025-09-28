package citd.nhom99.ck.model;

public class Teacher {
    private int userId;
    private String teacherCode;
    private User user;
    private int subjectId;
    private Subject subject;
    private int classroomId;
    private Classroom classroom;

    public Teacher() {
    }

    public Teacher(String teacherCode, User user) {
        this.teacherCode = teacherCode;
        this.user = user;
    }

    public Teacher(String teacherCode, User user, Subject subject) {
        this.teacherCode = teacherCode;
        this.user = user;
        this.subject = subject;
    }

    public Teacher(User user, String teacherCode, Subject subject, Classroom classroom) {
        this.teacherCode = teacherCode;
        this.user = user;
        this.subject = subject;
        this.classroom = classroom;
    }

    public Teacher(int userId, String teacherCode, int subjectId, int classroomId) {
        this.userId = userId;
        this.teacherCode = teacherCode;
        this.subjectId = subjectId;
        this.classroomId = classroomId;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
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
        return "Teacher: " +
                "userId=" + userId +
                ", teacherCode='" + teacherCode +
                ", user=" + user.getFullName() +
                ", subject=" + subject.getSubjectName() +
                ", classroom=" + classroom.getClassName();
    }
}
