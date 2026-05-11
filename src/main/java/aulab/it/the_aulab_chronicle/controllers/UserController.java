package aulab.it.the_aulab_chronicle.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import aulab.it.the_aulab_chronicle.dtos.ArticleDto;
import aulab.it.the_aulab_chronicle.dtos.UserDto;
import aulab.it.the_aulab_chronicle.models.User;
import aulab.it.the_aulab_chronicle.repositories.CareerRequestRepository;
import aulab.it.the_aulab_chronicle.services.ArticleService;
import aulab.it.the_aulab_chronicle.services.CategoryService;
import aulab.it.the_aulab_chronicle.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CareerRequestRepository careerRequestRepository;

    @Autowired
    private CategoryService categoryService;

    // Home

    @GetMapping("/")
    public String home(Model viewModel){

        List<ArticleDto> articles = articleService.readAll();
        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());
        List<ArticleDto> lastFourArticles = articles.stream().limit(4)
                                                    .collect(Collectors.toList());
        viewModel.addAttribute("articles", lastFourArticles);
        return "home";
    }

    // Register

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new UserDto());
        return "auth/register";
    }

    // Login

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    // Salvataggio registrazione

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userdto, 
                                BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response){
        
        User existingUser = userService.findUserByEmail(userdto.getEmail());
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
           result.rejectValue("email", null, "C'è già un account registrato con questa email"); 
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userdto);
            return "auth/register";
        }

        userService.saveUser(userdto, redirectAttributes, request, response);
        redirectAttributes.addFlashAttribute("successMessage", "Registrazione avvenuta con successo");
        return "redirect:/";
    } 

    // Rotta ricerca per utente

    @GetMapping("/users/search/{id}")
    public String userArticlesSearch(@PathVariable("id") Long id, Model viewModel){
        User user = userService.find(id);
        viewModel.addAttribute("title", "Tutti gli articoli dell'utente: " + user.getUsername());

        List<ArticleDto> articles = articleService.searchByAuthor(user);
        viewModel.addAttribute("articles", articles);

        return "/articles/articles";
    }

    // Rotta dashboard admin

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model viewModel){
        viewModel.addAttribute("title", "Richieste ricevute");
        viewModel.addAttribute("requests", careerRequestRepository.findByIsCheckedFalse());
        viewModel.addAttribute("categories", categoryService.readAll());

        return "admin/dashboard";
    }
    
                                
}
