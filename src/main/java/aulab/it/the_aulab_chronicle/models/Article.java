package aulab.it.the_aulab_chronicle.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotEmpty
    @Size(max = 100)
    private String title;

    @Column(nullable = false, length = 100)
    @NotEmpty
    @Size(max = 100)
    private String subtitle;

    @Column(nullable = false, length = 1000)
    @NotEmpty
    @Size(max = 1000)
    private String body;

    @Column(nullable = true, length = 8)
    @NotNull
    private LocalDate publishDate;

    // Relazione articoli con utente

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("articles")
    private User user;

    // Relazione tanti articoli con categorie

    @ManyToOne
    @JsonIgnoreProperties("articles")
    private Category category;
}
