package aulab.it.the_aulab_chronicle.services;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aulab.it.the_aulab_chronicle.dtos.ArticleDto;
import aulab.it.the_aulab_chronicle.models.Article;
import aulab.it.the_aulab_chronicle.models.User;
import aulab.it.the_aulab_chronicle.repositories.ArticleRepository;
import aulab.it.the_aulab_chronicle.repositories.UserRepository;

@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelmapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<ArticleDto> readAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArticleDto read(Long key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = (userRepository.findById(userDetails.getId())).get();
            article.setUser(user);
        }

        ArticleDto dto = modelmapper.map(articleRepository.save(article), ArticleDto.class);
        return dto;
    }

    @Override
    public ArticleDto update(Article model, Principal principal, MultipartFile file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Long key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
