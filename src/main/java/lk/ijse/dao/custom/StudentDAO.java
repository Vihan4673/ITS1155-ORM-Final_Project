package lk.ijse.dao.custom;

import lk.ijse.dao.SuperDAO;
import lk.ijse.entity.Student;

import java.util.List;

public interface StudentDAO extends SuperDAO {

    // Create - ensure studentId is not null before saving
    void saveStudent(Student student);

    // Update existing student
    void updateStudent(Student student);

    // Delete student by ID
    void deleteStudent(String studentId);

    // Read single student
    Student getStudent(String studentId);

    // Read all students
    List<Student> getAllStudent();

    // Utility / Extra

    // Return total number of students
    Long getStudentCount();

    // Alias for getStudent
    default Student findById(String id) {
        return getStudent(id);
    }

    // Alias for getAllStudent
    default List<Student> findAll() {
        return getAllStudent();
    }

    // Auto-generate new Student ID
    String generateNewId();
}
