package repository;

import entity.Visit;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class VisitRepository {

    public void save(Visit visit) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(visit);
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

    public List<Visit> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            List<Visit> visits = session.createQuery(
                            "SELECT DISTINCT v FROM Visit v JOIN FETCH v.visitor ORDER BY v.id",
                            Visit.class)
                    .getResultList();
            visits.sort((left, right) -> left.getVisitDate().compareTo(right.getVisitDate()));
            return visits;
        } finally {
            session.close();
        }
    }

    public List<Visit> findByVisitorId(Long visitorId) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "FROM Visit v WHERE v.visitor.id = :visitorId ORDER BY v.visitDate", Visit.class)
                    .setParameter("visitorId", visitorId)
                    .getResultList();
        } finally {
            session.close();
        }
    }
}
