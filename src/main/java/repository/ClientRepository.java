package repository;

import entity.Client;
import entity.ClientStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;

public class ClientRepository {

    private final EntityManagerFactory entityManagerFactory;

    public ClientRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void save(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        try {
            entityManager.persist(client);
            transaction.commit();
        } catch (Exception exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Long count = entityManager.createQuery(
                            "SELECT COUNT(c) FROM Client c WHERE c.phoneNumber = :phoneNumber", Long.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
            return count > 0;
        } finally {
            entityManager.close();
        }
    }

    public Optional<Client> findByPhoneNumber(String phoneNumber) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Client client = entityManager.createQuery(
                            "SELECT c FROM Client c WHERE c.phoneNumber = :phoneNumber", Client.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
            return Optional.of(client);
        } catch (NoResultException exception) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    public List<Client> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT c FROM Client c ORDER BY c.id", Client.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Optional<Client> findById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return Optional.ofNullable(entityManager.find(Client.class, id));
        } finally {
            entityManager.close();
        }
    }

    public void deleteById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        try {
            Client client = entityManager.find(Client.class, id);
            if (client != null) {
                entityManager.remove(client);
            }
            transaction.commit();
        } catch (Exception exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    public void updateStatus(Long id, ClientStatus status) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        try {
            Client client = entityManager.find(Client.class, id);
            if (client != null) {
                client.setStatus(status);
            }
            transaction.commit();
        } catch (Exception exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }
}
