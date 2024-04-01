package edu.bethlehem.scinexus.DatabaseLoading;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.JPARepository.ArticleRepository;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.JPARepository.InteractionRepository;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.JPARepository.JournalRepository;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.JPARepository.OpinionRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import edu.bethlehem.scinexus.ResearchPaper.ResearchLanguage;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.JPARepository.ResearchPaperRepository;
import edu.bethlehem.scinexus.User.OrganizationType;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// [...]

import java.util.*;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final ArticleRepository articleRepository;

    private final ResearchPaperRepository researchPaperRepository;
    private final JournalRepository journalRepository;
    private final OpinionRepository opinionRepository;
    private final InteractionRepository interactionRepository;
    Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final RandomDataGenerator dataGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        logger.trace("Starting Random Data Generation: \n\n");
        long startTime = System.currentTimeMillis();
        generateUser();
        generateRandomUsers(3);
        generateLinks();
        generateResearchPapers();
        generateArticles();
        generateOpinions();
        generateInteractions();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        logger.info("Random Data Generation Completed with time of " + totalTime + " milliseconds");
    }

    private void generateUser() {
        logger.debug("Generating base User and base Article: \n\n");
        User user = new User();
        user.setFirstName("Mohammed");
        user.setLastName("Switi");
        user.setUsername("Aaboduh");
        user.setEmail("obada@gmail.com");
        user.setPassword(passwordEncoder.encode("Mohammed1234!"));
        user.setPhoneNumber("123.456.7890");
        user.setRole(Role.ORGANIZATION);
        user.setType(OrganizationType.BUSINESS);

        logger.debug("Generating Base User with " + user.toString());
        user = userRepository.save(user);
        Article article = new Article(dataGenerator.generateRandomUniversityName(),
                dataGenerator.generateRandomWords(), dataGenerator.generateRandomFieldOfWork(), user);
        article.setInteractionsCount(0);
        article.setOpinionsCount(0);
        article.setVisibility(dataGenerator.generateRandomVisibility());
        article = articleRepository.save(article);
        user.addJournal(article);
        logger.debug("Generating Base Article with " + article.toString());

        userRepository.save(user);
    }

    private void generateLinks() {
        logger.debug("Generating Random Users' links: \n\n");
        // Code Does not work.......
        // it works nvm

        List<User> users = userRepository.findAll();

        Random random = new Random();

        logger.trace("Generating Random Users' links for " + users.size() + " users");
        for (User user : users) {
            for (int i = 0; i < 3; i++) {
                User linkTo = users.get(random.nextInt(users.size()));
                if (!user.getLinks().contains(linkTo) && user != linkTo) {

                    if (random.nextInt(2) == 0)
                        user.getLinks().add(linkTo);
                    else
                        user.getLinks().add(linkTo);
                }
            }
        }
        userRepository.saveAll(users);
        logger.debug("Saved Users' links for " + users.size() + " users");

    }

    private void generateRandomUsers(int count) {
        logger.debug("Generating Random Users: \n\n");
        List<User> users = new ArrayList<>();
        Set<String> usedUsernames = new HashSet<>();

        logger.trace("Generating Random User:" + count + " users will be generated");
        for (int i = 0; i < count; i++) {
            String username = generateUniqueUsername(usedUsernames);
            String firstName = dataGenerator.generateRandomFirstName();
            String lastName = dataGenerator.generateRandomLastName();
            String email = dataGenerator.generateRandomEmail(firstName, lastName);
            String password = dataGenerator.generateRandomPassword();
            String bio = dataGenerator.generateRandomBio();
            String phoneNumber = dataGenerator.generateRandomPhoneNumber();
            String fieldOfWork = dataGenerator.generateRandomFieldOfWork();

            Role role = dataGenerator.generateRandomRole(); // Assuming role is constant for all users
            if (role == Role.ACADEMIC) {
                User academic = new User();
                academic.setRole(Role.ACADEMIC);
                academic.setFirstName(firstName);
                academic.setLastName(lastName);
                academic.setUsername(username);
                academic.setEmail(email);
                academic.setPassword(passwordEncoder.encode(password));
                academic.setBio(bio);
                academic.setPhoneNumber(phoneNumber);
                academic.setFieldOfWork(fieldOfWork);
                academic.setPosition(Position.PROFESSOR);
                academic.setEducation(bio);
                academic.setBadge(bio);
                users.add(academic);
            }

            else {

                User organization = new User();
                organization.setRole(Role.ORGANIZATION);
                organization.setFirstName(firstName);
                organization.setLastName(lastName);
                organization.setUsername(username);
                organization.setEmail(email);
                organization.setPassword(passwordEncoder.encode(password));
                organization.setBio(bio);
                organization.setPhoneNumber(phoneNumber);
                organization.setFieldOfWork(fieldOfWork);

                organization.setType(OrganizationType.BUSINESS);
                organization.setVerified(true);

                users.add(organization);
            }
            users = userRepository.saveAll(users);
            logger.debug("Saved " + users.size() + " reandom users");

        }
    }

    private String generateUniqueUsername(Set<String> usedUsernames) {
        String username = dataGenerator.generateRandomUsername();
        while (usedUsernames.contains(username)) {
            username = dataGenerator.generateRandomUsername();
        }
        usedUsernames.add(username);
        return username;
    }

    private void generateArticles() {
        List<User> users = userRepository.findAll();
        List<Article> articles = new ArrayList<>();
        logger.trace("generating two Articles for " + users.size() + " users each");
        for (User user : users) {
            for (int i = 0; i < 2; i++) {
                Article article = new Article(dataGenerator.generateRandomUniversityName(),
                        dataGenerator.generateRandomWords(), dataGenerator.generateRandomFieldOfWork(), user);
                article.setInteractionsCount(0);
                article.setOpinionsCount(0);
                article.setVisibility(dataGenerator.generateRandomVisibility());

                user.addJournal(article);
                articles.add(article);

            }
        }

        // userRepository.saveAll(users);

        articleRepository.saveAll(articles);
        logger.debug(articles.size() + " Articles generated and saved successfully");

    }

    private void generateResearchPapers() {
        List<User> users = userRepository.findAll();
        List<ResearchPaper> researchPapers = new ArrayList<>();
        logger.trace("generating two Research Papers for " + users.size() + " users each");
        Random random = new Random();
        for (User user : users) {
            for (int i = 0; i < 2; i++) {

                ResearchPaper researchPaper = new ResearchPaper(dataGenerator.generateRandomUniversityName(),
                        dataGenerator.generateRandomWords(), dataGenerator.generateRandomBio(),
                        dataGenerator.generateRandomFieldOfWork(), user);
                researchPaper.setLanguage(ResearchLanguage.ENGLISH);
                researchPaper.setNoOfPages(random.nextInt(500));
                researchPaper.setInteractionsCount(0);
                researchPaper.setOpinionsCount(0);
                researchPaper.setVisibility(dataGenerator.generateRandomVisibility());
                researchPaper.setDescription(dataGenerator.generateRandomWords());
                user.addJournal(researchPaper);

                researchPapers.add(researchPaper);
            }
        }
        logger.trace(researchPapers.size() + " Research Papers generated");
        // userRepository.saveAll(users);
        researchPaperRepository.saveAll(researchPapers);
        logger.debug(researchPapers.size() + " Research Papers saved");

    }

    private void generateOpinions() {
        logger.debug("Starting to generate Opinions: \n\n");

        List<Journal> journals = journalRepository.findAll();
        List<User> users = userRepository.findAll();
        List<Opinion> opinions = new ArrayList<>();
        Random random = new Random();
        for (Journal journal : journals) {
            for (int j = 0; j < random.nextInt(10); j++) {
                Opinion opinion = new Opinion(dataGenerator.generateRandomWords(), journal,
                        users.get(random.nextInt(users.size())));

                journal.addOpinion();
                opinions.add(opinion);
            }
        }

        logger.trace(opinions.size() + " Opinion were generated for journals");

        opinionRepository.saveAll(opinions);
        List<Opinion> opinions2 = opinionRepository.findAll();
        for (Opinion opinion : opinions) {
            for (int j = 0; j < random.nextInt(10); j++) {
                Opinion reOpinion = new Opinion(dataGenerator.generateRandomWords(), opinion.getJournal(),
                        users.get(random.nextInt(users.size())));

                reOpinion.setPapaOpinion(opinion);
                opinion.addOpinion();

                // reOpinion.addOpinion();
                opinions2.add(reOpinion);
            }
        }
        logger.trace(opinions2.size() + " Opinion were generated for other Opinions");
        journalRepository.saveAll(journals);
        logger.debug(journals.size() + " Journal saved successfully");
        opinionRepository.saveAll(opinions);
        logger.debug(opinions.size() + " Opinion generated and saved successfully");
        opinionRepository.saveAll(opinions2);
        logger.debug(opinions2.size() + " Opinion generated and saved successfully for another Opinion");
    }

    private void generateInteractions() {
        logger.debug("Starting to generate Interactions: \n\n");

        List<Journal> journals = journalRepository.findAll();
        List<Opinion> opinions = opinionRepository.findAll();
        List<User> users = userRepository.findAll();

        List<Interaction> interactions = new ArrayList<>();
        Random random = new Random();
        for (Journal journal : journals) {
            for (int j = 0; j < random.nextInt(10); j++) {

                Interaction interaction = new Interaction(
                        dataGenerator.generateRandomInteractionType(), users.get(random.nextInt(users.size())),
                        journal);
                journal.addInteraction();
                interactions.add(interaction);
            }
        }
        logger.trace("generaterd " + interactions.size() + " Interactions for Journals");

        for (Opinion opinion : opinions) {
            for (int j = 0; j < random.nextInt(10); j++) {

                Interaction interaction = new Interaction(
                        dataGenerator.generateRandomInteractionType(), users.get(random.nextInt(users.size())),
                        opinion);
                opinion.addInteraction();
                interactions.add(interaction);
            }
        }
        logger.trace("generaterd " + interactions.size() + " Interactions for Opinions");

        journalRepository.saveAll(journals);
        logger.debug(journals.size() + " journals saved successfully");
        opinionRepository.saveAll(opinions);
        logger.debug(opinions.size() + " opinions saved successfully");
        interactionRepository.saveAll(interactions);
        logger.debug(interactions.size() + " Interactions generated and saved successfully");
    }
}
