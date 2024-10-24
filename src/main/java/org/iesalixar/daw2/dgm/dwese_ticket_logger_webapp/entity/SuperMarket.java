package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity;


import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * La clase `Supermarket` representa una entidad que modela un supermercado.
 * Contiene dos campos: `id` y `name`, donde `id` es el identificador único del supermercado,
 * y `name` es el nombre del supermercado.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "supermarkets") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperMarket {


    // Identificador único del supermercado. Es autogenerado y clave primaria.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID.
    private Integer id;


    // Nombre del supermercado. No puede estar vacío.
    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    @Column(name = "name", nullable = false) // Define la columna correspondiente en la tabla.
    private String name;

    // Relación uno a muchos con la entidad `Location`. Un supermercado puede tener muchas ubicaciones.
    @OneToMany(mappedBy = "supermarket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Location> locations;

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Supermarket`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarlo en la base de datos).
     * @param name Nombre del supermercado.
     */
    public SuperMarket(String name) {
        this.name = name;
    }
}