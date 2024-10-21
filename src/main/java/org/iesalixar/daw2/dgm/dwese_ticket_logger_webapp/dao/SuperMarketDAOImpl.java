package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.SuperMarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SuperMarketDAOImpl implements SuperMarketDAO {

    private static final Logger logger = LoggerFactory.getLogger(SuperMarketDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public SuperMarketDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SuperMarket> listAllSuperMarkets() {
        logger.info("Listing all supermarkets from the database.");
        String sql = "SELECT DISTINCT sm.*, l.address FROM supermarkets sm " +
                "JOIN locations l ON sm.id = l.supermarket_id"; // Asegúrate de que la clave foránea esté correctamente referenciada
        List<SuperMarket> superMarkets = jdbcTemplate.query(sql, new SuperMarketRowMapper());
        logger.info("Retrieved {} supermarkets from the database.", superMarkets.size());
        return superMarkets;
    }

    @Override
    public void insertSuperMarket(SuperMarket superMarket) {
        logger.info("Inserting supermarket with name: {}", superMarket.getName());
        String sql = "INSERT INTO supermarkets (name) VALUES (?)";
        int rowsAffected = jdbcTemplate.update(sql, superMarket.getName());
        logger.info("Inserted supermarket. Rows affected: {}", rowsAffected);
    }

    @Override
    public void updateSuperMarket(SuperMarket superMarket) throws SQLException {
        logger.info("Updating supermarket with id: {}", superMarket.getId());
        String sql = "UPDATE supermarkets SET name = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, superMarket.getName(), superMarket.getId());

        if (rowsAffected == 0) {
            logger.warn("No supermarket found with id: {}", superMarket.getId());
            throw new SQLException("No supermarket found with ID: " + superMarket.getId());
        }

        logger.info("Updated supermarket. Rows affected: {}", rowsAffected);
    }

    @Override
    public void deleteSuperMarket(int id) {
        logger.info("Deleting supermarket with id: {}", id);
        String sql = "DELETE FROM supermarkets WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        logger.info("Deleted supermarket. Rows affected: {}", rowsAffected);
    }

    @Override
    public SuperMarket getSuperMarketById(int id) {
        logger.info("Retrieving supermarket by id: {}", id);
        String sql = "SELECT * FROM supermarkets WHERE id = ?";
        try {
            SuperMarket superMarket = jdbcTemplate.queryForObject(sql, new SuperMarketRowMapper(), id);
            logger.info("Supermarket retrieved: {}", superMarket.getName());
            return superMarket;
        } catch (Exception e) {
            logger.warn("No supermarket found with id: {}", id);
            return null;
        }
    }

    private static class SuperMarketRowMapper implements RowMapper<SuperMarket> {
        @Override
        public SuperMarket mapRow(ResultSet rs, int rowNum) throws SQLException {
            SuperMarket superMarket = new SuperMarket();
            superMarket.setId(rs.getInt("id"));
            superMarket.setName(rs.getString("name"));
            return superMarket;
        }
    }
}