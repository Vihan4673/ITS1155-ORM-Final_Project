package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.LessonBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.CourseDAO;
import lk.ijse.dao.custom.InstructorDAO;
import lk.ijse.dao.custom.LessonDAO;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.LessonDTO;
import lk.ijse.entity.Lesson;
import lk.ijse.entity.Student;
import lk.ijse.entity.Course;
import lk.ijse.entity.Instructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LessonBOImpl implements LessonBO {

    private final LessonDAO lessonDAO = (LessonDAO) DAOFactory.getDAO(DAOFactory.DAOType.LESSON);
    private final StudentDAO studentDAO = (StudentDAO) DAOFactory.getDAO(DAOFactory.DAOType.STUDENT);
    private final CourseDAO courseDAO = (CourseDAO) DAOFactory.getDAO(DAOFactory.DAOType.COURSE);
    private final InstructorDAO instructorDAO = (InstructorDAO) DAOFactory.getDAO(DAOFactory.DAOType.INSTRUCTOR);

    @Override
    public String generateNextLessonId() {
        String lastId = lessonDAO.getLastLessonId();
        if (lastId != null) {
            int id = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("L%03d", id);
        }
        return "L001";
    }

    @Override
    public void saveLesson(LessonDTO dto) {
        lessonDAO.saveLesson(convertToEntity(dto));
    }

    @Override
    public void updateLesson(LessonDTO dto) {
        lessonDAO.updateLesson(convertToEntity(dto));
    }

    @Override
    public void deleteLesson(LessonDTO dto) {
        Lesson lesson = lessonDAO.getLesson(dto.getLessonId());
        if (lesson != null) {
            lessonDAO.deleteLesson(lesson);
        }
    }

    @Override
    public LessonDTO getLesson(String lessonId) {
        Lesson lesson = lessonDAO.getLesson(lessonId);
        if (lesson == null) return null;
        return convertToDTO(lesson);
    }

    @Override
    public List<LessonDTO> getAllLesson() {
        List<Lesson> all = lessonDAO.getAllLesson();
        List<LessonDTO> dtos = new ArrayList<>();
        for (Lesson lesson : all) {
            dtos.add(convertToDTO(lesson));
        }
        return dtos;
    }

    private Lesson convertToEntity(LessonDTO dto) {
        Lesson lesson = new Lesson();
        lesson.setLessonId(dto.getLessonId());

        Student student = studentDAO.findById(dto.getStudentId());
        Course course = courseDAO.findById(dto.getCourseId());
        Instructor instructor = instructorDAO.findById(dto.getInstructorId());

        lesson.setStudent(student);
        lesson.setCourse(course);
        lesson.setInstructor(instructor);


        LocalDateTime dateTime = LocalDateTime.of(dto.getLessonDate(), LocalTime.parse(dto.getLessonTime()));
        lesson.setLessonDate(Timestamp.valueOf(dateTime).toLocalDateTime());

        lesson.setDuration(dto.getDuration());
        return lesson;
    }


    private LessonDTO convertToDTO(Lesson lesson) {
        Timestamp ts = Timestamp.valueOf(lesson.getLessonDate());
        LocalDateTime dateTime = ts.toLocalDateTime();
        return new LessonDTO(
                lesson.getLessonId(),
                lesson.getStudent().getStudentId(),
                lesson.getCourse().getProgramId(),
                lesson.getInstructor().getInstructorId(),
                dateTime.toLocalDate(),
                dateTime.toLocalTime().toString(),
                lesson.getDuration()
        );
    }

}
