package lk.ijse.dao.custom.impl;

import lk.ijse.config.FactoryConfiguration;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.entity.Course;
import lk.ijse.entity.Student;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.stream.Collectors;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void saveStudent(Student student) {
        if (student == null) throw new RuntimeException("Student cannot be null");

        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();


            if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
                student.setStudentId(generateNewId());
            }


            if (student.getCourses() != null && !student.getCourses().isEmpty()) {
                List<Course> linkedCourses = student.getCourses().stream()
                        .map(c -> session.get(Course.class, c.getProgramId()))
                        .collect(Collectors.toList());
                student.getCourses().clear();
                student.getCourses().addAll(linkedCourses);
            }

            session.persist(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Failed to save student: " + e.getMessage(), e);
        }
    }


    @Override
    public Student getStudent(String studentId) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                Hibernate.initialize(student.getCourses()); // Avoid lazy loading issues
            }
            return student;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve student: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> getAllStudent() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            List<Student> students = session.createQuery("FROM Student", Student.class).list();
            students.forEach(s -> Hibernate.initialize(s.getCourses())); // Initialize courses
            return students;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve students: " + e.getMessage(), e);
        }
    }


    @Override
    public void updateStudent(Student student) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();

            Student existing = session.get(Student.class, student.getStudentId());
            if (existing == null) {
                throw new RuntimeException("Student does not exist: " + student.getStudentId());
            }


            existing.setName(student.getName());
            existing.setAddress(student.getAddress());
            existing.setTel(student.getTel());
            existing.setEmail(student.getEmail());
            existing.setRegistrationDate(student.getRegistrationDate());


            existing.getCourses().clear();
            if (student.getCourses() != null) {
                existing.getCourses().addAll(student.getCourses());
            }

            session.merge(existing);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Failed to update student: " + e.getMessage(), e);
        }
    }


    @Override
    public void deleteStudent(String studentId) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            if (student != null) {
                student.getCourses().clear();
                session.delete(student);
            } else {
                throw new RuntimeException("Student not found: " + studentId);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Failed to delete student: " + e.getMessage(), e);
        }
    }

    @Override
    public Long getStudentCount() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(s) FROM Student s", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Failed to count students: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateNewId() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<String> query = session.createQuery(
                    "SELECT s.studentId FROM Student s ORDER BY s.studentId DESC", String.class);
            query.setMaxResults(1);
            String lastId = query.uniqueResult();

            if (lastId != null && !lastId.isEmpty()) {
                int numeric = Integer.parseInt(lastId.replaceAll("\\D+", "")) + 1;
                return String.format("S%03d", numeric);
            } else {
                return "S001";
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate new student ID: " + e.getMessage(), e);
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
}
