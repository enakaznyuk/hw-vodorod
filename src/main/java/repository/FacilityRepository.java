package repository;

import entity.Facility;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
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
            return session.createQuery(
                            "SELECT f FROM Facility f LEFT JOIN FETCH f.providedService WHERE f.id = :id",
                            Facility.class)
                    .setParameter("id", id)
                    .uniqueResultOptional();
        } finally {
            session.close();
        }
    }

    public Optional<Facility> findByIdentificationNumber(String identificationNumber) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "SELECT f FROM Facility f LEFT JOIN FETCH f.providedService " +
                                    "WHERE f.identificationNumber = :identificationNumber",
                            Facility.class)
                    .setParameter("identificationNumber", identificationNumber)
                    .uniqueResultOptional();
        } finally {
            session.close();
        }
    }

    public List<Facility> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            List<Facility> facilities = session.createQuery(
                            "SELECT DISTINCT f FROM Facility f LEFT JOIN FETCH f.providedService",
                            Facility.class)
                    .getResultList();
            facilities.sort((left, right) -> Long.compare(left.getId(), right.getId()));
            return facilities;
        } finally {
            session.close();
        }
    }

    public List<Facility> findGyms() {
        Session session = HibernateUtil.openSession();
        try {
            List<Facility> gyms = session.createQuery(
                            "SELECT DISTINCT f FROM Facility f LEFT JOIN FETCH f.providedService " +
                                    "WHERE lower(f.facilityName) LIKE lower(:gymName) " +
                                    "OR lower(f.identificationNumber) LIKE lower(:gymCode)",
                            Facility.class)
                    .setParameter("gymName", "%Тренажёрный зал%")
                    .setParameter("gymCode", "GYM-%")
                    .getResultList();
            gyms.sort((left, right) -> Long.compare(left.getId(), right.getId()));
            return gyms;
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

            if (template.getProvidedService() != null) {
                template.getProvidedService().getServiceName();
            }

            session.detach(template);
            template.setId(null);
            template.setIdentificationNumber(newIdentificationNumber);
            template.setAppointments(new ArrayList<>());

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

    public void deleteById(Long id) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Facility facility = session.get(Facility.class, id);
            if (facility != null) {
                session.remove(facility);
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
