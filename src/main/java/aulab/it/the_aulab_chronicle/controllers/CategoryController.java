package aulab.it.the_aulab_chronicle.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import aulab.it.the_aulab_chronicle.dtos.ArticleDto;
import aulab.it.the_aulab_chronicle.dtos.CategoryDto;
import aulab.it.the_aulab_chronicle.models.Category;
import aulab.it.the_aulab_chronicle.services.ArticleService;
import aulab.it.the_aulab_chronicle.services.CategoryService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    // Rotta ricerca articolo per categoria

    @GetMapping("/search/{id}")
    public String searchCategory(@PathVariable("id") Long id, Model viewModel){
        
        CategoryDto category = categoryService.read(id);

        viewModel.addAttribute("title", "Ricerca per categoria: " + category.getName());

        List<ArticleDto> articles = articleService.searchByCategory(modelMapper.map(category, Category.class));

        List<ArticleDto> acceptedArticles = articles.stream()
                                            .filter(article->Boolean.TRUE.equals(article.getIsAccepted()))
                                            .collect(Collectors.toList());
        viewModel.addAttribute("articles", acceptedArticles);

        return "/articles/articles";
    }

    // Rotta per nuova categoria

    @GetMapping("/create")
    public String categoryCreate(Model viewModel){
        viewModel.addAttribute("title", "Crea nuova categoria");
        viewModel.addAttribute("category", new Category());
        return "category/create";
    }

    // Rotta per salvataggio categoria

    @PostMapping
    public String categoryStore(@Valid @ModelAttribute("category") Category category,
                                BindingResult result, RedirectAttributes redirectAttributes,
                                Model viewModel){

        if (result.hasErrors()) {
            viewModel.addAttribute("title", "Crea una categoria");
            viewModel.addAttribute("category", category);
            return "category/create";
        }
        categoryService.create(category, null, null);
        redirectAttributes.addFlashAttribute("successMessage", "Categoria aggiunta con successo");

        return "redirect:/admin/dashboard";
    }

    // Rotta modifica categoria

    @GetMapping("/edit/{id}")
    public String categoryEdit(@PathVariable("id") Long id, Model viewModel){

        viewModel.addAttribute("title", "Modifica categoria");
        viewModel.addAttribute("category", categoryService.read(id));
        return "/category/update";
    }

    // Rotta salvataggio modifica categoria

    @PostMapping("/update/{id}")
    public String categoryUpdate(@Valid @ModelAttribute("id") Long id, 
                                @Valid @ModelAttribute("category") Category category,
                                BindingResult result, RedirectAttributes redirectAttributes,
                                Model viewModel){

        if (result.hasErrors()) {
            viewModel.addAttribute("title", "Modifica categoria");
            viewModel.addAttribute("category", category);
            return "category/update";
        }
        categoryService.update(id, category, null);
        redirectAttributes.addFlashAttribute("successMessage", "Categoria modificata con successo");

        return "redirect:/admin/dashboard";
    }

    // Rotta cancellazione categoria

    @GetMapping("/delete/{id}")
    public String categoryDelete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){

        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cancellazione avvenuta con successo");
    
        return "redirect:/admin/dashboard";
    }

}
