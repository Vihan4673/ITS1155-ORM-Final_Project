package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Student;
import lk.ijse.entity.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentBOImpl implements StudentBO {

    private final StudentDAO studentDAO = (StudentDAO) DAOFactory.getDAO(DAOFactory.DAOType.STUDENT);

    @Override
    public void saveStudent(StudentDTO dto) {
        if (dto == null) throw new IllegalArgumentException("StudentDTO cannot be null");


        if (dto.getStudentId() == null || dto.getStudentId().isEmpty()) {
            dto.setStudentId(generateNewId());
        }

        Student student = mapDtoToEntity(dto);
        studentDAO.saveStudent(student);
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
    public boolean deleteStudent(String studentId) {
        if (studentId == null || studentId.isEmpty())
            throw new IllegalArgumentException("Student ID cannot be null or empty");

        try {
            studentDAO.deleteStudent(studentId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete student: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public StudentDTO getStudent(String studentId) {
        if (studentId == null || studentId.isEmpty()) return null;

        Student student = studentDAO.getStudent(studentId);
        return mapEntityToDto(student);
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
            String lastId = studentDAO.generateNewId();
            if (lastId == null || lastId.isEmpty()) return "S001";

            int numeric = Integer.parseInt(lastId.replaceAll("\\D+", "")) + 1;
            return String.format("S%03d", numeric);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate new student ID", e);
        }
    }

    private Student mapDtoToEntity(StudentDTO dto) {
        if (dto == null) return null;

        String studentId = dto.getStudentId();
        if (studentId == null || studentId.isEmpty()) {
            studentId = generateNewId();
            dto.setStudentId(studentId);
        }

        Student student = new Student(
                studentId,
                dto.getName(),
                dto.getAddress(),
                dto.getTel() != null ? dto.getTel() : 0L,
                dto.getEmail(),
                dto.getRegistrationDate()
        );

        if (dto.getEnrolledCourseIds() != null && !dto.getEnrolledCourseIds().isEmpty()) {
            List<Course> courses = dto.getEnrolledCourseIds().stream()
                    .map(Course::new)
                    .collect(Collectors.toList());
            student.setCourses(courses);
        }

        return student;
    }

    private StudentDTO mapEntityToDto(Student entity) {
        if (entity == null) return null;

        StudentDTO dto = new StudentDTO(
                entity.getStudentId(),
                entity.getName(),
                entity.getAddress(),
                entity.getTel(),
                entity.getEmail(),
                entity.getRegistrationDate()
        );

        List<Course> courses = entity.getCourses();
        if (courses != null && !courses.isEmpty()) {
            List<String> courseIds = courses.stream().map(Course::getProgramId).collect(Collectors.toList());
            dto.setEnrolledCourseIds(courseIds);
        }

        return dto;
    }
}
