package lk.ijse.dao.custom.impl;


import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.db.FactoryConfiguration;
import lk.ijse.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void saveStudent(Student student) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.save(student);

        transaction.commit();
        session.close();
    }

    @Override
    public void deleteStudent(Student student) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String studentId = student.getStudentId();

            // Delete lessons linked to student
            session.createQuery("DELETE FROM Lesson l WHERE l.student.studentId = :id")
                    .setParameter("id", studentId)
                    .executeUpdate();

            // Delete payments linked to student
            session.createQuery("DELETE FROM Payment p WHERE p.student.studentId = :id")
                    .setParameter("id", studentId)
                    .executeUpdate();

            // Now delete student
            session.delete(student);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateStudent(Student student) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.merge(student);  // <-- merge instead of update
        tx.commit();
        session.close();
    }


    @Override
    public List<Student> getAllStudent() {
        List<Student> students;
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        students = session.createQuery("from Student", Student.class).list();

        transaction.commit();
        session.close();

        return students;
    }

    @Override
    public Student getStudent(String studentId){
        Student student = null;
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        student = session.get(Student.class, studentId);

        transaction.commit();
        session.close();

        return student;
    }

    @Override
    public Long getStudentCount(){
        Long count = 0L;

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        String hql = "SELECT COUNT(s) FROM Student s";
        Query<Long> query = session.createQuery(hql, Long.class);

        count = query.uniqueResult();

        transaction.commit();
        session.close();

        return count;
    }

    @Override
    public Student findById(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Student student = session.get(Student.class, id);
        session.close();
        return student;
    }

    @Override
    public List<Student> findAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Student> students = session.createQuery("FROM Student", Student.class).list();
        session.close();
        return students;
    }

    @Override
    public String generateNewId() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();


        String hql = "SELECT s.studentId FROM Student s ORDER BY s.studentId DESC";
        Query<String> query = session.createQuery(hql, String.class);
        query.setMaxResults(1);

        String lastId = query.uniqueResult();

        transaction.commit();
        session.close();

        if (lastId != null) {
            int newId = Integer.parseInt(lastId.replace("S", "")) + 1;
            return String.format("S%03d", newId); // S001, S002 ...
        } else {
            return "S001";
        }
    }

}

