package edu.bethlehem.scinexus.DatabaseLoading;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.JPARepository.ArticleRepository;
import edu.bethlehem.scinexus.JPARepository.InteractionRepository;
import edu.bethlehem.scinexus.JPARepository.JournalRepository;
import edu.bethlehem.scinexus.JPARepository.MediaRepository;
import edu.bethlehem.scinexus.JPARepository.OpinionRepository;
import edu.bethlehem.scinexus.JPARepository.PostRepository;
import edu.bethlehem.scinexus.JPARepository.ResearchPaperRepository;
import edu.bethlehem.scinexus.JPARepository.UserLinksRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.ResearchPaper.ResearchLanguage;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.User.OrganizationType;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.UserLinks.UserLinks;
import edu.bethlehem.scinexus.UserLinks.UserLinksService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final ArticleRepository articleRepository;
    private final MediaRepository mediaRepository;

    private final ResearchPaperRepository researchPaperRepository;
    private final JournalRepository journalRepository;
    private final OpinionRepository opinionRepository;
    private final InteractionRepository interactionRepository;
    private final UserLinksRepository userLinksRepository;
    private final UserLinksService ulService;
    Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private final PostRepository postRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private final RandomDataGenerator dataGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        logger.trace("Starting Random Data Generation: \n\n");
        long startTime = System.currentTimeMillis();
        generateMedia();
        generateUser();
        generateRandomUsers(5);
        generateLinks();
        generateResearchPapers();
        generateArticles();
        generatePosts();
        generateOpinions();
        generateInteractions();
        generateRandomContributionsAndValidations();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        logger.info("Random Data Generation Completed with time of " + totalTime + " milliseconds");
    }

    public User generateUserAndSaveIt() {
        logger.debug("Generating User and Saving it: \n\n");
        User user = new User();
        String username = dataGenerator.generateRandomRealUsername();
        user.setFirstName(dataGenerator.generateRandomFirstName());
        user.setLastName(dataGenerator.generateRandomLastName());
        user.setUsername(username);
        user.setEmail(dataGenerator.generateRandomEmail(username, dataGenerator.generateRandomCharacterName()));
        user.setPassword(passwordEncoder.encode("Mohammed1234!"));
        user.setPhoneNumber(dataGenerator.generateRandomPhoneNumber());
        user.setRole((Math.random() < 0.5) ? Role.ACADEMIC : Role.ORGANIZATION);
        user.setType(OrganizationType.BUSINESS);

        return userRepository.save(user);
    }

    public void generateUser() {
        logger.debug("Generating base User and base Article: \n\n");
        User user = new User();
        user.setFirstName("Mohammed");
        user.setLastName("Switi");
        user.setUsername("Aaboduh");
        user.setEmail("obada@gmail.com");
        user.setPassword(passwordEncoder.encode("Mohammed1234!"));
        user.setPhoneNumber("123.456.7890");
        user.setRole(Role.ADMIN);
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
        // List<UserLinks> uls = new ArrayList<UserLinks>();
        Random random = new Random();

        logger.trace("Generating Random Users' links for " + users.size() + " users");
        for (User linkFrom : users) {
            for (int i = 0; i < 5; i++) {
                User linkTo = users.get(random.nextInt(users.size()));

                if (!ulService.areTheyLinked(linkFrom, linkTo) && linkFrom != linkTo) {
                    UserLinks ul = new UserLinks(linkTo, linkFrom);
                    if (random.nextInt(2) == 1)
                        ul.setAccepted(true);
                    userLinksRepository.save(ul);
                }
            }
        }
        // userLinksRepository.saveAll(uls);
        userRepository.saveAll(users);
        logger.debug("Saved Users' links for " + users.size() + " users");

    }

    private void generateRandomUsers(int count) {
        logger.debug("Generating Random Users: \n\n");
        List<User> users = new ArrayList<>();
        Set<String> usedUsernames = new HashSet<>();
        // List<Media> medias = mediaRepository.findAll();
        // Random r = new Random();
        logger.trace("Generating Random User: " + count + " users will be generated");
        for (int i = 0; i < count; i++) {

            String username = generateUniqueUsername(usedUsernames);
            String firstName = dataGenerator.generateRandomFirstName();
            String universityName = dataGenerator.generateRandomUniversityName();
            String lastName = dataGenerator.generateRandomLastName();
            String email = dataGenerator.generateRandomEmail(firstName, lastName);
            String password = dataGenerator.generateRandomPassword();
            String bio = dataGenerator.generateRandomBio();
            String phoneNumber = dataGenerator.generateRandomPhoneNumber();
            String fieldOfWork = dataGenerator.generateRandomFieldOfWork();
            Media media = new Media("svg", dataGenerator.generateRandomAvatarLink());
            media.setFileName(dataGenerator.generateRandomAvatarLink());
            media = mediaRepository.save(media);

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
                academic.setProfilePicture(media);
                academic.setPosition(dataGenerator.getRandomPosition());
                // academic.setProfileCover((medias.get(r.nextInt(medias.size()))));
                users.add(academic);
            }

            else {

                User organization = new User();
                organization.setRole(Role.ORGANIZATION);
                organization.setFirstName(universityName);
                organization.setLastName(lastName);
                organization.setUsername(username);
                organization.setEmail(email);
                organization.setPassword(passwordEncoder.encode(password));
                organization.setBio(bio);
                organization.setPhoneNumber(phoneNumber);
                organization.setFieldOfWork(fieldOfWork);
                organization.setProfilePicture(media);
                // organization.setProfileCover((medias.get(r.nextInt(medias.size()))));
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
        List<User> users = userRepository.findAllByRole(Role.ACADEMIC);
        List<User> organizations = userRepository.findAllByRole(Role.ORGANIZATION);
        List<ResearchPaper> researchPapers = new ArrayList<>();
        logger.trace("generating two Research Papers for " + users.size() + " users each");
        Random random = new Random();
        for (User user : users) {
            for (int i = 0; i < 6; i++) {

                ResearchPaper researchPaper = new ResearchPaper(dataGenerator.generateRandomFieldOfWork(),
                        dataGenerator.generateRandomWords(), dataGenerator.generateRandomBio(),
                        dataGenerator.generateRandomFieldOfWork(), user);
                researchPaper.setLanguage(ResearchLanguage.ENGLISH);
                researchPaper.setNoOfPages(random.nextInt(500));
                researchPaper.setInteractionsCount(0);
                researchPaper.setOpinionsCount(0);
                researchPaper.setVisibility(dataGenerator.generateRandomVisibility());
                researchPaper.setDescription(dataGenerator.generateRandomWords());
                user.addJournal(researchPaper);
                for (int j = 0; j < random.nextInt(3); j++) {
                    User organization = organizations.get(Math.abs(random.nextInt(organizations.size())));
                    researchPaper.addValidatedBy(organization);
                }
                researchPapers.add(researchPaper);
            }
        }
        logger.trace(researchPapers.size() + " Research Papers generated");
        // userRepository.saveAll(users);
        researchPaperRepository.saveAll(researchPapers);

    }

    private void generatePosts() {
        List<User> users = userRepository.findAll();
        List<Post> posts = new ArrayList<>();
        for (User user : users) {
            for (int i = 0; i < 2; i++) {

                Post post = new Post(dataGenerator.generateRandomWords(), user);
                post.setInteractionsCount(0);
                post.setOpinionsCount(0);
                post.setVisibility(dataGenerator.generateRandomVisibility());
                user.addJournal(post);

                posts.add(post);
            }
        }
        // userRepository.saveAll(users);
        postRepository.saveAll(posts);

    }

    private void generateOpinions() {
        logger.debug("Starting to generate Opinions: \n\n");

        List<Journal> journals = journalRepository.findAll();
        List<User> users = userRepository.findAll();
        List<Opinion> opinions = new ArrayList<>();
        Random random = new Random();
        for (Journal journal : journals) {
            for (int j = 0; j < random.nextInt(5); j++) {
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
            for (int j = 0; j < random.nextInt(5); j++) {
                Opinion reOpinion = new Opinion(dataGenerator.generateRandomWords(), opinion.getJournal(),
                        users.get(random.nextInt(users.size())));

                reOpinion.setPapaOpinion(opinion);
                opinion.addOpinion();
                opinion.getJournal().addOpinion();
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
            for (int j = 0; j < random.nextInt(5); j++) {

                Interaction interaction = new Interaction(
                        dataGenerator.generateRandomInteractionType(), users.get(random.nextInt(users.size())),
                        journal);
                journal.addInteraction();
                interactions.add(interaction);
            }
        }
        logger.trace("generaterd " + interactions.size() + " Interactions for Journals");

        for (Opinion opinion : opinions) {
            for (int j = 0; j < random.nextInt(5); j++) {

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

    public void generateRandomContributionsAndValidations() {
        List<User> users = userRepository.findAll();
        List<ResearchPaper> researchPapers = researchPaperRepository.findAll();
        Random random = new Random();
        for (User user : users) {
            for (int i = 0; i < random.nextInt(10); i++) {
                ResearchPaper journal = researchPapers.get(random.nextInt(researchPapers.size()));
                // if (user.getRole() == Role.ACADEMIC)

                user.addContributedJournal(journal);
                user.addValidatedJournal(journal);
                journal.addValidatedBy(user);
            }
        }
        userRepository.saveAll(users);
        // journalRepository.saveAll(researchPapers);
    }

    public void generateMedia() {
        List<Media> medias = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Media media = new Media("svg", dataGenerator.generateRandomAvatarLink());
            media.setFileName(dataGenerator.generateRandomAvatarLink());
            medias.add(media);
        }
        mediaRepository.saveAll(medias);
    }
}
