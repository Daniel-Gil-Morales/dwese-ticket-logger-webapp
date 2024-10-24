package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Location;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Province;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.SuperMarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class LocationDAOImpl implements LocationDAO {

    private static final Logger logger = LoggerFactory.getLogger(LocationDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Location> listAllLocations() {
        logger.info("Listing all locations from the database.");
        String query = "SELECT l FROM Location l JOIN FETCH l.province JOIN FETCH l.supermarket";
        List<Location> locations = entityManager.createQuery(query, Location.class).getResultList();
        logger.info("Retrieved {} locations from the database.", locations.size());
        return locations;
    }

    @Override
    public void insertLocation(Location location) {
        logger.info("Inserting a new location into the database. Address: {}, City: {}",
                location.getAddress(), location.getCity());
        entityManager.persist(location); // Usando persist para insertar
        logger.info("Location inserted with ID: {}", location.getId());
    }

    @Override
    public void updateLocation(Location location) {
        logger.info("Updating location with id: {}", location.getId());
        entityManager.merge(location); // Usando merge para actualizar
        logger.info("Updated location with id: {}", location.getId());
    }

    @Override
    public void deleteLocation(int id) {
        logger.info("Deleting location with id: {}", id);
        Location location = entityManager.find(Location.class, id);
        if (location != null) {
            entityManager.remove(location); // Usando remove para eliminar
            logger.info("Deleted location with id: {}", id);
        } else {
            logger.warn("Location with id: {} not found.", id);
        }
    }

    @Override
    public Location getLocationById(int id) {
        logger.info("Retrieving location by id: {}", id);
        Location location = entityManager.find(Location.class, id);
        if (location != null) {
            logger.info("Location retrieved: {} - {}", location.getAddress(), location.getCity());
        } else {
            logger.warn("No location found with id: {}", id);
        }
        return location;
    }
}
