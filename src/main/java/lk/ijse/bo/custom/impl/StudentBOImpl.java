package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentBOImpl implements StudentBO {

    StudentDAO studentDAO = (StudentDAO) DAOFactory.getDAO(DAOFactory.DAOType.STUDENT);

    @Override
    public void saveStudent(StudentDTO dto) {
        Student student = new Student(
                dto.getStudentId(),
                dto.getName(),
                dto.getAddress(),
                dto.getTel(),
                dto.getRegistrationDate()
        );
        studentDAO.saveStudent(student);
    }

    @Override
    public void updateStudent(StudentDTO dto) {
        Student student = new Student(
                dto.getStudentId(),
                dto.getName(),
                dto.getAddress(),
                dto.getTel(),
                dto.getRegistrationDate()
        );
        studentDAO.updateStudent(student);
    }

    @Override
    public void deleteStudent(StudentDTO dto) {
        Student student = new Student(
                dto.getStudentId(),
                dto.getName(),
                dto.getAddress(),
                dto.getTel(),
                dto.getRegistrationDate()
        );
        studentDAO.deleteStudent(student);
    }

    @Override
    public StudentDTO getStudent(String studentId) {
        Student student = studentDAO.getStudent(studentId);
        if (student == null) return null;

        return new StudentDTO(
                student.getStudentId(),
                student.getName(),
                student.getAddress(),
                student.getTel(),
                student.getRegistrationDate()
        );
    }

    @Override
    public List<StudentDTO> getAllStudent() {
        List<Student> all = studentDAO.getAllStudent();
        List<StudentDTO> dtos = new ArrayList<>();
        for (Student s : all) {
            dtos.add(new StudentDTO(
                    s.getStudentId(),
                    s.getName(),
                    s.getAddress(),
                    s.getTel(),
                    s.getRegistrationDate()
            ));
        }
        return dtos;
    }

    @Override
    public String generateNewId() {
        return studentDAO.generateNewId();
    }
}
