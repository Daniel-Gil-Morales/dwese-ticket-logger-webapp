package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.SuperMarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class SuperMarketDAOImpl implements SuperMarketDAO {

    private static final Logger logger = LoggerFactory.getLogger(SuperMarketDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SuperMarket> listAllSupermarkets() {
        logger.info("Listing all supermarkets from the database.");
        String query = "SELECT sm FROM SuperMarket sm"; // HQL para obtener todos los supermercados
        List<SuperMarket> supermarkets = entityManager.createQuery(query, SuperMarket.class).getResultList();
        logger.info("Retrieved {} supermarkets from the database.", supermarkets.size());
        return supermarkets;
    }

    @Override
    public void insertSuperMarket(SuperMarket supermarket) {
        logger.info("Inserting a new supermarket with ID: {} and name: {}", supermarket.getId(), supermarket.getName());
        entityManager.persist(supermarket); // Usando persist para insertar
        logger.info("Supermarket inserted with ID: {}", supermarket.getId());
    }

    @Override
    public void updateSuperMarket(SuperMarket supermarket) {
        logger.info("Updating supermarket with ID: {} and name: {}", supermarket.getId(), supermarket.getName());
        entityManager.merge(supermarket); // Usando merge para actualizar
        logger.info("Updated supermarket with ID: {}", supermarket.getId());
    }

    @Override
    public void deleteSuperMarket(int id) {
        logger.info("Deleting supermarket with id: {}", id);
        SuperMarket supermarket = entityManager.find(SuperMarket.class, id);
        if (supermarket != null) {
            entityManager.remove(supermarket); // Usando remove para eliminar
            logger.info("Deleted supermarket with id: {}", id);
        } else {
            logger.warn("No supermarket found with id: {}", id);
        }
    }

    @Override
    public SuperMarket getSuperMarketById(int id) {
        logger.info("Retrieving supermarket by id: {}", id);
        SuperMarket supermarket = entityManager.find(SuperMarket.class, id);
        if (supermarket != null) {
            logger.info("Supermarket retrieved: {}", supermarket.getName());
        } else {
            logger.warn("No supermarket found with id: {}", id);
        }
        return supermarket;
    }
}
