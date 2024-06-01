package edu.bethlehem.scinexus.DatabaseLoading;

import com.github.javafaker.Faker;
import com.google.common.util.concurrent.CycleDetectingLockFactory.Policies;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Interaction.InteractionType;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.User.Role;
import jakarta.persistence.criteria.From;
import edu.bethlehem.scinexus.User.Position;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.checkerframework.checker.units.qual.s;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Rand;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

@Component
public class RandomDataGenerator {
    private final String[] titels = new String[] {
            "Agile Governance, Digital Transformation, and Citizen Satisfaction Moderated by Political Stability in Indonesia’s Socio-Political Landscape",
            "Analyzing the Effect of Crime-Free Housing Policies on Completed Evictions Using Spatial First Differences",
            "COMPETITIVENESS INDICATORS OF SPORTS CITIES IN CENTRAL-EASTERN EUROPE",
            "Images of the Civil War generation", "WAYS TO ENGAGE WITH THE ACADEMY",
            "From chalkboards to AI-powered learning", "From Gates to Grids",
            "Improving knowledge gain and emotional experience in online learning with knowledge and emotional scaffolding-based conversational agent",
            "Editorial N.º 91 / Editorial issue 91", "Migration and Innovation",
            "Optimizing Surveillance Satellites for the Synthetic Theater Operations Research Model",
            "Publishing in ESA Journals Supports Critical Programs for Science Communication, Training, and Diversity",
            "CHALLENGES IN THE DEVELOPMENT OF AN URBAN RANKING SYSTEM",
            "Instrucciones para los autores - Instructions for authors", "CONTRIBUTORS",
            "KNOWLEDGE SPILLOVERS AND SCIENCE PARKS",
            "Estudio bibliométrico de la producción científica de educación musical en España (1978-2022) - Bibliometric study of the scientific production of music education in Spain (1978-2022)",
            "Exploring learners’ learning performance, knowledge construction, and behavioral patterns in online asynchronous discussion using guidance scaffolding in visual imagery education",
            "UNIVERSITY TECHNOLOGICAL OUTPUT AND INDUSTRIAL SPECIALIZATION IN ITALIAN REGIONS",
            "INTRODUCTION TO THE AES SPECIAL ISSUE",
            "The Importance of Diversity in the Physician Assistant/Associate Workforce",
            "DOES LAB FUNDING MATTER FOR THE TECHNOLOGICAL APPLICATION OF SCIENTIFIC RESEARCH? AN EMPIRICAL ANALYSIS OF FRENCH LABS",
            "How to Report Systematic Literature Reviews in Management Using SyReMa",
            "MORS Loses a Good Man and Good Friend", "ONLINE", "The impact of double majors during economic downturns",
            "The Rhythm of Government",
            "The Role of Social Media and Innovation in Mexican Industrial Entrepreneurship",
            "INSTRUCCIONES PARA COLABORADORES - GUIDANCE FOR CONTRIBUTORS", "A WORD",
            "Does Discrimination in Childhood Reduce Trust and Participation Among Adult Immigrants?", "Briefly Noted",
            "RESEARCH GRANTS AND SCIENTISTS’ INVENTIONS", "Dr. Peter Perla",
            "Impact of Organizational Structure and Culture on E-Government Implementation",
            "PROXIMITY OF FIRMS TO SCIENTIFIC PRODUCTION",
            "Roles and functionalities of ChatGPT for students with different growth mindsets",
            "Beyond Economic Migration: Social, Historical, and Political Factors in US Immigration",
            "Interested in writing a book review?", "Government Data of the People, by the People, for the People",
            "Language and Acculturation",
            "Adapting an Organizational Culture to Stay Abreast of Technological Changes in Indonesia’s Government Scientific Sector" };
    private final String[] keyPhrases = new String[] {
            "agile governance", "citizen satisfaction", "digital transformation", "inclusive decision", "inclusive",
            "inclusive decision making", "political stability", "public services", "public", "public administration",
            "evictions", "rental units", "free housing", "certified rental", "crime free housing", "housing policies",
            "block groups", "cfhp certified", "completed evictions", "nuisance ordinances",
            "sports", "empirical researches", "cities", "sports cities", "competitiveness indicators", "health",
            "theoretical and empirical", "sports facilities", "urban management", "urban theoretical",
            "mike fitzpatrick", "virginia", "visite", "fitzpatrick observes", "visite tells", "pencil inscription",
            "citizenry images", "regiment matched", "fitzpatrick collection", "owner mike",
            "academy", "members", "business youtubecom", "laurie mcdonough", "youtube panels", "mcdonough morton",
            "youtubecom americanacad", "mandel director", "videos", "media videos",
            "privacy concerns", "students", "perceived", "perceived usefulness", "personal innovativeness",
            "internet privacy", "technology", "perceived enjoyment", "technology acceptance", "perceived ease",
            "terrorism", "terrorism studies", "terrorism research", "emerging researchers", "ranstorp mapping",
            "talking stagnation", "ranstorp", "morrison talking", "andrew silke", "primary data",
            "learners", "emotional experience", "knowledge", "scaffolding", "learning", "knowledge gain",
            "positive emotion", "epistemic", "negative emotion", "knowledge transfer",
            "innovar", "castañeda rodríguez", "publicación", "editorial", "semanas", "colombia", "revista innovar",
            "ciencia tecnología", "innovación mayo", "ciencias económicas",
            "inventors", "migrant inventors", "patent", "trademark office", "patent trademark", "lissoni", "countries",
            "inventor data", "francesco lissoni", "cooperation treaty",
            "grid cells", "priority", "operations research", "medium priority", "storm heuristic", "priority grid",
            "surveillance satellites", "low priority", "optimizing surveillance", "resolution",
            "esa journals", "publishing", "ecological", "limnology oceanography", "limnology", "urnals onlin",
            "sajo urnals", "oceanography letters", "underrepresented groups", "commentaries",
            "urban ranking", "cities", "empirical researches", "ranking system", "ranking systems", "indicators",
            "challenges", "urban management", "urban ranking system", "theoretical and empirical",
            "los autores", "artículo", "revista", "revista española", "pedagogía", "referencias bibliográficas",
            "traducción", "deberá incluirse", "los artículos", "pedagogía año",
            "minnesota", "dave kenney", "howard zahniser", "ginny way", "laurie hertzel", "minnesota history",
            "star tribune", "jennifer huebscher", "linda koutsky", "foshay tower",
            "spillovers", "knowledge spillovers", "patents", "citations", "citing patents", "science parks",
            "listed firms", "non stp", "tier cities", "boris lokshin",
            "educación", "educación musical", "artículos", "revistas", "producción científica", "los artículos",
            "pedagogía año", "análisis", "revista española", "autores",
            "ftc model", "guidance scaffolding", "knowledge construction", "photographic", "learning",
            "photographic works", "students", "experimental group", "model guidance", "ftc model guidance",
            "specialization", "patent", "technological", "industrial specialization", "universities", "patent families",
            "università degli", "degli studi", "technological specialization", "provinces",
            "spillovers", "knowledge spillovers", "annales d’economie", "technological frontier", "statistiques",
            "funding", "research", "grant funding", "johana etner", "economics",
            "certi cation", "workforce", "physician", "physician assistant", "diversity", "hispanic", "latino",
            "healthcare", "accessed", "medical boards",
            "technological frontier", "funding", "grants", "grant funding", "researchers", "grants per", "lionel nesta",
            "guillou lionel", "publications", "competitive grant",
            "innovar", "review", "systematic", "management", "literature", "research", "revisión sistemática",
            "studies", "literature review", "revisiones sistemáticas",
            "jennifer ferat", "liz marriott", "wargaming special", "lisa oakley", "marriott tom", "mors loses",
            "mors events", "liz marriott tom", "wargaming special meeting", "regular participants",
            "linkedin", "american academy", "arts sciences", "uses linkedin", "linkedin recently", "linkedin american",
            "honoring excellence", "divides advancing", "sciences honoring", "academy followers",
            "double majors", "unrelated fields", "economic downturns", "single majors", "skills", "college graduates",
            "bls apri", "skills and knowledge", "percent", "percent reduction",
            "attention", "provincial governments", "government", "policy", "executive meetings", "central",
            "policy areas", "government attention", "attention allocation", "local governments",
            "innovation", "entrepreneurship", "social", "lópez lemus", "industrial entrepreneurship", "products",
            "networks", "product innovation", "entrepreneurs", "business",
            "deben enviarse", "los artículos", "investigación económica", "los autores", "artículo", "las citas",
            "deberán", "autores recibirán", "revista", "criterios establecidos",
            "australian", "australian institute", "wklv zdv", "zdv wkh", "aips protections", "maria kavallaris",
            "australian quarterly", "social cohesion", "fox juggernauts", "whistleblower",
            "discrimination", "social trust", "institutional trust", "immigrants", "participation", "attachment",
            "nordic", "migration research", "minority", "generalized trust",
            "derek gray", "declan cullen", "archivist derek", "library archivist", "uence tech", "biggest drawback",
            "study spotlights", "cullen princeton", "compelling assemblage", "index hardcover",
            "funding", "applicants", "inventions", "patents", "directed programs", "elodie carpentier",
            "funded applicants", "pascale roux", "projects", "researchers",
            "annandale virginia", "perla leader", "wife joann", "analytics", "peter perla", "perla family",
            "analytics easy", "analytics make", "make analytics", "sara wife",
            "government", "organizational", "implementation", "government implementation", "innovar",
            "organizational structure", "organizational culture", "yogyakarta", "obeying social",
            "significantly influences",
            "proximity", "scientific", "science", "patents", "laboratories", "spillovers", "computer science",
            "industries", "engineering", "nursing paramedical",
            "learning", "hgm group", "growth mindsets", "students", "functionalities", "roles and functionalities",
            "learning activities", "different growth mindsets", "learners", "conceptions",
            "migration", "economic migration", "immigration", "mahmud hasan", "migration research", "nordic",
            "transnationalism", "hasan eds", "hasan mahmud", "beyond economic",
            "psb suite", "telecommunications relay", "services psb", "relay service", "youve read",
            "massachusetts avenue", "economics topic", "marketing services psb", "services psb suite",
            "telecommunications relay service",
            "privacy", "data privacy", "privacy loss", "formal privacy", "claire mckay", "data users", "mckay bowen",
            "privacy methods", "formally private", "loss budget",
            "latvian", "russian", "language", "acculturation", "finnish", "russian speakers", "language proficiency",
            "proficiency", "siitonen", "siitonen nordic",
            "digital literacy", "innovation resilience", "government science", "communication policies",
            "researchers digital", "government scientific", "literacy and skills", "researchers digital literacy",
            "research technologies", "scientific"

    };
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

