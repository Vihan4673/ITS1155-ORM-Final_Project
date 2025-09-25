package lk.ijse.dao.custom;

import lk.ijse.dao.SuperDAO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.entity.Course;

import java.util.List;

public interface CourseDAO extends SuperDAO {


    void saveCourse(Course course);
    void updateCourse(Course course);
    void deleteCourse(Course course);
    List<Course> getAllCourses();
    Course getCourse(String programId);
    Course findById(String programId);
    List<Course> findAll();
    CourseDTO getCourseByName(String programName);
    Long getCourseCount();
    String generateCourseId();
}
