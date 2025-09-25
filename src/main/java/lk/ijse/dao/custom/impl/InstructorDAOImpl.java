package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.InstructorDAO;
import lk.ijse.config.FactoryConfiguration;
import lk.ijse.entity.Instructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class InstructorDAOImpl implements InstructorDAO {

    @Override
    public boolean save(Instructor instructor) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            session.persist(instructor);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Instructor findById(String id) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.get(Instructor.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(Instructor instructor) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            session.merge(instructor);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            Instructor instructor = session.get(Instructor.class, id);
            if (instructor != null) session.remove(instructor);
            transaction.commit();
            return instructor != null;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Instructor search(String id) {
        return findById(id);
    }

    @Override
    public List<Instructor> getAll() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.createQuery("FROM Instructor ORDER BY instructorId DESC", Instructor.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public String generateNewId() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<String> query = session.createQuery("SELECT i.instructorId FROM Instructor i ORDER BY i.instructorId DESC", String.class);
            query.setMaxResults(1);
            String lastId = query.uniqueResult();

            if (lastId != null) {
                int newId = Integer.parseInt(lastId.replaceAll("[^0-9]", "")) + 1;
                return String.format("I%03d", newId);
            } else {
                return "I001";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "I001";
        }
    }

    @Override
    public int count() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Long total = (Long) session.createQuery("SELECT COUNT(i) FROM Instructor i").uniqueResult();
            return total != null ? total.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
