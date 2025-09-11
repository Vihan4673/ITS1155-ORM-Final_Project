package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.InstructorDAO;
import lk.ijse.db.FactoryConfiguration;
import lk.ijse.entity.Instructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class InstructorDAOImpl implements InstructorDAO {

    @Override
    public boolean save(Instructor instructor) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(instructor);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Instructor instructor) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.merge(instructor);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Instructor instructor = session.get(Instructor.class, id);
        if (instructor != null) session.remove(instructor);
        transaction.commit();
        session.close();
        return instructor != null;
    }

    @Override
    public Instructor search(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Instructor instructor = session.get(Instructor.class, id);
        session.close();
        return instructor;
    }

    @Override
    public List<Instructor> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Instructor> list = session.createQuery("FROM Instructor ORDER BY instructorId DESC", Instructor.class).list();
        session.close();
        return list;
    }

    @Override
    public String generateNewId() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Query<String> query = session.createQuery("SELECT i.instructorId FROM Instructor i ORDER BY i.instructorId DESC", String.class);
        query.setMaxResults(1);
        String lastId = query.uniqueResult();
        session.close();

        if (lastId != null) {
            int newId = Integer.parseInt(lastId.replace("I", "")) + 1;
            return String.format("I%03d", newId); // I001, I002...
        } else {
            return "I001";
        }
    }

    @Override
    public int count() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        Long total = (Long) session.createQuery("SELECT COUNT(i) FROM Instructor i").uniqueResult();

        transaction.commit();
        session.close();
        return total.intValue();
    }

}
