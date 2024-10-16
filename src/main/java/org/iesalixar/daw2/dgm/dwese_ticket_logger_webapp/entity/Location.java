package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

/**
 * La clase `Location` representa una entidad que modela una ubicación dentro de la base de datos.
 * Contiene cuatro campos: `id`, `code`, `address` y `city`, donde `id` es el identificador único de la ubicación,
 * `code` es un código asociado a la ubicación, `address` es la dirección de la ubicación,
 * y `city` es la ciudad donde se encuentra la ubicación.
 *
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Data  // Genera getters, setters, equals, hashCode, toString y canEqual automáticamente.
@NoArgsConstructor  // Genera un constructor sin argumentos.
@AllArgsConstructor  // Genera un constructor con todos los campos como parámetros.
public class Location {

    // Identificador único de la ubicación. Puede ser nulo antes de ser persistido en la base de datos.
    private Integer id;

    // Código de la ubicación, que suele ser una cadena corta para identificarla.
    @NotEmpty(message = "{msg.location.code.notEmpty}")
    @Size(max = 10, message = "{msg.location.code.size}")
    private String code;

    // Dirección de la ubicación.
    @NotEmpty(message = "{msg.location.address.notEmpty}")
    @Size(max = 255, message = "{msg.location.address.size}")
    private String address;

    // Ciudad donde se encuentra la ubicación.
    @NotEmpty(message = "{msg.location.city.notEmpty}")
    @Size(max = 100, message = "{msg.location.city.size}")
    private String city;

    /**
     * Constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Location` antes de persistirlas, donde el `id` es autogenerado.
     * @param code Código de la ubicación.
     * @param address Dirección de la ubicación.
     * @param city Ciudad donde se encuentra la ubicación.
     */
    public Location(String code, String address, String city) {
        this.code = code;
        this.address = address;
        this.city = city;
    }
}
