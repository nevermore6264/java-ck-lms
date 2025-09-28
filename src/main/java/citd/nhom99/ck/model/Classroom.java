package citd.nhom99.ck.model;

import java.util.ArrayList;
import java.util.List;

public class Classroom {
    private int classId;
    private String className;
    private int GVCNId;
    private Teacher teacher;
    private List<Student> students = new ArrayList<>();

    public Classroom() {
    }

    public Classroom(String className) {
        this.className = className;
    }

    public Classroom(String className, int GVCNId) {
        this.className = className;
        this.GVCNId = GVCNId;
    }

    public Classroom(String className, Teacher teacher) {
        this.className = className;
        this.teacher = teacher;
    }

    public Classroom(int classId, String className, int GVCNId) {
        this.classId = classId;
        this.className = className;
        this.GVCNId = GVCNId;
    }

    public Classroom(int classId, String className, Teacher teacher) {
        this.classId = classId;
        this.className = className;
        this.teacher = teacher;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
    }

    public String getClassName() {
        return className;
    }

    public int getTeacherId() {
        return GVCNId;
    }

    public void setTeacherId(int GVCNId) {
        this.GVCNId = GVCNId;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(String studentCode) {

    }

    public void removeStudent(String studentCode) {

    }

    @Override
    public String toString() {
        return "Lớp" + getClassId() + " " + getClassName() + ": " + "GVCN: " + getTeacherId();
    }
}