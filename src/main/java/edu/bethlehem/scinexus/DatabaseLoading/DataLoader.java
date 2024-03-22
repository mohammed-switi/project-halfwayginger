package edu.bethlehem.scinexus.DatabaseLoading;

import edu.bethlehem.scinexus.Academic.Academic;
import edu.bethlehem.scinexus.Academic.AcademicRepository;
import edu.bethlehem.scinexus.Academic.Position;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.Organization.OrganizationRepository;
import edu.bethlehem.scinexus.Organization.OrganizationType;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserRepository;
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
    private final OrganizationRepository organizationRepository;
    private final RandomDataGenerator dataGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<List> users = generateRandomUsers(100);
        academicRepository.saveAll((List<Academic>) users.get(0));
        organizationRepository.saveAll((List<Organization>) users.get(1));
        generateLinks();

    }

    private void generateLinks() {
        // Code Does not work.......
        // it works nvm

        List<User> users = userRepository.findAll();

        Random random = new Random();

        for (User user : users) {
            for (int i = 0; i < 5; i++) {

                if (random.nextInt(2) == 0) {

                    user.getLinks().add(users.get(random.nextInt(users.size())));
                } else
                    user.getLinks().add(users.get(random.nextInt(users.size())));
            }
        }
        userRepository.saveAll(users);

    }

    private List<List> generateRandomUsers(int count) {
        List<Academic> academics = new ArrayList<>();
        List<Organization> organizations = new ArrayList<>();
        List<List> users = new ArrayList<>();
        users.add(academics);
        users.add(organizations);
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

        return users;
    }

    private String generateUniqueUsername(Set<String> usedUsernames) {
        String username = dataGenerator.generateRandomUsername();
        while (usedUsernames.contains(username)) {
            username = dataGenerator.generateRandomUsername();
        }
        usedUsernames.add(username);
        return username;
    }

}
