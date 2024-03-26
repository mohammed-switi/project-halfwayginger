package edu.bethlehem.scinexus.DatabaseLoading;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Article.ArticleRepository;

import edu.bethlehem.scinexus.User.UserRepository;

import edu.bethlehem.scinexus.ResearchPaper.ResearchLanguage;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperRepository;
import edu.bethlehem.scinexus.User.OrganizationType;
import edu.bethlehem.scinexus.User.Position;
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

    private final ArticleRepository articleRepository;

    private final ResearchPaperRepository researchPaperRepository;

    private final RandomDataGenerator dataGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        generateUser();
        generateRandomUsers(10);
        generateLinks();
        generateResearchPapers();
        generateArticles();

    }

    private void generateUser() {
        Random random = new Random();
        User user = new User();
        user.setFirstName("Mohammed");
        user.setLastName("Switi");
        user.setUsername("Aaboduh");
        user.setEmail("obada@gmail.com");
        user.setPassword(passwordEncoder.encode("Mohammed1234!"));
        user.setPhoneNumber("123.456.7890");
        user.setRole(Role.ORGANIZATION);
        user.setType(OrganizationType.BUSINESS);

        user = userRepository.save(user);
        Article article = new Article(dataGenerator.generateRandomUniversityName(),
                dataGenerator.generateRandomWords(), dataGenerator.generateRandomFieldOfWork(), user);
        System.out.println(user.getUsername());
        article.setInteractionCount(random.nextInt(1000));
        article.setOpinionCount(random.nextInt(1000));
        article.setVisibility(dataGenerator.generateRandomVisibility());
        article = articleRepository.save(article);
        user.addJournal(article);

        userRepository.save(user);
    }

    private void generateLinks() {
        // Code Does not work.......
        // it works nvm

        List<User> users = userRepository.findAll();

        Random random = new Random();

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

    }

    private void generateRandomUsers(int count) {
        List<User> users = new ArrayList<>();
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
            // for (User user : users) {
            // if (user.getRole() == Role.ACADEMIC) {
            // Academic academic = user.toAcademic();

            // academic.setPosition(Position.PROFESSOR);
            // academic.setEducation(dataGenerator.generateRandomWords());
            // academic.setBadge(dataGenerator.generateRandomWords());
            // academicRepository.save(academic);
            // } else {
            // Organization organization = user.toOrganization();
            // organization.setType(OrganizationType.BUSINESS);
            // organization.setVerified(true);
            // organizationRepository.save(organization);
            // }
            // }

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
        Random random = new Random();
        for (User user : users) {
            for (int i = 0; i < 2; i++) {
                Article article = new Article(dataGenerator.generateRandomUniversityName(),
                        dataGenerator.generateRandomWords(), dataGenerator.generateRandomFieldOfWork(), user);
                System.out.println(user.getUsername());
                article.setInteractionCount(random.nextInt(1000));
                article.setOpinionCount(random.nextInt(1000));
                article.setVisibility(dataGenerator.generateRandomVisibility());
                article = articleRepository.save(article);

                user.addJournal(article);
                userRepository.save(user);

            }
        }

    }

    private void generateResearchPapers() {
        List<User> users = userRepository.findAll();
        Random random = new Random();
        for (User user : users) {
            for (int i = 0; i < 2; i++) {

                ResearchPaper researchPaper = new ResearchPaper(dataGenerator.generateRandomUniversityName(),
                        dataGenerator.generateRandomWords(), dataGenerator.generateRandomBio(),
                        dataGenerator.generateRandomFieldOfWork(), user);
                System.out.println(user.getUsername());
                researchPaper.setLanguage(ResearchLanguage.ENGLISH);
                researchPaper.setNoOfPages(random.nextInt(500));
                researchPaper.setInteractionCount(random.nextInt(1000));
                researchPaper.setOpinionCount(random.nextInt(1000));
                researchPaper.setVisibility(dataGenerator.generateRandomVisibility());
                researchPaper.setDescription(dataGenerator.generateRandomWords());
                researchPaper = researchPaperRepository.save(researchPaper);
                user.addJournal(researchPaper);
                userRepository.save(user);
            }
        }

    }
}
