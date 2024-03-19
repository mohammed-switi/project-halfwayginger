package edu.bethlehem.scinexus.DatabaseLoading;

import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.Organization.OrganizationType;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RandomDataGenerator dataGenerator;
    private final PasswordEncoder passwordEncoder;



    @Override
    public void run(String... args) {
        List<User> users = generateRandomUsers(100);
        userRepository.saveAll(users);
    }

    private List<User> generateRandomUsers(int count) {
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
            Role role = Role.ORGANIZATION; // Assuming role is constant for all users

            User user = Organization.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .bio(bio)
                    .phoneNumber(phoneNumber)
                    .fieldOfWork(fieldOfWork)
                    .role(role)
                    .build();
            ((Organization) user).setType(OrganizationType.GOVERNMENT);
            users.add(user);
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
