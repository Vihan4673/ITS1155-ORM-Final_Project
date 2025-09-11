package lk.ijse.bo.custom;



import lk.ijse.bo.SuperBO;
import lk.ijse.dto.InstructorDTO;

import java.util.List;

public interface InstructorBO extends SuperBO {
    boolean saveInstructor(InstructorDTO dto);
    boolean updateInstructor(InstructorDTO dto);
    boolean deleteInstructor(String id);
    InstructorDTO searchInstructor(String id);
    List<InstructorDTO> getAllInstructors();
    String generateNewId();
}
