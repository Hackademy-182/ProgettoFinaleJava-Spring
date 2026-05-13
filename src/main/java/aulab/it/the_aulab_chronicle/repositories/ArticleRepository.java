package aulab.it.the_aulab_chronicle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import aulab.it.the_aulab_chronicle.models.Article;
import aulab.it.the_aulab_chronicle.models.Category;
import aulab.it.the_aulab_chronicle.models.User;

public interface ArticleRepository extends ListCrudRepository<Article, Long> {

    List<Article> findByCategory(Category category);
    List<Article> findByUser(User user);
    List<Article> findByIsAcceptedTrue();
    List<Article> findByIsAcceptedFalse();
    List<Article> findByIsAcceptedNull();
   
    @Query("SELECT COUNT(a) FROM Article a WHERE a.isAccepted IS NULL")
    long countByIsAcceptedNull();

    @Query("SELECT a FROM Article a WHERE " + 
    "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(a.subtitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(a.user.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(a.category.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Article> search(@Param("searchTerm") String searchTerm);
}
