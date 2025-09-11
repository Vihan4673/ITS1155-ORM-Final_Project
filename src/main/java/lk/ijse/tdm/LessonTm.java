package lk.ijse.tdm;

import java.time.LocalDate;

public class LessonTm {

    private String lessonId;
    private String studentId;
    private String courseId;
    private String instructorId;
    private LocalDate lessonDate;
    private String lessonTime;
    private int duration;

    public LessonTm() {
    }

    public LessonTm(String lessonId, String studentId, String courseId, String instructorId,
                    LocalDate lessonDate, String lessonTime, int duration) {
        this.lessonId = lessonId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.lessonDate = lessonDate;
        this.lessonTime = lessonTime;
        this.duration = duration;
    }

    // Getters and Setters
    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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
