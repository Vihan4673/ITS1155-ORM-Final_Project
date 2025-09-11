package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.DashboardBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.CourseDAO;
import lk.ijse.dao.custom.InstructorDAO;
import lk.ijse.dao.custom.QueryDAO;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class DashboardBOImpl implements DashboardBO {

    CourseDAO culinaryProgramDAO = (CourseDAO) DAOFactory.getDAO(DAOFactory.DAOType.PROGRAM);
    StudentDAO studentDAO = (StudentDAO) DAOFactory.getDAO(DAOFactory.DAOType.STUDENT);
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getDAO(DAOFactory.DAOType.QUERY);
    private final InstructorDAO instructorDAO =
            (InstructorDAO) DAOFactory.getDAO(DAOFactory.DAOType.INSTRUCTOR);

    @Override
    public Long getCulinaryProgramCount(){
        return culinaryProgramDAO.getCulinaryProgramCount();
    }

    @Override
    public Long getStudentCount(){
        return studentDAO.getStudentCount();
    }

    @Override
    public int getInstructorCount() {
        assert instructorDAO != null;
        return instructorDAO.count();
    }

    @Override
    public List<StudentDTO> getAllProgramStudents(){
        List<StudentDTO> studentDTOS = new ArrayList<>();
        List<Student> allProgramsStudent = queryDAO.getAllProgramsStudent();

        for (Student student : allProgramsStudent) {
            studentDTOS.add(new StudentDTO(student.getStudentId(),student.getName(),student.getAddress(),student.getTel(),student.getRegistrationDate()));
        }
        return studentDTOS;
    }
}
