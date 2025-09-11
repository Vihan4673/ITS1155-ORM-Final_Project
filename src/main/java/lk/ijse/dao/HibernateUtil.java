package lk.ijse.dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("student_pu");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void shutdown() {
        if (emf.isOpen()) emf.close();
    }
}
