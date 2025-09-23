package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.StudentDTO;

import java.util.List;

public interface StudentBO extends SuperBO {

    void saveStudent(StudentDTO dto);
    void updateStudent(StudentDTO dto);

    // Fix delete: only use studentId
    void deleteStudent(String studentId);

    StudentDTO getStudent(String studentId);
    List<StudentDTO> getAllStudent();
    String generateNewId();
}
