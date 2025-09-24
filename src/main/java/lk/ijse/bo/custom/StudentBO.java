package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.StudentDTO;

import java.util.List;

public interface StudentBO extends SuperBO {

    // Save a new student along with enrolled courses
    void saveStudent(StudentDTO dto) throws Exception;

    // Update existing student info + enrolled courses
    void updateStudent(StudentDTO dto) throws Exception;

    // Delete student by ID
    void deleteStudent(String studentId) throws Exception;

    // Get single student by ID
    StudentDTO getStudent(String studentId) throws Exception;

    // Get all students
    List<StudentDTO> getAllStudent() throws Exception;

    // Auto-generate new student ID
    String generateNewId() throws Exception;
}
