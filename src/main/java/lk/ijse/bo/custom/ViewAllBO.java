package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.courseDTO;
import java.util.List;

public interface ViewAllBO extends SuperBO {

    List<courseDTO> getAllCulinaryProgram();
    List<Object[]> getAllEqualByProgramName(String programName);
}
