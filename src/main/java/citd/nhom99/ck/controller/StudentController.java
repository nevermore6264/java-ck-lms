package citd.nhom99.ck.controller;

import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.Student;
import citd.nhom99.ck.model.dao.StudentDAO;

import java.util.List;

public class StudentController {
    private final StudentDAO studentDAO = new StudentDAO();

    public StudentController() {
    }

    public void createStudent(User student) {
        studentDAO.createStudent(student);
        System.out.println("Controller: Creating student " + student.getUsername());
    }

    public List<Student> getAllStudents() {
        return this.studentDAO.getAllStudents();
    }

    public void updateStudent(User student) {
        studentDAO.updateStudent(student);
        System.out.println("Controller: Updating student info for student ");
    }

    public void deleteStudent(int id) {
        if (id <= 0) {
            System.out.println("Controller: Invalid student id");
            return;
        }
        studentDAO.deleteStudent(id);
        System.out.println("Controller: Deleting student " + id);
    }
}
