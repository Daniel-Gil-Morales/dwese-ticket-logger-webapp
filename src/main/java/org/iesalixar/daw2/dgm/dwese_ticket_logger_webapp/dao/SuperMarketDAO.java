package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.SuperMarket;

import java.sql.SQLException;
import java.util.List;

public interface SuperMarketDAO {

    List<SuperMarket> listAllSupermarkets();
    void insertSuperMarket(SuperMarket superMarket);
    void updateSuperMarket(SuperMarket superMarket) throws SQLException;
    void deleteSuperMarket(int id);
    SuperMarket getSuperMarketById(int id);
}