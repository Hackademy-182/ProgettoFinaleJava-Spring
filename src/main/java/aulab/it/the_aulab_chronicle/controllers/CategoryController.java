package aulab.it.the_aulab_chronicle.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import aulab.it.the_aulab_chronicle.dtos.ArticleDto;
import aulab.it.the_aulab_chronicle.dtos.CategoryDto;
import aulab.it.the_aulab_chronicle.models.Category;
import aulab.it.the_aulab_chronicle.services.ArticleService;
import aulab.it.the_aulab_chronicle.services.CategoryService;

@Controller
@RequestMapping
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
        viewModel.addAttribute("articles", articles);

        return "/articles/articles";
    }
}
