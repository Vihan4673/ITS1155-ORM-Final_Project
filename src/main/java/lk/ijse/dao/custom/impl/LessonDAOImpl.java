package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.LessonDAO;
import lk.ijse.db.FactoryConfiguration;
import lk.ijse.entity.Lesson;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class LessonDAOImpl implements LessonDAO {

    @Override
    public void saveLesson(Lesson lesson) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.save(lesson);
        tx.commit();
        session.close();
    }

    @Override
    public void updateLesson(Lesson lesson) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.merge(lesson);
        tx.commit();
        session.close();
    }

    @Override
    public void deleteLesson(Lesson lesson) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.delete(lesson);
        tx.commit();
        session.close();
    }

    @Override
    public List<Lesson> getAllLesson() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List<Lesson> lessons = session.createQuery("from Lesson", Lesson.class).list();
        tx.commit();
        session.close();
        return lessons;
    }

    @Override
    public Lesson getLesson(String lessonId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        Lesson lesson = session.get(Lesson.class, lessonId);
        tx.commit();
        session.close();
        return lesson;
    }

    @Override
    public String getLastLessonId() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        String hql = "SELECT l.lessonId FROM Lesson l ORDER BY l.lessonId DESC";
        Query<String> query = session.createQuery(hql, String.class);
        query.setMaxResults(1);
        String lastId = query.uniqueResult();
        tx.commit();
        session.close();
        return lastId;
    }
}
