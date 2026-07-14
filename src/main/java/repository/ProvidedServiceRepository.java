package repository;

import entity.ProvidedService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class ProvidedServiceRepository {

    public void save(ProvidedService providedService) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(providedService);
            transaction.commit();
        } catch (Exception exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            session.close();
        }
    }

    public List<ProvidedService> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM ProvidedService ps ORDER BY ps.id", ProvidedService.class)
                    .getResultList();
        } finally {
            session.close();
        }
    }
}
