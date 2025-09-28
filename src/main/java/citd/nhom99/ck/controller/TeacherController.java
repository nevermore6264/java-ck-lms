package citd.nhom99.ck.controller;

import citd.nhom99.ck.model.Teacher;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.dao.TeacherDAO;

import java.util.List;

public class TeacherController {
    private final TeacherDAO teacherDAO = new TeacherDAO();

    public TeacherController() {
    }

    public List<Teacher> getAllTeachers() {
        return this.teacherDAO.getAllTeachers();
    }

    public Teacher getTeacherById(int id) {
        return this.teacherDAO.getTeacherById(id);
    }

    public void createTeacher(User user) {
        if (user == null) {
            System.out.println("Controller: User is null");
            return;
        }
        this.teacherDAO.createTeacher(user);
        System.out.println("Controller: Added teacher " + user.getUserId());
    }

    public void deleteTeacher(int id) {
        if (id <= 0) {
            System.out.println("Controller: Invalid teacher id");
            return;
        }
        this.teacherDAO.deleteTeacher(id);
        System.out.println("Controller: Deleted teacher " + id);
    }

    public void updateTeacher(Teacher teacher) {
        this.teacherDAO.updateTeacher(teacher);
        System.out.println("Controller: Updating teacher info for teacher " + teacher.getTeacherCode());
    }
}
