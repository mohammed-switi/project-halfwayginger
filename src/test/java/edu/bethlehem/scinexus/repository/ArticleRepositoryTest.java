package edu.bethlehem.scinexus.repository;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.JPARepository.ArticleRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;


    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .firstName("Mohammed")
                .lastName("Sowaity")
                .username("mohammed")
                .email("mohammed@example.com")
                .password("MustEncrypt")
                .role(Role.ACADEMIC)
                .position(Position.ASSISTANT_PROFESSOR)
                .education("Bethlehem University")
                .phoneNumber("0593333333")
                .bio("NOTHING")
                .fieldOfWork("Software")
                .build();

        userRepository.save(user);
    }
    @Test
    public void Article_SaveAll_ReturnArticle() {
        Article article = Article.builder()
                .content("Content 1") // Provide actual content
                .visibility(Visibility.PUBLIC) // Provide actual visibility
                .title("Title 1") // Provide actual title
                .subject("Subject 1") // Provide actual subject
                .publisher(user) // Assuming getUserId() returns a User entity based on the provided user ID
                .build();

        Article savedArticle = articleRepository.save(article);

        Assertions.assertThat(savedArticle).isNotNull();
        Assertions.assertThat(savedArticle.getId()).isNotNull();
    }

    @Test
    public void Article_GetAll_ReturnMoreThanOneArticle() {
        Article article1 = Article.builder()
                .content("Content 1")
                .visibility(Visibility.PUBLIC)
                .title("Title 1")
                .subject("Subject 1")
                .publisher(user)
                .build();

        Article article2 = Article.builder()
                .content("Content 2")
                .visibility(Visibility.PRIVATE)
                .title("Title 2")
                .subject("Subject 2")
                .publisher(user)
                .build();

        articleRepository.saveAll(List.of(article1, article2));

        List<Article> articles = articleRepository.findAll();

        Assertions.assertThat(articles).isNotNull();
        Assertions.assertThat(articles.size()).isEqualTo(2);
    }

    @Test
    public void Article_FindByID_GetById() {
        Article article = Article.builder()
                .content("Content")
                .visibility(Visibility.PUBLIC)
                .title("Title")
                .subject("Subject")
                .publisher(user)
                .build();

        articleRepository.save(article);

        Optional<Article> foundArticle = articleRepository.findById(article.getId());

        Assertions.assertThat(foundArticle).isPresent();
    }

    @Test
    public void Article_FindByStatus_ReturnArticleNotNull() {
        Article article = Article.builder()
                .content("Content")
                .visibility(Visibility.PUBLIC)
                .title("Title")
                .subject("Subject")
                .publisher(user)
                .build();

        articleRepository.save(article);

        Optional<Article> foundArticle = articleRepository.findByVisibility(Visibility.PUBLIC);

        Assertions.assertThat(foundArticle).isPresent();
    }

    @Test
    public void Article_UpdateArticle_ReturnArticleNotNull() {
        Article article = Article.builder()
                .content("Content")
                .visibility(Visibility.PUBLIC)
                .title("Title")
                .subject("Subject")
                .publisher(user)
                .build();

        Article savedArticle = articleRepository.save(article);

        savedArticle.setVisibility(Visibility.PRIVATE);
        savedArticle.setContent("Updated Content");

        Article updatedArticle = articleRepository.save(savedArticle);

        Assertions.assertThat(updatedArticle.getVisibility()).isEqualTo(Visibility.PRIVATE);
        Assertions.assertThat(updatedArticle.getContent()).isEqualTo("Updated Content");
    }

    @Test
    public void Article_DeleteArticle_ReturnArticleNotNull() {
        Article article = Article.builder()
                .content("Content")
                .visibility(Visibility.PUBLIC)
                .title("Title")
                .subject("Subject")
                .publisher(user)
                .build();

        Article savedArticle = articleRepository.save(article);

        articleRepository.deleteById(savedArticle.getId());

        Optional<Article> deletedArticle = articleRepository.findById(savedArticle.getId());

        Assertions.assertThat(deletedArticle).isEmpty();
    }

    // Method to create and save a user (you can replace it with your actual implementation)

}
