package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.CourseBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.CourseDAO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseBOImpl implements CourseBO {

    private final CourseDAO courseDAO = (CourseDAO) DAOFactory.getDAO(DAOFactory.DAOType.PROGRAM);

    @Override
    public void saveCourse(CourseDTO dto) {
        if (dto == null) throw new IllegalArgumentException("CourseDTO cannot be null");

        int durationInMonths = convertToMonths(dto.getDuration(), dto.getDurationUnit());
        Course entity = new Course(dto.getProgramId(), dto.getProgramName(), durationInMonths, dto.getFee());
        courseDAO.saveCourse(entity);
    }

    @Override
    public void deleteCourse(CourseDTO dto) {
        if (dto == null) throw new IllegalArgumentException("CourseDTO cannot be null");
        Course entity = courseDAO.findById(dto.getProgramId());
        if (entity != null) {
            courseDAO.deleteCourse(entity);
        }
    }

    @Override
    public void updateCourse(CourseDTO dto) {
        if (dto == null) throw new IllegalArgumentException("CourseDTO cannot be null");

        int durationInMonths = convertToMonths(dto.getDuration(), dto.getDurationUnit());
        Course entity = new Course(dto.getProgramId(), dto.getProgramName(), durationInMonths, dto.getFee());
        courseDAO.updateCourse(entity);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        List<Course> entities = courseDAO.getAllCourses();
        List<CourseDTO> dtoList = new ArrayList<>();
        if (entities != null) {
            for (Course c : entities) {
                String unit = (c.getDuration() > 0) ? "Months" : "Weeks";
                int value = (c.getDuration() > 0) ? c.getDuration() : 0;

                dtoList.add(new CourseDTO(
                        c.getProgramId(),
                        c.getProgramName(),
                        value,
                        c.getFee(),
                        unit
                ));
            }
        }
        return dtoList;
    }

    @Override
    public CourseDTO getCourse(String programId) {
        if (programId == null || programId.isEmpty()) return null;
        Course entity = courseDAO.getCourse(programId);
        if (entity == null) return null;

        String unit = (entity.getDuration() > 0) ? "Months" : "Weeks";
        int value = (entity.getDuration() > 0) ? entity.getDuration() : 0;

        return new CourseDTO(
                entity.getProgramId(),
                entity.getProgramName(),
                value,
                entity.getFee(),
                unit
        );
    }

    @Override
    public String generateCourseId() {
        return courseDAO.generateCourseId();
    }

    // Converts user input to months for storage
    private int convertToMonths(int value, String unit) {
        if (unit == null) return value; // default: months
        switch (unit.toLowerCase()) {
            case "weeks":
                return (int) Math.ceil(value / 4.0); // 4 weeks ≈ 1 month
            case "months":
            default:
                return value;
        }
    }
}
