package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.CourseDTO;

import java.util.List;

public interface ViewAllBO extends SuperBO {

    // Returns all courses
    List<CourseDTO> getAllCourses();

    // Returns a raw query result filtered by program name
    List<Object[]> getAllEqualByProgramName(String programName);
}
