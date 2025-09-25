package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.config.FactoryConfiguration;
import lk.ijse.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean save(Payment payment) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            session.persist(payment);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Payment payment) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            session.merge(payment);
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
            Query<?> query = session.createQuery("DELETE FROM Payment p WHERE p.paymentId = :id");
            query.setParameter("id", id);
            int result = query.executeUpdate();
            transaction.commit();
            return result > 0;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Payment findById(String id) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.get(Payment.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Payment> findAll() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.createQuery("FROM Payment", Payment.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String generatePaymentId() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<String> query = session.createQuery(
                    "SELECT p.paymentId FROM Payment p ORDER BY p.paymentId DESC", String.class);
            query.setMaxResults(1);
            String lastId = query.uniqueResult();

            if (lastId == null) {
                return "PAY001";
            } else {

                String numericPart = lastId.substring(3);
                int num = Integer.parseInt(numericPart) + 1;
                return String.format("PAY%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "PAY001";
        }
    }
    @Override
    public Double getMonthlyIncome(int month, int year) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            String hql = "SELECT SUM(p.amount) FROM Payment p " +
                    "WHERE MONTH(p.paymentDate) = :month AND YEAR(p.paymentDate) = :year";
            Query<Double> query = session.createQuery(hql, Double.class);
            query.setParameter("month", month);
            query.setParameter("year", year);
            Double result = query.uniqueResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }


}
