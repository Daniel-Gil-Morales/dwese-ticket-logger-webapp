package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Location` representa una entidad que modela una ubicación dentro de la base de datos.
 * Contiene un campo `id`, `address`, `city` y un objeto `Province`.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private Integer id;

    @NotEmpty(message = "{msg.location.address.notEmpty}")
    @Size(max = 200, message = "{msg.location.address.size}")
    private String address;

    @NotEmpty(message = "{msg.location.city.notEmpty}")
    @Size(max = 100, message = "{msg.location.city.size}")
    private String city;

    // Relación con la entidad `Province`
    private Province province;

    // Relación con la entidad `SuperMarket`
    private Supermarket supermarket;

}
