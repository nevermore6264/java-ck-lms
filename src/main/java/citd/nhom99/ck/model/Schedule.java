package citd.nhom99.ck.model;

public class Schedule {
    private int id;
    private int classroomId;
    private Classroom classroom;
    private int subjectId;
    private Subject subject;
    private int teacherId;
    private Teacher teacher;
    private String dayOfWeek; // MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    private int period; // Tiết học (1-12)
    private int semester;
    private int academicYear;

    public Schedule() {
    }

    public Schedule(int classroomId, int subjectId, int teacherId, String dayOfWeek, int period, int semester, int academicYear) {
        this.classroomId = classroomId;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.dayOfWeek = dayOfWeek;
        this.period = period;
        this.semester = semester;
        this.academicYear = academicYear;
    }

    public Schedule(Classroom classroom, Subject subject, Teacher teacher, String dayOfWeek, int period, int semester, int academicYear) {
        this.classroom = classroom;
        this.subject = subject;
        this.teacher = teacher;
        this.dayOfWeek = dayOfWeek;
        this.period = period;
        this.semester = semester;
        this.academicYear = academicYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", classroom=" + (classroom != null ? classroom.getClassName() : "N/A") +
                ", subject=" + (subject != null ? subject.getSubjectName() : "N/A") +
                ", teacher=" + (teacher != null ? teacher.getUser().getFullName() : "N/A") +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", period=" + period +
                ", semester=" + semester +
                ", academicYear=" + academicYear +
                '}';
    }
}
