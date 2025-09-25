package lk.ijse.dao.custom.impl;

import lk.ijse.config.FactoryConfiguration;
import lk.ijse.dao.custom.CourseDAO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.entity.Course;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CourseDAOImpl implements CourseDAO {

    @Override
    public void saveCourse(Course course) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            session.save(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public void deleteCourse(Course course) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            Course managedCourse = session.get(Course.class, course.getProgramId());
            if (managedCourse != null) {
                session.delete(managedCourse);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public void updateCourse(Course course) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            session.merge(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public List<Course> getAllCourses() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.createQuery("FROM Course", Course.class).list();
        }
    }

    @Override
    public CourseDTO getCourseByName(String programName) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<Course> query = session.createQuery(
                    "FROM Course c WHERE c.programName = :programName", Course.class
            );
            query.setParameter("programName", programName);
            Course result = query.uniqueResult();

            if (result != null) {
                return new CourseDTO(
                        result.getProgramId(),
                        result.getProgramName(),
                        result.getDuration(),
                        result.getFee(),
                        "Months"
                );
            } else {
                return null;
            }
        }
    }


    @Override
    public Course getCourse(String programId) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.get(Course.class, programId);
        }
    }

    @Override
    public Long getCourseCount() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.createQuery("SELECT COUNT(c) FROM Course c", Long.class).uniqueResult();
        }
    }

    @Override
    public String generateCourseId() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            String lastId = (String) session.createQuery(
                            "SELECT c.programId FROM Course c WHERE c.programId LIKE 'C%' ORDER BY c.programId DESC")
                    .setMaxResults(1)
                    .uniqueResult();

            if (lastId != null) {
                int newId = Integer.parseInt(lastId.substring(1)) + 1;
                return "C" + String.format("%04d", newId); // C1001, C1002, etc.
            } else {
                return "C1001";
            }
        }
    }

    @Override
    public Course findById(String id) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.get(Course.class, id);
        }
    }

    @Override
    public List<Course> findAll() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.createQuery("FROM Course", Course.class).list();
        }
    }
}
