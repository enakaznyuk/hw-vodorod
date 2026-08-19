package repository;

import entity.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.time.LocalDate;
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
        return findAllByCriteria();
    }

    public List<Employee> findAllByCriteria() {
        Session session = HibernateUtil.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);

            criteriaQuery.select(root).orderBy(criteriaBuilder.asc(root.get("id")));

            return session.createQuery(criteriaQuery).getResultList();
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

    public Optional<Employee> findHighestPaid() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "FROM Employee e ORDER BY e.monthlySalary DESC, e.id ASC",
                            Employee.class)
                    .setMaxResults(1)
                    .uniqueResultOptional();
        } finally {
            session.close();
        }
    }

    public Optional<Employee> findLowestPaid() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "FROM Employee e ORDER BY e.monthlySalary ASC, e.id ASC",
                            Employee.class)
                    .setMaxResults(1)
                    .uniqueResultOptional();
        } finally {
            session.close();
        }
    }

    public List<Employee> findActiveInPeriod(LocalDate periodStart, LocalDate periodEnd) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "FROM Employee e WHERE e.hireDate <= :periodEnd " +
                                    "AND (e.fireDate IS NULL OR e.fireDate >= :periodStart) " +
                                    "ORDER BY e.id",
                            Employee.class)
                    .setParameter("periodStart", periodStart)
                    .setParameter("periodEnd", periodEnd)
                    .getResultList();
        } finally {
            session.close();
        }
    }
}
