package lk.ijse.dao.custom;

import lk.ijse.dao.SuperDAO;
import lk.ijse.entity.Lesson;

import java.util.List;

public interface LessonDAO extends SuperDAO {
    void saveLesson(Lesson lesson);
    void updateLesson(Lesson lesson);
    void deleteLesson(Lesson lesson);
    List<Lesson> getAllLesson();
    Lesson getLesson(String lessonId);
    String getLastLessonId();
}
