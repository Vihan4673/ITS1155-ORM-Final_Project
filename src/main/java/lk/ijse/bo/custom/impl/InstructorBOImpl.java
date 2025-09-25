package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.InstructorBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.InstructorDAO;
import lk.ijse.dto.InstructorDTO;
import lk.ijse.entity.Instructor;

import java.util.ArrayList;
import java.util.List;

public class InstructorBOImpl implements InstructorBO {

    private final InstructorDAO instructorDAO = (InstructorDAO) DAOFactory.getDAO(DAOFactory.DAOType.INSTRUCTOR);

    @Override
    public boolean saveInstructor(InstructorDTO dto) {
        Instructor instructor = new Instructor(
                dto.getInstructorId(),
                dto.getName(),
                dto.getSpecialization(),
                dto.getEmail(),
                dto.getPhone()
        );
        return instructorDAO.save(instructor);
    }

    @Override
    public boolean updateInstructor(InstructorDTO dto) {
        Instructor instructor = new Instructor(
                dto.getInstructorId(),
                dto.getName(),
                dto.getSpecialization(),
                dto.getEmail(),
                dto.getPhone()
        );
        return instructorDAO.update(instructor);
    }

    @Override
    public boolean deleteInstructor(String id) {
        return instructorDAO.delete(id);
    }

    @Override
    public InstructorDTO searchInstructor(String id) {
        Instructor i = instructorDAO.search(id);
        if (i != null) {
            return new InstructorDTO(
                    i.getInstructorId(),
                    i.getName(),
                    i.getSpecialization(),
                    i.getEmail(),
                    i.getPhone()
            );
        }
        return null;
    }

    @Override
    public List<InstructorDTO> getAllInstructors() {
        List<Instructor> list = instructorDAO.getAll();
        List<InstructorDTO> dtoList = new ArrayList<>();
        for (Instructor i : list) {
            dtoList.add(new InstructorDTO(
                    i.getInstructorId(),
                    i.getName(),
                    i.getSpecialization(),
                    i.getEmail(),
                    i.getPhone()
            ));
        }
        return dtoList;
    }

    @Override
    public String generateNewId() {
        return instructorDAO.generateNewId();
    }
}
