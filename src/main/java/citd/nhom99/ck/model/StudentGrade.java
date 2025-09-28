package citd.nhom99.ck.model;

import citd.nhom99.ck.model.constant.Classified;

public class StudentGrade {

    private int id;
    private double regularGrade;
    private double midtermGrade;
    private double finalGrade;
    private double averageGrade;
    private Classified classified;
    private int semester;
    private int academicYear;
    private int studentId;
    private Student student;
    private int subjectId;
    private Subject subject;

    public StudentGrade() {
    }

    public StudentGrade(int studentId) {
        this.studentId = studentId;
    }

    public StudentGrade(Student student) {
        this.student = student;
    }

    public StudentGrade(int studentId, double regularGrade, double midtermGrade, double finalGrade, double averageGrade, Classified classified, int semester, int academicYear, int subjectId) {
        this.studentId = studentId;
        this.regularGrade = regularGrade;
        this.midtermGrade = midtermGrade;
        this.finalGrade = finalGrade;
        this.averageGrade = averageGrade;
        this.classified = classified;
        this.semester = semester;
        this.academicYear = academicYear;
        this.subjectId = subjectId;
    }

    public StudentGrade(Student student, double regularGrade, double midtermGrade, double finalGrade, double averageGrade, Classified classified, int semester, int academicYear, Subject subject) {
        this.student = student;
        this.regularGrade = regularGrade;
        this.midtermGrade = midtermGrade;
        this.finalGrade = finalGrade;
        this.averageGrade = averageGrade;
        this.classified = classified;
        this.semester = semester;
        this.academicYear = academicYear;
        this.subject = subject;
    }

    public StudentGrade(int id, int studentId, double regularGrade, double midtermGrade, double finalGrade, double averageGrade, Classified classified, int semester, int academicYear, int subjectId) {
        this.id = id;
        this.studentId = studentId;
        this.regularGrade = regularGrade;
        this.midtermGrade = midtermGrade;
        this.finalGrade = finalGrade;
        this.averageGrade = averageGrade;
        this.classified = classified;
        this.semester = semester;
        this.academicYear = academicYear;
        this.subjectId = subjectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRegularGrade() {
        return regularGrade;
    }

    public void setRegularGrade(double regularGrade) {
        this.regularGrade = regularGrade;
    }

    public double getMidtermGrade() {
        return midtermGrade;
    }

    public void setMidtermGrade(double midtermGrade) {
        this.midtermGrade = midtermGrade;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
    }

    public Classified getClassified() {
        return classified;
    }

    public void setClassified(Classified classified) {
        this.classified = classified;
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

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public double averageGradeCalculate() {
        double averageGrade = 0;
        if (this.regularGrade == 0 && this.midtermGrade == 0 && this.finalGrade == 0) {
            return 0;
        }
        averageGrade = (this.regularGrade + this.midtermGrade * 2 + this.finalGrade * 3) / 6;
        setAverageGrade(averageGrade);
        return averageGrade;
    }

    @Override
    public String toString() {
        return "StudentGrade" +
                "id=" + id +
                "regularGrade=" + regularGrade +
                "midtermGrade=" + midtermGrade +
                "finalGrade=" + finalGrade +
                "averageGrade=" + averageGrade +
                "classified=" + classified +
                "academicYear=" + academicYear +
                "studentId=" + studentId +
                "student=" + student +
                "subjectId=" + subjectId +
                "subject=" + subject;
    }
}
