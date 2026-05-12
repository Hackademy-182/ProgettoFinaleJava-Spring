package aulab.it.the_aulab_chronicle.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import aulab.it.the_aulab_chronicle.models.Article;
import aulab.it.the_aulab_chronicle.models.Category;
import aulab.it.the_aulab_chronicle.models.User;

public interface ArticleRepository extends ListCrudRepository<Article, Long> {

    List<Article> findByCategory(Category category);
    List<Article> findByUser(User user);
    List<Article> findByIsAcceptedTrue();
    List<Article> findByIsAcceptedFalse();
    List<Article> findByIsAcceptedNull();
}
