package lk.ijse.tdm;

import java.time.LocalDate;
import java.util.List;

public class LessonTm {

    private String lessonId;
    private List<String> studentIds;
    private String courseId;
    private String instructorId;
    private LocalDate lessonDate;
    private String lessonTime;
    private int duration;

    public LessonTm() {
    }

    public LessonTm(String lessonId, List<String> studentIds, String courseId, String instructorId,
                    LocalDate lessonDate, String lessonTime, int duration) {
        this.lessonId = lessonId;
        this.studentIds = studentIds;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.lessonDate = lessonDate;
        this.lessonTime = lessonTime;
        this.duration = duration;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public LocalDate getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(LocalDate lessonDate) {
        this.lessonDate = lessonDate;
    }

    public String getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(String lessonTime) {
        this.lessonTime = lessonTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
