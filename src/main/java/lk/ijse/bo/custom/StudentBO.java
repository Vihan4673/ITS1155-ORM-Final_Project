package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.StudentDTO;

import java.util.List;

public interface StudentBO extends SuperBO {

    void saveStudent(StudentDTO dto) throws Exception;

    void updateStudent(StudentDTO dto) throws Exception;

    boolean deleteStudent(String studentId) throws Exception;

    StudentDTO getStudent(String studentId) throws Exception;

    List<StudentDTO> getAllStudent() throws Exception;


    String generateNewId() throws Exception;
}
