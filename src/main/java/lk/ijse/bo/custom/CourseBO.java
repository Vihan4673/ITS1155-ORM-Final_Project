package lk.ijse.bo.custom;



import lk.ijse.bo.SuperBO;
import lk.ijse.dto.courseDTO;

import java.util.List;

public interface CourseBO extends SuperBO {
    void saveCulinaryProgram(courseDTO culinaryProgramDTO);
    void deleteCulinaryProgram(courseDTO culinaryProgramDTO);
    void updateCulinaryProgram(courseDTO culinaryProgramDTO);
    List<courseDTO> getAllCulinaryProgram();
    courseDTO getCulinaryProgram(String programId);
    String generateProgramId(); // new
}
