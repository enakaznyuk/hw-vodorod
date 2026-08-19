package repository;

import entity.SmallCapacityFacility;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class SmallCapacityFacilityRepository {

    public List<SmallCapacityFacility> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "FROM SmallCapacityFacility f ORDER BY f.id", SmallCapacityFacility.class)
                    .getResultList();
        } finally {
            session.close();
        }
    }
}
