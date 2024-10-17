package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao.LocationDAO;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao.ProvinceDAO; // Inyectar el DAO de provincias
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Location;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Location`.
 * Utiliza `LocationDAO` y `ProvinceDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationDAO locationDAO;

    @Autowired
    private ProvinceDAO provinceDAO; // Inyección de ProvinceDAO

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todas las ubicaciones y las pasa como atributo al modelo para que sean accesibles en la vista `location.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de ubicaciones.
     */
    @GetMapping("")
    public String listLocations(Model model) {
        List<Location> listLocations;
        try {
            logger.info("Obteniendo la lista de ubicaciones.");
            listLocations = locationDAO.listAllLocations();
            logger.info("Número de ubicaciones obtenidas: {}", listLocations.size());
            model.addAttribute("locations", listLocations);
        } catch (SQLException e) {
            model.addAttribute("errorMessage", "Error al cargar las ubicaciones.");
            logger.error("Error al cargar las ubicaciones: {}", e.getMessage());
            return "location"; // Retorna a la vista para mostrar el error
        }
        model.addAttribute("listLocations", listLocations);
        return "location";
    }

    /**
     * Muestra el formulario para crear una nueva ubicación.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) throws SQLException {
        logger.info("Mostrando formulario para nueva ubicación.");
        model.addAttribute("location", new Location()); // Crear un nuevo objeto Location
        List<Province> provinces = provinceDAO.listAllProvinces(); // Cargar la lista de provincias
        model.addAttribute("provinces", provinces); // Pasar la lista de provincias al modelo
        return "location-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    /**
     * Muestra el formulario para editar una ubicación existente.
     *
     * @param id    ID de la ubicación a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) throws SQLException {
        logger.info("Mostrando formulario de edición para la ubicación con ID {}", id);

        // Cargar la ubicación por ID
        Location location = locationDAO.getLocationById(id);
        if (location == null) {
            logger.warn("No se encontró la ubicación con ID {}", id);
            return "redirect:/locations"; // Redirigir si no se encuentra la ubicación
        }

        model.addAttribute("location", location); // Agregar la ubicación al modelo
        List<Province> provinces = provinceDAO.listAllProvinces(); // Cargar la lista de provincias
        model.addAttribute("provinces", provinces); // Pasar la lista de provincias al modelo

        return "location-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Inserta una nueva ubicación en la base de datos.
     *
     * @param location           Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de ubicaciones.
     */
    @PostMapping("/insert")
    public String insertLocation(@Valid @ModelAttribute("location") Location location, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nueva ubicación en {}", location.getAddress());
        try {
            if (result.hasErrors()) {
                return "location-form";  // Devuelve el formulario para mostrar los errores de validación
            }
            locationDAO.insertLocation(location);
            logger.info("Ubicación en {} insertada con éxito.", location.getAddress());
        } catch (SQLException e) {
            logger.error("Error al insertar la ubicación en {}: {}", location.getAddress(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.location-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/locations"; // Redirigir a la lista de ubicaciones
    }

    /**
     * Actualiza una ubicación existente en la base de datos.
     *
     * @param location           Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de ubicaciones.
     */
    @PostMapping("/update")
    public String updateLocation(@Valid @ModelAttribute("location") Location location, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando ubicación con ID {}", location.getId());
        try {
            if (result.hasErrors()) {
                return "location-form";  // Devuelve el formulario para mostrar los errores de validación
            }
            locationDAO.updateLocation(location);
            logger.info("Ubicación con ID {} actualizada con éxito.", location.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar la ubicación con ID {}: {}", location.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.location-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/locations"; // Redirigir a la lista de ubicaciones
    }

    /**
     * Elimina una ubicación de la base de datos.
     *
     * @param id                ID de la ubicación a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de ubicaciones.
     */
    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        try {
            locationDAO.deleteLocation(id); // Asegúrate de que locationDAO esté inyectado correctamente
            redirectAttributes.addFlashAttribute("successMessage", "Ubicación eliminada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la ubicación: " + e.getMessage());
            logger.error("Error al eliminar la ubicación con ID {}: {}", id, e.getMessage());
        }
        return "redirect:/locations"; // Redirigir a la lista de ubicaciones
    }

}
