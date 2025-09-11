package lk.ijse.db;

import lk.ijse.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;

    private FactoryConfiguration() {
        try {
            // Load hibernate.properties from resources folder
            Properties properties = new Properties();
            InputStream input = getClass().getResourceAsStream("/hibernate.properties");
            if (input == null) {
                throw new RuntimeException("hibernate.properties file not found in resources!");
            }
            properties.load(input);

            // Hibernate Configuration
            Configuration configuration = new Configuration();
            configuration.setProperties(properties)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(course.class)
                    .addAnnotatedClass(Instructor.class)
                    .addAnnotatedClass(Lesson.class)
                    .addAnnotatedClass(Payment.class)
                    .addAnnotatedClass(Student.class);

            // Build SessionFactory
            sessionFactory = configuration.buildSessionFactory();
            System.out.println("Hibernate SessionFactory created successfully, DB should be ready!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Hibernate configuration failed", e);
        }
    }

    public static FactoryConfiguration getInstance() {
        if (factoryConfiguration == null) {
            factoryConfiguration = new FactoryConfiguration();
        }
        return factoryConfiguration;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
