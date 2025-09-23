package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentBOImpl implements StudentBO {

    private final StudentDAO studentDAO = (StudentDAO) DAOFactory.getDAO(DAOFactory.DAOType.STUDENT);

    @Override
    public void saveStudent(StudentDTO dto) {
        if (dto == null) throw new IllegalArgumentException("StudentDTO cannot be null");
        try {
            Student student = mapDtoToEntity(dto);
            studentDAO.saveStudent(student);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save student: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateStudent(StudentDTO dto) {
        if (dto == null) throw new IllegalArgumentException("StudentDTO cannot be null");
        try {
            Student student = mapDtoToEntity(dto);
            studentDAO.updateStudent(student);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update student: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteStudent(String studentId) {
        if (studentId == null || studentId.isEmpty()) throw new IllegalArgumentException("Student ID cannot be null or empty");
        try {
            studentDAO.deleteStudent(studentId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete student: " + e.getMessage(), e);
        }
    }

    @Override
    public StudentDTO getStudent(String studentId) {
        if (studentId == null || studentId.isEmpty()) return null;
        Student student = studentDAO.getStudent(studentId);
        return (student != null) ? mapEntityToDto(student) : null;
    }

    @Override
    public List<StudentDTO> getAllStudent() {
        List<Student> students = studentDAO.getAllStudent();
        List<StudentDTO> dtos = new ArrayList<>();
        for (Student s : students) {
            dtos.add(mapEntityToDto(s));
        }
        return dtos;
    }

    @Override
    public String generateNewId() {
        try {
            return studentDAO.generateNewId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate new ID: " + e.getMessage(), e);
        }
    }

    // Helper methods for mapping
    private Student mapDtoToEntity(StudentDTO dto) {
        return new Student(
                dto.getStudentId(),
                dto.getName(),
                dto.getAddress(),
                dto.getTel(),
                dto.getRegistrationDate()
        );
    }

    private StudentDTO mapEntityToDto(Student entity) {
        return new StudentDTO(
                entity.getStudentId(),
                entity.getName(),
                entity.getAddress(),
                entity.getTel(),
                entity.getRegistrationDate()
        );
    }
}
