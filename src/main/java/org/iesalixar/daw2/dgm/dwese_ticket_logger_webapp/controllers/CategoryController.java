package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao.CategoryDAO;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryDAO categoryDAO;

    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryDAO.listAllCategories();
        model.addAttribute("listCategories", categories);
        return "category"; // Devuelve el nombre de la vista category.html
    }

    /**
     * Muestra el formulario para crear una nueva categoría.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("category", new Category()); // Crear un nuevo objeto Category
        return "category-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @PostMapping("/insert")
    public String insertCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        if (category.getId() == null) {
            categoryDAO.updateCategory(category);
        } else {
            categoryDAO.updateCategory(category);
        }
        return "redirect:/categories"; // Redirigir a la lista de categorías
    }

    @PostMapping("/update")
    public String saveCategory(Category category) {
        if (category.getId() == null) {
            categoryDAO.insertCategory(category);
        } else {
            categoryDAO.updateCategory(category);
        }
        return "redirect:/categories"; // Redirige a la lista de categorías después de guardar
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) throws SQLException {
        // Cargar la categoría por ID
        Category category = categoryDAO.getCategoryById(id);
        if (category == null) {
            return "redirect:/categories"; // Redirigir si no se encuentra la categoría
        }

        model.addAttribute("category", category); // Agregar la categoría al modelo
        return "category-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam("id") int id) {
        categoryDAO.deleteCategory(id);
        return "redirect:/categories"; // Redirige a la lista de categorías después de eliminar
    }
}
