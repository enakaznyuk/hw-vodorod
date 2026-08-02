package repository;

import entity.ClientStatus;
import entity.Visitor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class VisitorRepository {

    public void save(Visitor visitor) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(visitor);
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

    public boolean existsByFullNameAndBirthYear(String firstName, String lastName, int birthYear) {
        Session session = HibernateUtil.openSession();
        try {
            Long count = session.createQuery(
                            "SELECT COUNT(v) FROM Visitor v " +
                                    "WHERE v.firstName = :firstName AND v.lastName = :lastName AND v.birthYear = :birthYear",
                            Long.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .setParameter("birthYear", birthYear)
                    .getSingleResult();
            return count > 0;
        } finally {
            session.close();
        }
    }

    public List<Visitor> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Visitor v ORDER BY v.id", Visitor.class)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    public Optional<Visitor> findById(Long id) {
        Session session = HibernateUtil.openSession();
        try {
            return Optional.ofNullable(session.get(Visitor.class, id));
        } finally {
            session.close();
        }
    }

    public Optional<Visitor> findByFullNameAndBirthYear(String firstName, String lastName, int birthYear) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "FROM Visitor v WHERE v.firstName = :firstName " +
                                    "AND v.lastName = :lastName AND v.birthYear = :birthYear",
                            Visitor.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .setParameter("birthYear", birthYear)
                    .uniqueResultOptional();
        } finally {
            session.close();
        }
    }

    public void deleteById(Long id) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Visitor visitor = session.get(Visitor.class, id);
            if (visitor != null) {
                session.remove(visitor);
            }
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

    public void updateStatus(Long id, ClientStatus status) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Visitor visitor = session.get(Visitor.class, id);
            if (visitor != null) {
                visitor.setStatus(status);
            }
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
}
