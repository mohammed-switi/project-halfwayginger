package edu.bethlehem.scinexus.DatabaseLoading;

import com.github.javafaker.Faker;

import edu.bethlehem.scinexus.Interaction.InteractionType;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.User.Role;

import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Rand;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

@Component
public class RandomDataGenerator {

    private final SecureRandom secureRandom = new SecureRandom();
    private final Faker faker = new Faker();

    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC_CHARACTERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+";

    private static final int MIN_USERNAME_LENGTH = 4;
    private static final int MAX_USERNAME_LENGTH = 15;

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;

    public String generateRandomUsername() {
        return generateRandomString(MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH, LOWERCASE_CHARACTERS);
    }

    public String generateRandomFirstName() {
        return faker.name().firstName();
    }

    public String generateRandomLastName() {

        return faker.name().lastName();
    }

    public String generateRandomUniversityName() {
        return faker.university().name();
    }

    public String generateRandomEmail(String firstName, String lastName) {
        return faker.internet().emailAddress();
    }

    public Boolean generateRandomIsVerified() {
        return faker.bool().bool();
    }

    public String generateRandomPassword() {
        return generateRandomString(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH,
                LOWERCASE_CHARACTERS + UPPERCASE_CHARACTERS + NUMERIC_CHARACTERS + SPECIAL_CHARACTERS);
    }

    public String generateRandomWords() {
        return generateRandomString(100, 100,
                LOWERCASE_CHARACTERS + UPPERCASE_CHARACTERS);
    }

    public String generateRandomBio() {
        return faker.backToTheFuture().character();
    }

    public String generateRandomPhoneNumber() {
        String phoneNumber = faker.phoneNumber().cellPhone().toString();
        return phoneNumber;
    }

    public String generateRandomRealUsername() {
        return faker.name().username();
    }

    public String generateRandomCharacterName() {
        return faker.gameOfThrones().character();
    }

    public String generateRandomFieldOfWork() {
        return faker.job().field();
    }

    public Visibility generateRandomVisibility() {
        int randomIndex = secureRandom.nextInt(Visibility.values().length);
        return Visibility.values()[randomIndex];

    }

    public Role generateRandomRole() {
        Random random = new Random();

        if (random.nextInt(2) == 1) {
            return Role.ACADEMIC;
        } else {
            return Role.ORGANIZATION;
        }

    }

    public InteractionType generateRandomInteractionType() {
        Random random = new Random();
        int randomIndex = random.nextInt(InteractionType.values().length);
        return InteractionType.values()[randomIndex];

    }

    private String generateRandomString(int minLength, int maxLength, String characters) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = minLength + secureRandom.nextInt(maxLength - minLength + 1);
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            stringBuilder.append(characters.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

    public String generateRandomAvatarLink() {
        Random random = new Random();
        String[] avatars = { "Sasha", "Bailey", "Kitty", "Annie", "Leo", "Chloe", "Simba", "Milo", "Gizmo", "Harley" };
        String link = "https://api.dicebear.com/8.x/adventurer-neutral/svg?seed="
                + avatars[random.nextInt(avatars.length)]
                + "&backgroundColor=ecad80,f2d3b1,b6e3f4,c0aede,d1d4f9&backgroundType=gradientLinear";
        return link;
    }

}
