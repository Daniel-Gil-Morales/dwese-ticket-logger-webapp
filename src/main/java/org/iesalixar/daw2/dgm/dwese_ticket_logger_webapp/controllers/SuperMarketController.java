package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao.SuperMarketDAO; // Inyectar el DAO de SuperMarket
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.SuperMarket;
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
 * Controlador que maneja las operaciones CRUD para la entidad `SuperMarket`.
 * Utiliza `SuperMarketDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/supermarkets")
public class SuperMarketController {

    private static final Logger logger = LoggerFactory.getLogger(SuperMarketController.class);

    @Autowired
    private SuperMarketDAO superMarketDAO; // Inyección de SuperMarketDAO

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos los supermercados y los pasa como atributo al modelo para que sean accesibles en la vista `supermarket.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de supermercados.
     */
    @GetMapping("")
    public String listSuperMarkets(Model model) {
        List<SuperMarket> listSuperMarkets;
        logger.info("Obteniendo la lista de supermercados.");
        listSuperMarkets = superMarketDAO.listAllSuperMarkets();
        logger.info("Número de supermercados obtenidos: {}", listSuperMarkets.size());
        model.addAttribute("listSuperMarkets", listSuperMarkets);
        return "supermarket";
    }

    /**
     * Muestra el formulario para crear un nuevo supermercado.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo supermercado.");
        model.addAttribute("supermarket", new SuperMarket()); // Crear un nuevo objeto SuperMarket
        return "supermarket-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    /**
     * Muestra el formulario para editar un supermercado existente.
     *
     * @param id    ID del supermercado a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) throws SQLException {
        logger.info("Mostrando formulario de edición para el supermercado con ID {}", id);

        // Cargar el supermercado por ID
        SuperMarket superMarket = superMarketDAO.getSuperMarketById(id);
        if (superMarket == null) {
            logger.warn("No se encontró el supermercado con ID {}", id);
            return "redirect:/supermarkets"; // Redirigir si no se encuentra el supermercado
        }

        model.addAttribute("supermarket", superMarket); // Agregar el supermercado al modelo
        return "supermarket-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Inserta un nuevo supermercado en la base de datos.
     *
     * @param superMarket        Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/insert")
    public String insertSuperMarket(@Valid @ModelAttribute("supermarket") SuperMarket superMarket,
                                    BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nuevo supermercado: {}", superMarket.getName());
        if (result.hasErrors()) {
            return "supermarket-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        superMarketDAO.insertSuperMarket(superMarket);
        logger.info("Supermercado {} insertado con éxito.", superMarket.getName());
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }

    /**
     * Actualiza un supermercado existente en la base de datos.
     *
     * @param superMarket        Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/update")
    public String updateSuperMarket(@Valid @ModelAttribute("supermarket") SuperMarket superMarket,
                                    BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando supermercado con ID {}", superMarket.getId());
        try {
            if (result.hasErrors()) {
                return "supermarket-form";  // Devuelve el formulario para mostrar los errores de validación
            }
            superMarketDAO.updateSuperMarket(superMarket);
            logger.info("Supermercado con ID {} actualizado con éxito.", superMarket.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar el supermercado con ID {}: {}", superMarket.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }

    /**
     * Elimina un supermercado de la base de datos.
     *
     * @param id                ID del supermercado a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/delete")
    public String deleteSuperMarket(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        try {
            superMarketDAO.deleteSuperMarket(id); // Asegúrate de que superMarketDAO esté inyectado correctamente
            redirectAttributes.addFlashAttribute("successMessage", "Supermercado eliminado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el supermercado: " + e.getMessage());
            logger.error("Error al eliminar el supermercado con ID {}: {}", id, e.getMessage());
        }
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }
}