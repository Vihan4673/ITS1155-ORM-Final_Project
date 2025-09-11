package lk.ijse.dao.custom;

import lk.ijse.dao.SuperDAO;
import lk.ijse.dto.courseDTO;
import lk.ijse.entity.course;

import java.util.List;

public interface CourseDAO extends SuperDAO {

    void saveCulinaryProgram(course culinaryProgram);
    void deleteCulinaryProgram(course culinaryProgram);
    void updateCulinaryProgram(course culinaryProgram);
    List<course> getAllCulinaryProgram();
    courseDTO getProgramsCheckName(String programName);
    course getCulinaryProgram(String programId);
    Long getCulinaryProgramCount();

    String generateProgramId(); // new
    course findById(String id);
    List<course> findAll();
}
