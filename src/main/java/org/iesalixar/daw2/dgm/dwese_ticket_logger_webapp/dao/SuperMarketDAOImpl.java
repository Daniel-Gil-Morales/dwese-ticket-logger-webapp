package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.SuperMarket;
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
public class SuperMarketDAOImpl implements SuperMarketDAO {

    private static final Logger logger = LoggerFactory.getLogger(SuperMarketDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public SuperMarketDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SuperMarket> listAllSuperMarkets() {
        logger.info("Listando todos los supermercados desde la base de datos.");
        String sql = "SELECT * FROM supermarkets";
        List<SuperMarket> supermarkets = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SuperMarket.class));
        logger.info("Se han recuperado {} supermercados de la base de datos.", supermarkets.size());
        return supermarkets;

    }

    @Override
    public void insertSuperMarket(SuperMarket supermarket) {
        logger.info("Insertando un nuevo supermercado con ID: {} y nombre: {}", supermarket.getId(), supermarket.getName());
        String sql = "INSERT INTO supermarkets (id, name) VALUES (?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, supermarket.getId(), supermarket.getName());
    }

    public void updateSuperMarket(SuperMarket supermarket) {
        logger.info("Updating supermarket with new id: {} and name: {}", supermarket.getId(), supermarket.getName());
        String sql = "UPDATE supermarkets SET id = ?, name = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, supermarket.getId(), supermarket.getName(), supermarket.getId());
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