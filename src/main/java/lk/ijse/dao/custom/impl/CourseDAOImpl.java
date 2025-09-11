package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.CourseDAO;
import lk.ijse.db.FactoryConfiguration;
import lk.ijse.dto.courseDTO;
import lk.ijse.entity.course;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CourseDAOImpl implements CourseDAO {

    @Override
    public void saveCulinaryProgram(course culinaryProgram) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.save(culinaryProgram);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteCulinaryProgram(course culinaryProgram) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(culinaryProgram);
        transaction.commit();
        session.close();
    }

    @Override
    public void updateCulinaryProgram(course culinaryProgram) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.update(culinaryProgram);
        transaction.commit();
        session.close();
    }

    @Override
    public List<course> getAllCulinaryProgram() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        List<course> list = session.createQuery("from course", course.class).list();
        transaction.commit();
        session.close();
        return list;
    }

    @Override
    public courseDTO getProgramsCheckName(String programName) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        Query<course> query = session.createQuery("FROM course c WHERE c.programName = :programName", course.class);
        query.setParameter("programName", programName);
        course result = query.uniqueResult();

        transaction.commit();
        session.close();

        if (result != null) {

            return new courseDTO(
                    result.getProgramId(),
                    result.getProgramName(),
                    result.getDuration(),
                    result.getFee()
            );
        } else {
            return null;
        }
    }

    @Override
    public course getCulinaryProgram(String programId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        course program = session.get(course.class, programId);
        transaction.commit();
        session.close();
        return program;
    }

    @Override
    public Long getCulinaryProgramCount() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Long count = session.createQuery("SELECT COUNT(c) FROM course c", Long.class).uniqueResult();
        transaction.commit();
        session.close();
        return count;
    }

    @Override
    public String generateProgramId() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        String lastId = (String) session.createQuery("SELECT c.programId FROM course c ORDER BY c.programId DESC")
                .setMaxResults(1)
                .uniqueResult();

        transaction.commit();
        session.close();

        if (lastId != null) {
            int newId = Integer.parseInt(lastId.replace("P", "")) + 1;
            return String.format("P%03d", newId);
        } else {
            return "P001";
        }
    }

    @Override
    public course findById(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        course c = session.get(course.class, id);
        session.close();
        return c;
    }

    @Override
    public List<course> findAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<course> courses = session.createQuery("FROM course", course.class).list();
        session.close();
        return courses;
    }
}
