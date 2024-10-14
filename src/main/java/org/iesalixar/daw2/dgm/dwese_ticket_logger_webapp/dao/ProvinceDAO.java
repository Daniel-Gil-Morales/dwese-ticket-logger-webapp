package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Province;

import java.sql.SQLException;
import java.util.List;


public interface ProvinceDAO {

    List<org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Province> listAllProvinces() throws SQLException;
    void insertProvince(Province province) throws SQLException;
    void updateProvince(Province province) throws SQLException;
    void deleteProvince(int id) throws SQLException;
    Province getProvinceById(int id) throws SQLException;
    boolean existsProvinceByCode(String code) throws SQLException;
    boolean existsProvinceByCodeAndNotId(String code, int id) throws SQLException;
    // List<Province> listProvincesByRegionID(int regionid) throws SQLException;

}