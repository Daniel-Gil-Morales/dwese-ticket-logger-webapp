package org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.controllers;

import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.dao.CategoryDAO;
import org.iesalixar.daw2.dgm.dwese_ticket_logger_webapp.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    @GetMapping("/new")
    public String newCategoryForm(Model model) {
        model.addAttribute("category", new Category()); // Inicializa un nuevo objeto Category
        return "category_form"; // Devuelve el nombre de la vista para el formulario de nueva categoría
    }

    @PostMapping("/save")
    public String saveCategory(Category category) {
        if (category.getId() == null) {
            categoryDAO.insertCategory(category);
        } else {
            categoryDAO.updateCategory(category);
        }
        return "redirect:/categories"; // Redirige a la lista de categorías después de guardar
    }

    @GetMapping("/edit")
    public String editCategoryForm(@RequestParam("id") int id, Model model) {
        Category category = categoryDAO.getCategoryById(id);
        model.addAttribute("category", category); // Agrega la categoría al modelo para el formulario
        return "category_form"; // Devuelve el nombre de la vista para el formulario de edición
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam("id") int id) {
        categoryDAO.deleteCategory(id);
        return "redirect:/categories"; // Redirige a la lista de categorías después de eliminar
    }
}