    public String generateLongRandomWords() {
        return generateRandomString(1000, 10000,
                LOWERCASE_CHARACTERS + UPPERCASE_CHARACTERS);
    }

    public String test() {
        return faker.educator().course();

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

    public Position getRandomPosition() {
        Position[] positions = Position.values();
        Random random = new Random();
        int randomIndex = random.nextInt(positions.length);
        return positions[randomIndex];
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

    public String generateMaxLorem(int maxLength) {
        return faker.lorem().paragraph(maxLength);
    }

    public String generateRandomAvatarLink() {
        Random random = new Random();
        String[] avatars = { "Sasha", "Bailey", "Kitty", "Annie", "Leo", "Chloe", "Simba", "Milo", "Gizmo", "Harley" };
        String link = "https://api.dicebear.com/8.x/adventurer-neutral/svg?seed="
                + avatars[random.nextInt(avatars.length)]
                + "&backgroundColor=ecad80,f2d3b1,b6e3f4,c0aede,d1d4f9&backgroundType=gradientLinear";
        return link;
    }

    public String getRandomTitle() {
        Random random = new Random();
        int randomIndex = random.nextInt(titels.length);
        return titels[randomIndex];
    }

    public String getRandomKeyPhrase() {
        Random random = new Random();
        int randomIndex = random.nextInt(keyPhrases.length);
        return keyPhrases[randomIndex];
    }

}
