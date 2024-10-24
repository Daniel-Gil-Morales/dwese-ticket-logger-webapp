package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * La clase `Category` representa una entidad que modela una categoría dentro de la base de datos.
 * Contiene campos como `id`, `name`, `image`, y `parentId`, donde `id` es el identificador único de la categoría.
 */
@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "categories") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    // Campo que almacena el identificador único de la categoría.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Campo que almacena el nombre de la categoría.
    @NotEmpty(message = "{msg.category.name.notEmpty}")
    @Size(max = 255, message = "{msg.category.name.size}")
    @Column(name = "name", nullable = false) // Define la columna correspondiente en la tabla.
    private String name;

    // Campo que almacena la ruta de la imagen asociada a la categoría.
    @Size(max = 500, message = "{msg.category.image.size}")
    @Column(name = "image") // Define la columna correspondiente en la tabla.
    private String image;

    // Campo que almacena el ID de la categoría padre, si existe.
    @Column(name = "parent_id")
    private Integer parentId;

    // Relación recursiva con la misma entidad para representar las subcategorías.
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> subCategories;

    // Relación con la categoría padre.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Category parentCategory;
}
