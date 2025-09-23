package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.ViewAllBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.CourseDAO;
import lk.ijse.dao.custom.QueryDAO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class ViewAllBOImpl implements ViewAllBO {

    private final CourseDAO courseDAO = (CourseDAO) DAOFactory.getDAO(DAOFactory.DAOType.PROGRAM);
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDAO(DAOFactory.DAOType.QUERY);

    @Override
    public List<CourseDTO> getAllCourses() {
        List<Course> allCourses = courseDAO.getAllCourses();
        List<CourseDTO> allCoursesDTO = new ArrayList<>();

        for (Course course : allCourses) {
            allCoursesDTO.add(new CourseDTO(
                    course.getProgramId(),
                    course.getProgramName(),
                    course.getDuration(), // stored in months
                    course.getFee(),
                    null // optional payments list
            ));
        }

        return allCoursesDTO;
    }

    @Override
    public List<Object[]> getAllEqualByProgramName(String programName) {
        return queryDAO.getAllEqualByProgramName(programName);
    }
}
