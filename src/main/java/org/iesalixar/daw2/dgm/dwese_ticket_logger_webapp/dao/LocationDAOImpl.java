package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Location;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LocationDAOImpl implements LocationDAO {

    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger = LoggerFactory.getLogger(LocationDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;

    // Inyección de JdbcTemplate
    public LocationDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Lista todas las ubicaciones de la base de datos.
     * @return Lista de ubicaciones
     */
    @Override
    public List<Location> listAllLocations() {
        logger.info("Listing all locations from the database.");
        String sql = "SELECT l.*, p.code AS province_code, p.name AS province_name " +
                "FROM locations l JOIN provinces p ON l.id_province = p.id";
        List<Location> locations = jdbcTemplate.query(sql, new LocationRowMapper());
        logger.info("Retrieved {} locations from the database.", locations.size());
        return locations;
    }

    /**
     * Inserta una nueva ubicación en la base de datos.
     * @param location Ubicación a insertar
     */
    @Override
    public void insertLocation(Location location) {
        logger.info("Inserting location with address: {} and city: {}", location.getAddress(), location.getCity());
        String sql = "INSERT INTO locations (address, city, id_province) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, location.getAddress(), location.getCity(), location.getProvince().getId());
        logger.info("Inserted location. Rows affected: {}", rowsAffected);
    }

    /**
     * Actualiza una ubicación existente en la base de datos.
     * @param location Ubicación a actualizar
     */
    @Override
    public void updateLocation(Location location) {
        logger.info("Updating location with id: {}", location.getId());
        String sql = "UPDATE locations SET address = ?, city = ?, id_province = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, location.getAddress(), location.getCity(), location.getProvince().getId(), location.getId());
        logger.info("Updated location. Rows affected: {}", rowsAffected);
    }

    /**
     * Elimina una ubicación de la base de datos.
     * @param id ID de la ubicación a eliminar
     */
    @Override
    public void deleteLocation(int id) {
        logger.info("Deleting location with id: {}", id);
        String sql = "DELETE FROM locations WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        logger.info("Deleted location. Rows affected: {}", rowsAffected);
    }

    /**
     * Obtiene una ubicación por su ID.
     * @param id ID de la ubicación
     * @return Ubicación correspondiente al ID
     */
    @Override
    public Location getLocationById(int id) {
        logger.info("Retrieving location by id: {}", id);
        String sql = "SELECT l.*, p.code AS province_code, p.name AS province_name " +
                "FROM locations l JOIN provinces p ON l.id_province = p.id WHERE l.id = ?";
        try {
            Location location = jdbcTemplate.queryForObject(sql, new LocationRowMapper(), id);
            logger.info("Location retrieved: {} - {}", location.getAddress(), location.getCity());
            return location;
        } catch (Exception e) {
            logger.warn("No location found with id: {}", id);
            return null;
        }
    }

    /**
     * Clase interna que implementa RowMapper para mapear los resultados de la consulta SQL a la entidad Location.
     */
    private static class LocationRowMapper implements RowMapper<Location> {
        @Override
        public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
            Location location = new Location();
            location.setId(rs.getInt("id"));
            location.setAddress(rs.getString("address"));
            location.setCity(rs.getString("city"));

            Province province = new Province();
            province.setId(rs.getInt("id_province"));
            province.setCode(rs.getString("province_code"));
            province.setName(rs.getString("province_name"));
            location.setProvince(province);

            return location;
        }
    }
}
