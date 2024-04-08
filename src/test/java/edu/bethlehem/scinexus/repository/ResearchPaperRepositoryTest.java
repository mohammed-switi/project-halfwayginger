package edu.bethlehem.scinexus.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.bethlehem.scinexus.JPARepository.JournalRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.ResearchPaper.ResearchLanguage;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ResearchPaperRepositoryTest {

        @Autowired
        private JournalRepository journalRepository;
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
        public void researchPaperRepository_SaveAll_ReturnResearchPaper() {
                List<ResearchPaper> researchPapersToSave = new ArrayList<>();

                // Create and add some ResearchPaper entities to the list
                ResearchPaper researchPaper1 = ResearchPaper.builder()
                                .content("Content 1") // Provide actual content
                                .visibility(Visibility.PRIVATE) // Provide actual visibility
                                .language(ResearchLanguage.ENGLISH) // Provide actual language enum value
                                .title("Title 1") // Provide actual title
                                .subject("Subject 1") // Provide actual subject
                                .description("Description 1") // Provide actual description
                                .publisher(user) // Assuming getUserId() returns a User entity based on the provided
                                                 // user ID
                                .build();

                ResearchPaper researchPaper2 = ResearchPaper.builder()
                                .content("Content 2") // Provide actual content
                                .visibility(Visibility.PUBLIC) // Provide actual visibility
                                .language(ResearchLanguage.ENGLISH) // Provide actual language enum value
                                .title("Title 2") // Provide actual title
                                .subject("Subject 2") // Provide actual subject
                                .description("Description 2") // Provide actual description
                                .publisher(user) // Assuming getUserId() returns a User entity based on the provided
                                                 // user ID
                                .build();
                // Add the ResearchPaper entities to the list
                researchPapersToSave.add(researchPaper1);
                researchPapersToSave.add(researchPaper2);

                // Save the list of ResearchPaper entities
                List<ResearchPaper> savedResearchPapers = journalRepository.saveAll(researchPapersToSave);

                // Verify that the saved entities match the ones we saved
                Assertions.assertThat(savedResearchPapers.size()).isEqualTo(2);
                Assertions.assertThat(savedResearchPapers.get(0).getTitle()).isEqualTo("Title 1");
                Assertions.assertThat(savedResearchPapers.get(0).getDescription()).isEqualTo("Description 1");
                Assertions.assertThat(savedResearchPapers.get(0).getSubject()).isEqualTo("Subject 1");
                Assertions.assertThat(savedResearchPapers.get(1).getTitle()).isEqualTo("Title 2");
                Assertions.assertThat(savedResearchPapers.get(1).getDescription()).isEqualTo("Description 2");
                Assertions.assertThat(savedResearchPapers.get(1).getSubject()).isEqualTo("Subject 2");
                // Assert that the publisher of researchPaper1 is user
                Assertions.assertThat(researchPaper1.getPublisher()).isEqualTo(user);

        }

        @Test
        public void ResearchPaper_GetAll_ReturnMoreThanOneResearchPaper() {
                ResearchPaper researchPaper1 = ResearchPaper.builder()
                                .content("Content 1")
                                .visibility(Visibility.PUBLIC)
                                .language(ResearchLanguage.ENGLISH)
                                .title("Title 1")
                                .subject("Subject 1")
                                .description("Description 1")
                                .publisher(user)
                                .build();

                ResearchPaper researchPaper2 = ResearchPaper.builder()
                                .content("Content 2")
                                .visibility(Visibility.PRIVATE)
                                .language(ResearchLanguage.ENGLISH)
                                .title("Title 2")
                                .subject("Subject 2")
                                .description("Description 2")
                                .publisher(user)

                                .build();

                journalRepository.saveAll(List.of(researchPaper1, researchPaper2));

                List<Journal> researchPapers = journalRepository.findAll();

                Assertions.assertThat(researchPapers).isNotNull();
                Assertions.assertThat(researchPapers.size()).isEqualTo(2);
        }

        @Test
        public void ResearchPaper_FindByID_GetById() {
                ResearchPaper researchPaper = ResearchPaper.builder()
                                .content("Content")
                                .visibility(Visibility.PUBLIC)
                                .language(ResearchLanguage.ENGLISH)
                                .title("Title")
                                .subject("Subject")
                                .description("Description")
                                .publisher(user)

                                .build();

                journalRepository.save(researchPaper);

                Optional<Journal> foundResearchPaper = journalRepository.findById(researchPaper.getId());

                Assertions.assertThat(foundResearchPaper).isPresent();
        }

        @Test
        public void ResearchPaper_FindByStatus_ReturnResearchPaperNotNull() {
                ResearchPaper researchPaper = ResearchPaper.builder()
                                .content("Content")
                                .visibility(Visibility.PUBLIC)
                                .language(ResearchLanguage.ENGLISH)
                                .title("Title")
                                .subject("Subject")
                                .description("Description")
                                .publisher(user)

                                .build();

                journalRepository.save(researchPaper);

                Optional<Journal> foundResearchPaper = journalRepository.findByVisibility(Visibility.PUBLIC);

                Assertions.assertThat(foundResearchPaper).isPresent();
        }

        @Test
        public void ResearchPaper_UpdatedUser_ReturnResearchPaperNotNull() {
                ResearchPaper researchPaper = ResearchPaper.builder()
                                .content("Content")
                                .visibility(Visibility.PUBLIC)
                                .language(ResearchLanguage.ENGLISH)
                                .title("Title")
                                .subject("Subject")
                                .description("Description")
                                .publisher(user)

                                .build();

                ResearchPaper savedResearchPaper = journalRepository.save(researchPaper);

                savedResearchPaper.setVisibility(Visibility.PRIVATE);
                savedResearchPaper.setDescription("Updated Description");

                ResearchPaper updatedResearchPaper = journalRepository.save(savedResearchPaper);

                Assertions.assertThat(updatedResearchPaper.getVisibility()).isEqualTo(Visibility.PRIVATE);
                Assertions.assertThat(updatedResearchPaper.getDescription()).isEqualTo("Updated Description");
        }

        @Test
        public void ResearchPaper_DeleteUser_ReturnResearchPaperNotNull() {
                ResearchPaper researchPaper = ResearchPaper.builder()
                                .content("Content")
                                .visibility(Visibility.PUBLIC)
                                .language(ResearchLanguage.ENGLISH)
                                .title("Title")
                                .subject("Subject")
                                .description("Description")
                                .publisher(user)

                                .build();

                ResearchPaper savedResearchPaper = journalRepository.save(researchPaper);

                journalRepository.deleteById(savedResearchPaper.getId());

                Optional<Journal> deletedResearchPaper = journalRepository.findById(savedResearchPaper.getId());

                Assertions.assertThat(deletedResearchPaper).isEmpty();
        }
}
