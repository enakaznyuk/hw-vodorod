package repository;

import entity.Appointment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class AppointmentRepository {

    public void save(Appointment appointment) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(appointment);
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

    public List<Appointment> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            List<Appointment> appointments = session.createQuery(
                            "SELECT DISTINCT a FROM Appointment a " +
                                    "JOIN FETCH a.client " +
                                    "JOIN FETCH a.facility " +
                                    "ORDER BY a.id",
                            Appointment.class)
                    .getResultList();
            appointments.sort((left, right) -> {
                int byDate = left.getAppointmentDate().compareTo(right.getAppointmentDate());
                if (byDate != 0) {
                    return byDate;
                }
                return left.getAppointmentTime().compareTo(right.getAppointmentTime());
            });
            return appointments;
        } finally {
            session.close();
        }
    }

    public List<Appointment> findByFacilityId(Long facilityId) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "SELECT DISTINCT a FROM Appointment a " +
                                    "JOIN FETCH a.client " +
                                    "JOIN FETCH a.facility " +
                                    "WHERE a.facility.id = :facilityId " +
                                    "ORDER BY a.id",
                            Appointment.class)
                    .setParameter("facilityId", facilityId)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    public long count() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("SELECT COUNT(a) FROM Appointment a", Long.class)
                    .getSingleResult();
        } finally {
            session.close();
        }
    }
}
