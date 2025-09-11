package lk.ijse.bo.custom;



import lk.ijse.bo.SuperBO;
import lk.ijse.dto.LessonDTO;

import java.util.List;

public interface LessonBO extends SuperBO {
    void saveLesson(LessonDTO dto);
    void updateLesson(LessonDTO dto);
    void deleteLesson(LessonDTO dto);
    LessonDTO getLesson(String lessonId);
    List<LessonDTO> getAllLesson();
    String generateNextLessonId();
}
