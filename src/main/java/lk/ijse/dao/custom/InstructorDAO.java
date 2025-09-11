package lk.ijse.dao.custom;

import lk.ijse.dao.SuperDAO;
import lk.ijse.entity.Instructor;

import java.util.List;

public interface InstructorDAO extends SuperDAO {
    boolean save(Instructor instructor);
    boolean update(Instructor instructor);
    boolean delete(String id);
    Instructor search(String id);
    List<Instructor> getAll();

    String generateNewId();
    int count();
}
