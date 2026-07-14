package repository;

import entity.Facility;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class FacilityRepository {

    public void save(Facility facility) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(facility);
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

    public Optional<Facility> findById(Long id) {
        Session session = HibernateUtil.openSession();
        try {
            return Optional.ofNullable(session.get(Facility.class, id));
        } finally {
            session.close();
        }
    }

    public Optional<Facility> findByIdentificationNumber(String identificationNumber) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "FROM Facility f WHERE f.identificationNumber = :identificationNumber", Facility.class)
                    .setParameter("identificationNumber", identificationNumber)
                    .uniqueResultOptional();
        } finally {
            session.close();
        }
    }

    public List<Facility> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Facility f ORDER BY f.id", Facility.class)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    public Facility addFacilityByDetach(Long templateId, String newIdentificationNumber) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Facility template = session.get(Facility.class, templateId);
            if (template == null) {
                transaction.commit();
                return null;
            }

            session.detach(template);
            template.setId(null);
            template.setIdentificationNumber(newIdentificationNumber);

            session.persist(template);
            transaction.commit();
            return template;
        } catch (Exception exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            session.close();
        }
    }

    public void updateHourlyRentalCost(Long id, BigDecimal hourlyRentalCost) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Facility facility = session.get(Facility.class, id);
            if (facility != null) {
                facility.setHourlyRentalCost(hourlyRentalCost);
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
