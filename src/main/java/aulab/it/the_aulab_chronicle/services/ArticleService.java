package aulab.it.the_aulab_chronicle.services;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import aulab.it.the_aulab_chronicle.dtos.ArticleDto;
import aulab.it.the_aulab_chronicle.models.Article;
import aulab.it.the_aulab_chronicle.models.User;
import aulab.it.the_aulab_chronicle.repositories.ArticleRepository;
import aulab.it.the_aulab_chronicle.repositories.UserRepository;
import aulab.it.the_aulab_chronicle.models.Category;
import aulab.it.the_aulab_chronicle.models.Image;

@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ImageService imageService;

    @Override
    public List<ArticleDto> readAll() {
        
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for (Article article : articleRepository.findAll()) {
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

    @Override
    public ArticleDto read(Long key) {
        Optional<Article> optionalArticle = articleRepository.findById(key);
        if (optionalArticle.isPresent()) {
            return modelMapper.map(optionalArticle.get(), ArticleDto.class);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Articolo non trovato");
        }
    }

    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile file) {

        String url = "";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = (userRepository.findById(userDetails.getId())).get();
            article.setUser(user);
        }

        article.setPublishDate(LocalDate.now());

        if (!file.isEmpty()) {
            try {
                CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
                url = futureUrl.get();
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        article.setIsAccepted(null);

        ArticleDto dto = modelMapper.map(articleRepository.save(article), ArticleDto.class);
        if (!file.isEmpty()) {
            imageService.saveImageOnDB(url, article);
        }
        return dto;
    }

   
    @Override
    public ArticleDto update(Long key, Article updatedArticle, MultipartFile file) {

        Article existingArticle = articleRepository.findById(key)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Articolo non trovato"));

        updatedArticle.setId(key);
        updatedArticle.setUser(existingArticle.getUser());
        updatedArticle.setPublishDate(existingArticle.getPublishDate());
        updatedArticle.setIsAccepted(null);

        try {

            // ======================
            // IMMAGINE (DELEGATA)
            // ======================
            if (file != null && !file.isEmpty()) {

                // elimina vecchia immagine da cloud (se esiste)
                if (existingArticle.getImage() != null) {
                    imageService.deleteImage(existingArticle.getImage().getPath());
                }

                // upload nuova immagine
                String url = imageService.saveImageOnCloud(file).get();

                // salva o aggiorna immagine nel DB (ORA È INTELLIGENTE)
                imageService.saveImageOnDB(url, updatedArticle);
            } else {
                // nessun file nuovo → mantieni immagine
                updatedArticle.setImage(existingArticle.getImage());
            }

            Article saved = articleRepository.save(updatedArticle);

            return modelMapper.map(saved, ArticleDto.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante aggiornamento articolo");
        }
    }

    @Override
    public void delete(Long key) {
        
        if (articleRepository.existsById(key)) {
            Article article = articleRepository.findById(key).get();

            try {
                String path = article.getImage().getPath();
                article.getImage().setArticle(null);
                imageService.deleteImage(path);
            } catch (Exception e) {
                 e.printStackTrace();
            }
            articleRepository.deleteById(key);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    // ricerca per categoria

    public List<ArticleDto> searchByCategory(Category category){

        List<ArticleDto> dtos = new ArrayList<>();
        for(Article article: articleRepository.findByCategory(category)){
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

    // ricerca per autore

    public List<ArticleDto> searchByAuthor(User user){

        List<ArticleDto> dtos = new ArrayList<>();
        for (Article article : articleRepository.findByUser(user)) {
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

    public void setIsAccepted(Boolean result, Long id){

        Article article = articleRepository.findById(id).get();
        article.setIsAccepted(result);
        articleRepository.save(article);
    }

    // Funzionamento barra di ricerca

    public List<ArticleDto> search(String keyword){

        List<ArticleDto> dtos = new ArrayList<>();

        for (Article article : articleRepository.search(keyword)) {
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }
    

}
