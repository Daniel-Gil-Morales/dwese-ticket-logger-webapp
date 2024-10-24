package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Supermarket;

import java.sql.SQLException;
import java.util.List;

public interface SupermarketDAO {

    List<Supermarket> listAllSupermarkets();
    void insertSupermarket(Supermarket superMarket);
    void updateSupermarket(Supermarket superMarket) throws SQLException;
    void deleteSupermarket(int id);
    Supermarket getSupermarketById(int id);
}