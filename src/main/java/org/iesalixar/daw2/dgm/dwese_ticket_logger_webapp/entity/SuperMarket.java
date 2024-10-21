package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `SuperMarket` representa una entidad que modela un supermercado dentro de la base de datos.
 * Contiene un campo `id`, `name`, y una relación con `Location`.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperMarket {

    private Integer id;

    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    @Size(max = 100, message = "{msg.supermarket.name.size}")
    private String name;

}
