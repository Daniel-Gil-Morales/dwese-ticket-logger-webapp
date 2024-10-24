package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.SuperMarket;

import java.util.List;

public interface SuperMarketDAO {

    // Listar todos los supermercados
    List<SuperMarket> listAllSuperMarkets();

    // Insertar un nuevo supermercado
    void insertSuperMarket(SuperMarket supermarket);

    // Actualizar un supermercado existente
    void updateSuperMarket(SuperMarket supermarket);

    // Eliminar un supermercado por su ID
    void deleteSuperMarket(int id);

    // Obtener un supermercado por su ID
    SuperMarket getSuperMarketById(int id);
}
