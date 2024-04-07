package edu.bethlehem.scinexus.repository;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.JPARepository.ArticleRepository;
import edu.bethlehem.scinexus.JPARepository.OpinionRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.Opinion.Opinion;
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

public class OpinionRepositoryTest {

    @Autowired
    private OpinionRepository opinionRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Article article;

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
        article = Article.builder()
                .content("Content 1")
                .visibility(Visibility.PUBLIC)
                .title("Title 1")
                .subject("Subject 1")
                .publisher(user)
                .build();


        articleRepository.save(article);
    }

    @Test
    public void Opinion_SaveAll_ReturnOpinion() {
        Opinion opinion = Opinion.builder()
                .content("Opinion Content")
                .opinionOwner(user)

                .journal(article)
                .build();

        Opinion savedOpinion = opinionRepository.save(opinion);

        Assertions.assertThat(savedOpinion).isNotNull();
        Assertions.assertThat(savedOpinion.getId()).isNotNull();
    }

    @Test
    public void Opinion_GetAll_ReturnMoreThanOneOpinion() {
        Opinion opinion1 = Opinion.builder()
                .content("Opinion Content 1")
                .journal(article)
                .opinionOwner(user)
                .build();

        Opinion opinion2 = Opinion.builder()
                .content("Opinion Content 2")
                .journal(article)
                .opinionOwner(user)
                .build();

        opinionRepository.save(opinion1);
        opinionRepository.save(opinion2);

        List<Opinion> opinions = opinionRepository.findAll();

        Assertions.assertThat(opinions).isNotNull();
        Assertions.assertThat(opinions.size()).isEqualTo(2);
    }

    @Test
    public void Opinion_FindByID_GetById() {
        Opinion opinion = Opinion.builder()
                .content("Opinion Content")
                .opinionOwner(user)
                .journal(article)
                .build();

        Opinion savedOpinion = opinionRepository.save(opinion);

        Optional<Opinion> foundOpinion = opinionRepository.findById(savedOpinion.getId());

        Assertions.assertThat(foundOpinion).isPresent();
    }

    // Additional test methods can be added as needed...


    @Test
    public void Opinion_UpdatedUser_ReturnOpinionNotNull() {
        // Create a new opinion
        Opinion opinion = Opinion.builder()
                .content("Opinion Content")
                .opinionOwner(user)
                .journal(article)
                .build();
        // Retrieve the saved opinion

            Opinion savedOpinion=  opinionRepository.save(opinion);


        savedOpinion.setContent("Another Content");

        // Update the user associated with the opinion

        Opinion updatedOpinion = opinionRepository.save(savedOpinion);


        // Assertion
        Assertions.assertThat(updatedOpinion.getContent()).isEqualTo("Another Content");
    }

    @Test
    public void Opinion_DeleteUser_ReturnOpinionNotNull() {
        // Create a new opinion
        Opinion opinion = Opinion.builder()
                .content("Opinion Content")
                .opinionOwner(user)
                .journal(article)
                .build();

        Opinion savedOpinion = opinionRepository.save(opinion);

        // Delete the user associated with the opinion
        opinionRepository.delete(savedOpinion);

        // Try to retrieve the opinion
        Optional<Opinion> deletedOpinion = opinionRepository.findById(savedOpinion.getId());

        // Assertion
        Assertions.assertThat(deletedOpinion).isEmpty();
    }

}
