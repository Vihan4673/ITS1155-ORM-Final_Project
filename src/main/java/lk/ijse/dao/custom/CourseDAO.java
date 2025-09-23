package lk.ijse.dao.custom;

import lk.ijse.dao.SuperDAO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.entity.Course;

import java.util.List;

public interface CourseDAO extends SuperDAO {

    // CRUD operations
    void saveCourse(Course course);
    void updateCourse(Course course);
    void deleteCourse(Course course);

    // Read operations
    List<Course> getAllCourses();
    Course getCourse(String programId);
    Course findById(String programId);
    List<Course> findAll();

    // Utility methods
    CourseDTO getCourseByName(String programName); // Check course by name
    Long getCourseCount(); // Total number of courses
    String generateCourseId(); // Auto-generate new course ID
}
