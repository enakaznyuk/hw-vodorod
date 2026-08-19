package repository;

import entity.ProvidedService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

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

    public Optional<ProvidedService> findByServiceName(String serviceName) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "FROM ProvidedService ps WHERE ps.serviceName = :serviceName", ProvidedService.class)
                    .setParameter("serviceName", serviceName)
                    .uniqueResultOptional();
        } finally {
            session.close();
        }
    }

    public Optional<ProvidedService> findWithLowestPriceByCriteria() {
        Session session = HibernateUtil.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ProvidedService> criteriaQuery = criteriaBuilder.createQuery(ProvidedService.class);
            Root<ProvidedService> root = criteriaQuery.from(ProvidedService.class);

            criteriaQuery.select(root)
                    .orderBy(
                            criteriaBuilder.asc(root.get("price")),
                            criteriaBuilder.asc(root.get("id"))
                    );

            return session.createQuery(criteriaQuery)
                    .setMaxResults(1)
                    .uniqueResultOptional();
        } finally {
            session.close();
        }
    }
}
