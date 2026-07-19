package repository;

import entity.PremiumClient;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class PremiumClientRepository {

    public List<PremiumClient> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "FROM PremiumClient c ORDER BY c.id", PremiumClient.class)
                    .getResultList();
        } finally {
            session.close();
        }
    }
}
