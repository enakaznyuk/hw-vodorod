package util;

import entity.Employee;
import entity.Facility;
import entity.PremiumClient;
import entity.ProvidedService;
import entity.SmallCapacityFacility;
import entity.User;
import entity.Visitor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                    .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/sport_center_db")
                    .setProperty("hibernate.connection.username", "postgres")
                    .setProperty("hibernate.connection.password", "root")
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .setProperty("hibernate.show_sql", "true")
                    .setProperty("hibernate.format_sql", "true")
                    .setProperty("hibernate.current_session_context_class", "thread")
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Visitor.class)
                    .addAnnotatedClass(Employee.class)
                    .addAnnotatedClass(ProvidedService.class)
                    .addAnnotatedClass(Facility.class)
                    .addAnnotatedClass(SmallCapacityFacility.class)
                    .addAnnotatedClass(PremiumClient.class)
                    .buildSessionFactory();
        } catch (Exception exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static Session openSession() {
        return SESSION_FACTORY.openSession();
    }

    public static void shutdown() {
        if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }
}
