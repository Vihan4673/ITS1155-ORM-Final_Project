package lk.ijse.dao.custom.impl;

import lk.ijse.config.FactoryConfiguration;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void saveStudent(Student student) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            session.save(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new RuntimeException("Failed to save student: " + e.getMessage());
        }
    }

    @Override
    public void deleteStudent(String studentId) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            if (student != null) {
                // Delete related entities first (cascade optional)
                session.createQuery("DELETE FROM Lesson l WHERE l.student.studentId = :id")
                        .setParameter("id", studentId)
                        .executeUpdate();
                session.createQuery("DELETE FROM Payment p WHERE p.student.studentId = :id")
                        .setParameter("id", studentId)
                        .executeUpdate();

                session.delete(student);
            } else {
                throw new RuntimeException("Student not found: " + studentId);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new RuntimeException("Failed to delete student: " + e.getMessage());
        }
    }

    @Override
    public void updateStudent(Student student) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();

            // Get existing student
            Student existing = session.get(Student.class, student.getStudentId());
            if (existing == null) {
                throw new RuntimeException("Student does not exist: " + student.getStudentId());
            }

            // Update fields manually to avoid overwriting nulls
            existing.setName(student.getName());
            existing.setAddress(student.getAddress());
            existing.setTel(student.getTel());
            existing.setRegistrationDate(student.getRegistrationDate());

            session.update(existing); // safe update
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new RuntimeException("Failed to update student: " + e.getMessage());
        }
    }

    @Override
    public List<Student> getAllStudent() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve students: " + e.getMessage());
        }
    }

    @Override
    public Student getStudent(String studentId) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.get(Student.class, studentId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve student: " + e.getMessage());
        }
    }

    @Override
    public Long getStudentCount() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            String hql = "SELECT COUNT(s) FROM Student s";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to count students: " + e.getMessage());
        }
    }

    @Override
    public Student findById(String id) {
        return getStudent(id);
    }

    @Override
    public List<Student> findAll() {
        return getAllStudent();
    }

    @Override
    public String generateNewId() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            String hql = "SELECT s.studentId FROM Student s ORDER BY s.studentId DESC";
            Query<String> query = session.createQuery(hql, String.class);
            query.setMaxResults(1);
            String lastId = query.uniqueResult();

            if (lastId != null) {
                int newId = Integer.parseInt(lastId.replace("S", "")) + 1;
                return String.format("S%03d", newId);
            } else {
                return "S001";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate new student ID: " + e.getMessage());
        }
    }
}
