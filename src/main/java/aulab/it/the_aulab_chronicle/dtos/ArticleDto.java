package aulab.it.the_aulab_chronicle.dtos;

import java.time.LocalDate;

import aulab.it.the_aulab_chronicle.models.Category;
import aulab.it.the_aulab_chronicle.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {

    private Long id;
    private String title;
    private String subtitle;
    private String Body;
    private LocalDate publisDate;
    private User user;
    private Category category;

}
