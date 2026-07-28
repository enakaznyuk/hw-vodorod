package repository;

import entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class EmployeeRepository {

    public void save(Employee employee) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(employee);
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
                            "SELECT COUNT(e) FROM Employee e " +
                                    "WHERE e.firstName = :firstName AND e.lastName = :lastName AND e.birthYear = :birthYear",
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

    public List<Employee> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Employee e ORDER BY e.id", Employee.class)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    public Optional<Employee> findById(Long id) {
        Session session = HibernateUtil.openSession();
        try {
            return Optional.ofNullable(session.get(Employee.class, id));
        } finally {
            session.close();
        }
    }
}
