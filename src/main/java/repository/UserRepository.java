package repository;

import entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class UserRepository {

    public List<User> findByAgeRange(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < minAge) {
            throw new IllegalArgumentException("Некорректный диапазон возраста");
        }

        Session session = HibernateUtil.openSession();
        try {
            int currentYear = LocalDate.now().getYear();
            int minBirthYear = currentYear - maxAge;
            int maxBirthYear = currentYear - minAge;

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            criteriaQuery.select(root)
                    .where(criteriaBuilder.between(root.get("birthYear"), minBirthYear, maxBirthYear))
                    .orderBy(criteriaBuilder.asc(root.get("id")));

            return session.createQuery(criteriaQuery).getResultList();
        } finally {
            session.close();
        }
    }
}
