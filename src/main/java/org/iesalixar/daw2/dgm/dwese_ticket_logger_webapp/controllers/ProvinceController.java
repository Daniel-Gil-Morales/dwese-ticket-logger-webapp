package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao.ProvinceDAO;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao.RegionDAO;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Province;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Region;
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
 * Controlador que maneja las operaciones CRUD para la entidad `Province`.
 * Utiliza `ProvinceDAO` y `RegionDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/provinces")
public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceDAO provinceDAO;

    @Autowired
    private RegionDAO regionDAO; // Inyección de RegionDAO

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todas las provincias y las pasa como atributo al modelo para que sean accesibles en la vista `province.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de provincias.
     */
    @GetMapping
    public String listProvinces(Model model) {
        logger.info("Solicitando la lista de todas las provincias...");
        List<Province> listProvinces = null;
        try {
            listProvinces = provinceDAO.listAllProvinces();
            logger.info("Se han cargado {} provincias.", listProvinces.size());
        } catch (SQLException e) {
            logger.error("Error al listar las provincias: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar las provincias.");
        }
        model.addAttribute("listProvinces", listProvinces); // Pasar la lista de provincias al modelo
        return "province"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    /**
     * Muestra el formulario para crear una nueva provincia.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva provincia.");
        model.addAttribute("province", new Province()); // Crear un nuevo objeto Province
        return "province-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    /**
     * Muestra el formulario para editar una provincia existente.
     *
     * @param id    ID de la provincia a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para la provincia con ID {}", id);
        Province province = null;
        try {
            province = provinceDAO.getProvinceById(id);
            if (province == null) {
                logger.warn("No se encontró la provincia con ID {}", id);
                model.addAttribute("errorMessage", "No se encontró la provincia.");
                return "redirect:/provinces"; // Redirigir si no se encuentra la provincia
            }
        } catch (SQLException e) {
            logger.error("Error al obtener la provincia con ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Error al obtener la provincia.");
            return "redirect:/provinces"; // Redirigir en caso de error
        }
        model.addAttribute("province", province);

        // Cargar todas las regiones
        try {
            List<Region> regions = regionDAO.listAllRegions();
            model.addAttribute("regions", regions);
        } catch (SQLException e) {
            logger.error("Error al listar las regiones: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar las regiones.");
        }

        return "province-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    /**
     * Inserta una nueva provincia en la base de datos.
     *
     * @param province           Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/insert")
    public String insertProvince(@Valid @ModelAttribute("province") Province province, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nueva provincia con código {}", province.getCode());
        try {
            if (result.hasErrors()) {
                return "province-form";  // Devuelve el formulario para mostrar los errores de validación
            }
            if (provinceDAO.existsProvinceByCode(province.getCode())) {
                logger.warn("El código de la provincia {} ya existe.", province.getCode());
                String errorMessage = messageSource.getMessage("msg.province-controller.insert.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/provinces/new";
            }
            provinceDAO.insertProvince(province);
            logger.info("Provincia {} insertada con éxito.", province.getCode());
        } catch (SQLException e) {
            logger.error("Error al insertar la provincia {}: {}", province.getCode(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.province-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/provinces"; // Redirigir a la lista de provincias
    }

    /**
     * Actualiza una provincia existente en la base de datos.
     *
     * @param province           Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/update")
    public String updateProvince(@Valid @ModelAttribute("province") Province province, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando provincia con ID {}", province.getId());
        try {
            if (result.hasErrors()) {
                return "province-form";  // Devuelve el formulario para mostrar los errores de validación
            }
            if (provinceDAO.existsProvinceByCodeAndNotId(province.getCode(), province.getId())) {
                logger.warn("El código de la provincia {} ya existe para otra provincia.", province.getCode());
                String errorMessage = messageSource.getMessage("msg.province-controller.update.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/provinces/edit?id=" + province.getId();
            }
            provinceDAO.updateProvince(province);
            logger.info("Provincia con ID {} actualizada con éxito.", province.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar la provincia con ID {}: {}", province.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.province-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/provinces"; // Redirigir a la lista de provincias
    }
}
