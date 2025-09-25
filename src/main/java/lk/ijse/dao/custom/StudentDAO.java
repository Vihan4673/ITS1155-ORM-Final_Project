package lk.ijse.dao.custom;

import lk.ijse.dao.SuperDAO;
import lk.ijse.entity.Student;

import java.util.List;

public interface StudentDAO extends SuperDAO {

    void saveStudent(Student student);
    void updateStudent(Student student);
    void deleteStudent(String studentId);
    Student getStudent(String studentId);
    List<Student> getAllStudent();
    Long getStudentCount();

    default Student findById(String id) {
        return getStudent(id);
    }

    default List<Student> findAll() {
        return getAllStudent();
    }


    String generateNewId();
}
