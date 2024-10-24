package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao.SupermarketDAO;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Supermarket;
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
public class SupermarketController {

    private static final Logger logger = LoggerFactory.getLogger(SupermarketController.class);

    @Autowired
    private SupermarketDAO supermarketDAO; // Inyección de SuperMarketDAO

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos los supermercados y los pasa como atributo al modelo para que sean accesibles en la vista `supermarket.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de supermercados.
     */
    @GetMapping("")
    public String listSupermarkets(Model model) {
        List<Supermarket> listSupermarkets = supermarketDAO.listAllSupermarkets();
        model.addAttribute("listSupermarkets", listSupermarkets);
        return "supermarket"; // Asegúrate de que este nombre coincide con tu plantilla
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
        model.addAttribute("supermarket", new Supermarket()); // Crear un nuevo objeto SuperMarket
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
        Supermarket superMarket = supermarketDAO.getSupermarketById(id);
        if (superMarket == null) {
            logger.warn("No se encontró el supermercado con ID {}", id);
            return "redirect:/supermarkets"; // Redirigir si no se encuentra el supermercado
        }

        model.addAttribute("supermarket", superMarket); // Agregar el supermercado al modelo
        return "supermarket-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    @PostMapping("/insert")
    public String insertSupermarket(@Valid Supermarket supermarket, BindingResult result, Model model, Locale locale) throws SQLException {
        if (result.hasErrors()) {
            logger.warn("Errores en el formulario de inserción de supermercado: {}", result.getFieldErrors());
            return "supermarket-form";
        }
        supermarketDAO.insertSupermarket(supermarket);
        logger.info("Supermercado insertado correctamente: {}", supermarket.getName());
        return "redirect:/supermarkets";
    }


    @PostMapping("/update")
    public String updateSupermarket(@Valid @ModelAttribute("supermarket") Supermarket supermarket,
                                    BindingResult result, Model model) throws SQLException {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Por favor corrige los errores en el formulario.");
            return "supermarket-form"; // Reenviar al formulario
        }

        // Aquí puedes añadir lógica para verificar si el ID ha cambiado
        // y manejarlo según sea necesario.

        supermarketDAO.updateSupermarket(supermarket); // Actualizar supermercado
        model.addAttribute("successMessage", "Supermercado actualizado correctamente.");
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
            supermarketDAO.deleteSupermarket(id); // Asegúrate de que superMarketDAO esté inyectado correctamente
            redirectAttributes.addFlashAttribute("successMessage", "Supermercado eliminado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el supermercado: " + e.getMessage());
            logger.error("Error al eliminar el supermercado con ID {}: {}", id, e.getMessage());
        }
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }
}