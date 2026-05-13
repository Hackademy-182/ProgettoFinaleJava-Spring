package aulab.it.the_aulab_chronicle.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import aulab.it.the_aulab_chronicle.dtos.ArticleDto;
import aulab.it.the_aulab_chronicle.dtos.CategoryDto;
import aulab.it.the_aulab_chronicle.models.Article;
import aulab.it.the_aulab_chronicle.models.Category;
import aulab.it.the_aulab_chronicle.repositories.ArticleRepository;
import aulab.it.the_aulab_chronicle.services.ArticleService;
import aulab.it.the_aulab_chronicle.services.CrudService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    @Qualifier("categoryService")
    private CrudService<CategoryDto, Category, Long> categoryService;
    
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ArticleRepository articleRepository;

    // Rotta get per entrare nel form di creazione dell'articolo

    @GetMapping("/create")
    public String articleCreate(Model viewModel){
        viewModel.addAttribute("title", "Crea un articolo");
        viewModel.addAttribute("article", new Article());
        viewModel.addAttribute("categories", categoryService.readAll());

        return "articles/create";
    }

    // Rotta per la pagina con tutti gli articoli

    @GetMapping
    public String articlesIndex (Model viewModel){

        viewModel.addAttribute("title", "Tutti gli articoli");

        List<ArticleDto> articles = new ArrayList<>();
        for (Article article : articleRepository.findByIsAcceptedTrue()) {
            articles.add(modelMapper.map(article, ArticleDto.class));
        }

        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());
        viewModel.addAttribute("articles", articles);

        return "articles/articles";
    }

    // Rotta per i dettagli degli articoli

    @GetMapping("/detail/{id}")
    public String detailArticle(@PathVariable("id") Long id, Model viewModel){
        viewModel.addAttribute("title", "Article title");
        viewModel.addAttribute("article", articleService.read(id));
        return "articles/detail";
    }

    // Rotta per lo store delle immagini

    @PostMapping
    public String articleStore(@Valid @ModelAttribute("article") Article article, BindingResult result,
                                RedirectAttributes redirectAttributes, Principal principal, 
                                MultipartFile file, Model viewModel){
        
        if(result.hasErrors()){
            viewModel.addAttribute("title", "Crea un articolo");
            viewModel.addAttribute("article", article);
            viewModel.addAttribute("categories", categoryService.readAll());

            return "articles/create";
        }

        articleService.create(article, principal, file);
        redirectAttributes.addFlashAttribute("successMessage", "Articolo creato con successo");

        return "redirect:/";
    }

    // Rotta dettaglio dell'articolo per revisore

    @GetMapping("/revisor/detail/{id}")
    public String revisorDetailArticle(@PathVariable("id") Long id, Model viewModel){
        viewModel.addAttribute("title", "Dettaglio articolo revisore");
        viewModel.addAttribute("article", articleService.read(id));

        return "revisor/detail";
    }

    // Rotta per azioni del revisore

    @PostMapping("/accept")
    public String articleSetAccepted(@RequestParam("action") String action,
                                     @RequestParam("articleId") Long articleId,
                                     RedirectAttributes redirectAttributes){

        if (action.equals("accept")) {
            articleService.setIsAccepted(true, articleId);
            redirectAttributes.addFlashAttribute("resultMessage", "Articolo accettato");
        }else if(action.equals("reject")){
            articleService.setIsAccepted(false, articleId);
            redirectAttributes.addFlashAttribute("resultMessage", "Articolo rifiutato");
        }else{
            redirectAttributes.addFlashAttribute("resultMessage", "Azione non corretta!");
        }

        return "redirect:/revisor/dashboard";
    }

    // Rotta per barra di ricerca

    @GetMapping("/search")
    public String articleSearch(@Param("keyword") String keyword, Model viewModel){

        viewModel.addAttribute("title", "Articoli trovati");

        List<ArticleDto> articles = articleService.search(keyword);

        List<ArticleDto> acceptedArticles = articles.stream().filter(article->Boolean.TRUE
                                            .equals(article.getIsAccepted())).collect(Collectors.toList());
        viewModel.addAttribute("articles", acceptedArticles);

        return "articles/articles";
    }

}
