package edu.bethlehem.scinexus.DatabaseLoading;

import edu.bethlehem.scinexus.Academic.Academic;
import edu.bethlehem.scinexus.Academic.AcademicRepository;
import edu.bethlehem.scinexus.Academic.Position;
import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Article.ArticleRepository;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.User.UserRepository;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.Organization.OrganizationRepository;
import edu.bethlehem.scinexus.Organization.OrganizationType;
import edu.bethlehem.scinexus.ResearchPaper.ResearchLanguage;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperRepository;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AcademicRepository academicRepository;
    private final ArticleRepository articleRepository;

    private final ResearchPaperRepository researchPaperRepository;
    private final OrganizationRepository organizationRepository;
    private final RandomDataGenerator dataGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        generateRandomUsers(10);
        generateLinks();
        generateResearchPapers();
        generateArticles();

    }

    private void generateLinks() {
        // Code Does not work.......
        // it works nvm

        List<User> users = userRepository.findAll();

        Random random = new Random();

        for (User user : users) {
            for (int i = 0; i < 5; i++) {
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

    }

    private void generateRandomUsers(int count) {
        List<Academic> academics = new ArrayList<>();
        List<Organization> organizations = new ArrayList<>();
        Set<String> usedUsernames = new HashSet<>();

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
            if (role == Role.ACADEMIC)
                academics.add(Academic.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .username(username)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .bio(bio)
                        .phoneNumber(phoneNumber)
                        .fieldOfWork(fieldOfWork)
                        .role(role)
                        .position(Position.PROFESSOR)
                        .education(bio)
                        .badge(bio)
                        .build());

            else
                organizations.add(Organization.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .username(username)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .bio(bio)
                        .phoneNumber(phoneNumber)
                        .fieldOfWork(fieldOfWork)
                        .role(role)
                        .type(OrganizationType.GOVERNMENT)
                        .build());

        }
        academicRepository.saveAll((List<Academic>) academics);
        organizationRepository.saveAll((List<Organization>) organizations);

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
        List<Article> articlesList = new ArrayList<>();
        Random random = new Random();
        for (User user : users) {
            for (int i = 0; i < 5; i++) {
                Article article = new Article(dataGenerator.generateRandomUniversityName(),
                        dataGenerator.generateRandomWords(), dataGenerator.generateRandomFieldOfWork(), user);
                System.out.println(user.getUsername());
                article.setInteractionCount(random.nextInt(1000));
                article.setOpinionCount(random.nextInt(1000));
                article.setVisibility(dataGenerator.generateRandomVisibility());
                article.setDescription(dataGenerator.generateRandomWords());
                article.setPublisher(user);
                user.getJournals().add(article);

                articlesList.add(article);
            }
        }
        articleRepository.saveAll(articlesList);
        userRepository.saveAll(users);
    }

    private void generateResearchPapers() {
        List<User> users = userRepository.findAll();
        List<ResearchPaper> researchPapersList = new ArrayList<>();
        Random random = new Random();
        for (User user : users) {
            for (int i = 0; i < 5; i++) {

                ResearchPaper researchPaper = new ResearchPaper(dataGenerator.generateRandomUniversityName(),
                        dataGenerator.generateRandomWords(), dataGenerator.generateRandomFieldOfWork(), user);
                System.out.println(user.getUsername());
                researchPaper.setLanguage(ResearchLanguage.ENGLISH);
                researchPaper.setNoOfPages(random.nextInt(500));
                researchPaper.setInteractionCount(random.nextInt(1000));
                researchPaper.setOpinionCount(random.nextInt(1000));
                researchPaper.setVisibility(dataGenerator.generateRandomVisibility());
                researchPaper.setDescription(dataGenerator.generateRandomWords());
                researchPaper.setPublisher(user);

                user.getJournals().add(researchPaper);
                researchPapersList.add(researchPaper);
            }
        }
        userRepository.saveAll(users);
        researchPaperRepository.saveAll(researchPapersList);
        // List<ResearchPaper> researchPapers = researchPaperRepository.findAll();

        // for (ResearchPaper researchPaper : researchPapers)
        // researchPaper.setPublisher(users.get(random.nextInt(users.size())));

        // researchPaperRepository.saveAll(researchPapers);
    }
}
