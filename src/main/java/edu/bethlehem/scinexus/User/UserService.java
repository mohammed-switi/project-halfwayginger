package edu.bethlehem.scinexus.User;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import edu.bethlehem.scinexus.JPARepository.*;
import edu.bethlehem.scinexus.UserLinks.UserLinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Article.ArticleModelAssembler;
import edu.bethlehem.scinexus.File.FileStorageService;
import edu.bethlehem.scinexus.JPARepository.ArticleRepository;
import edu.bethlehem.scinexus.JPARepository.MediaRepository;
import edu.bethlehem.scinexus.JPARepository.NotificationRepository;
import edu.bethlehem.scinexus.JPARepository.PostRepository;
import edu.bethlehem.scinexus.JPARepository.ResearchPaperRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Media.MediaNotFoundException;
import edu.bethlehem.scinexus.MongoRepository.UserMongoRepository;
import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.Notification.NotificationModelAssembler;
import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.Post.PostModelAssembler;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperModelAssembler;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository repository;
    private final UserMongoRepository mongoRepository;
    private final ArticleRepository articleRepository;
    private final MediaRepository mediaRepository;
    private final NotificationRepository notificationRepository;
    private final ResearchPaperRepository researchPapersRepository;
    private final PostRepository postRepository;
    private final ResearchPaperModelAssembler researchPapersAssembler;
    private final NotificationModelAssembler notificationAssembler;
    private final UserModelAssembler assembler;
    private final ArticleModelAssembler articleAssembler;
    private final PostModelAssembler postAssembler;
    private final UserLinksRepository userLinksRepository;
    private final UserDocumentService userDocumentService;

    @Autowired
    FileStorageService fileStorageManager;

    private final JwtService jwtService;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    // USING Java Persistence Query Language (JPQL), (Tested: Works Well!)
    // public boolean areUsersLinked(User user1, User user2) {
    // logger.debug("Checking if the users are linked");
    // Long count = entityManager.createQuery(
    // "SELECT COUNT(u) FROM User u JOIN u.links l " +
    // "WHERE u.id = :userId1 AND l.id = :userId2",
    // Long.class)
    // .setParameter("userId1", user1.getId())
    // .setParameter("userId2", user2.getId())
    // .getSingleResult();

    // return count > 0;
    // }

    public CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> users = repository.findAll().stream()
                .map(user -> assembler.toModel(user))
                .collect(Collectors.toList());
        logger.trace("Got all users");
        logger.debug("returning all users");
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    public EntityModel<User> one(Long userId) {
        logger.debug("returning user with id: " + userId);
        User user = entityManager.find(User.class, userId);
        logger.trace("Got user with id: " + userId);
        return assembler.toModel(user);
    }

    public EntityModel<User> updateUserPartially(UserRequestPatchDTO editUser, Authentication auth) {
        User user = jwtService.getUser(auth);
        logger.debug("partially updating user with id: " + user.getId());

        logger.trace("Got user with id: " + user.getId());

        try {
            for (Method method : UserRequestPatchDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(editUser);

                    if (value != null && !isEmptyValue(value)) {
                        String propertyName = method.getName().substring(3); // remove "get"
                        logger.trace("Updating user property: " + propertyName);
                        if (propertyName.equals("Class")) // Class is a reserved keyword in Java
                            continue;
                        Method setter;
                        if (method.getReturnType().equals(Long.class)) {
                            Media media = mediaRepository.findById((Long) value)
                                    .orElseThrow(() -> new MediaNotFoundException(
                                            "The media with id:" + value + " is not found",
                                            HttpStatus.NOT_FOUND));
                            setter = User.class.getMethod("set" + propertyName, Media.class);
                            setter.invoke(user, media);
                        } else if (propertyName.equals("ContactEmail") || propertyName.equals("ContactPhoneNumber")
                                || propertyName.equals("Languages")) {
                            setter = User.class.getMethod("set" + propertyName, method.getReturnType());
                            setter.invoke(user, value);
                        } else {
                            setter = User.class.getMethod("set" + propertyName, method.getReturnType());
                            setter.invoke(user, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.trace("Updated user with id: " + user.getId());
        EntityModel<User> entityModel = assembler.toModel(repository.save(user));
        logger.trace("returning updated user with id: " + user.getId());
        return entityModel;
    }

    private boolean isEmptyValue(Object value) {
        if (value instanceof String) {
            return ((String) value).trim().isEmpty();
        } else if (value instanceof Set) {
            return ((Set<?>) value).isEmpty();
        }
        return false;
    }

    public CollectionModel<EntityModel<Article>> getUserArticles(Authentication authentication) {
        logger.debug("returning user articles");
        Long userId = jwtService.extractId(authentication);
        logger.trace("Got user with id: " + userId);
        List<EntityModel<Article>> articles = articleRepository.findByPublisherId(userId).stream()
                .map(article -> articleAssembler.toModel(article))
                .collect(Collectors.toList());
        logger.trace("Got user articles");
        logger.trace("returning user articles");
        return CollectionModel.of(articles, linkTo(methodOn(UserController.class).all()).withSelfRel());

    }

    public CollectionModel<EntityModel<Post>> getUserPosts(Authentication authentication) {
        logger.debug("returning user posts");
        Long userId = jwtService.extractId(authentication);
        logger.trace("Got user with id: " + userId);
        List<EntityModel<Post>> posts = postRepository.findByPublisherId(userId).stream()
                .map(article -> postAssembler.toModel(article))
                .collect(Collectors.toList());
        logger.trace("Got user posts");
        logger.trace("returning user posts");
        return CollectionModel.of(posts, linkTo(methodOn(UserController.class).all()).withSelfRel());

    }

    public EntityModel<Article> getUserArticle(Long articleId, Authentication authentication) {
        logger.debug("returning user articles");
        Long userId = jwtService.extractId(authentication);
        logger.trace("Got user with id: " + userId);

        logger.trace("returning user articles");
        return articleAssembler
                .toModel(articleRepository.findByIdAndPublisherId(articleId, userId));

    }

    public CollectionModel<EntityModel<ResearchPaper>> getUserResearchPapers(Authentication authentication) {
        logger.debug("returning user ResearchPapers");
        Long userId = jwtService.extractId(authentication);
        logger.trace("Got user with id: " + userId);
        List<EntityModel<ResearchPaper>> researchPapers = researchPapersRepository.findByPublisherId(userId).stream()
                .map(researchPapersAssembler::toModel)
                .collect(Collectors.toList());

        logger.trace("Got user ResearchPapers");
        logger.trace("returning user ResearchPapers");
        return CollectionModel.of(researchPapers, linkTo(methodOn(UserController.class).all()).withSelfRel());

    }

    public EntityModel<ResearchPaper> getUserResearchPaper(Long researchPaperId, Authentication authentication) {
        logger.debug("returning user ResearchPapers");
        Long userId = jwtService.extractId(authentication);
        logger.trace("Got user with id: " + userId);

        logger.trace("returning user ResearchPapers");
        return researchPapersAssembler
                .toModel(researchPapersRepository.findByIdAndPublisherId(researchPaperId, userId));
    }

    public void disconnectUser(String username) {
        User user = repository.findByUsername(username).orElse(null);
        if (user != null) {
            user.setStatus(Status.OFFLINE);
            repository.save(user);
            userDocumentService.updateUserStatus(user.getUsername(), Status.OFFLINE);
        }

    }

    public List<UserDocument> findConnectedUser() {
        return mongoRepository.findAllByStatus(Status.ONLINE);

    }

    @Transactional
    public void deleteUser(Long userId) {
//        logger.debug("deleting user with id: " + userId);
//        User user = repository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException("The user with id:" + userId + " is not found",
//                        HttpStatus.NOT_FOUND));
     logger.trace("Got user with id: " + userId);

        repository.deleteByIdCustom(userId);
        logger.trace("Deleted user with id: " + userId);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with username " + email + " is not found"));

    }

    public int enableUser(String email) {
        return repository.enableAppUser(email);
    }

    CollectionModel<EntityModel<Notification>> getUserNotifications(Authentication authentication) {
        logger.debug("returning user Notifications");

        User user = jwtService.getUser(authentication);
        logger.trace("Got user with id: " + user.getId());
        List<EntityModel<Notification>> notifications = notificationRepository.findByUser(
                user).stream()
                .map(article -> notificationAssembler.toModel(article))
                .collect(Collectors.toList());
        logger.trace("Got user notifications");
        logger.trace("returning user notifications");
        return CollectionModel.of(notifications, linkTo(methodOn(UserController.class).all()).withSelfRel());

    }

    public EntityModel<User> uploadProfilePicture(Authentication authentication, MultipartFile file) {
        User user = jwtService.getUser(authentication);
        Media media = fileStorageManager.saveOne(file, authentication);

        user.setProfilePicture(media);
        return assembler.toModel(repository.save(user));
    }

    public EntityModel<User> uploadCoverPicture(Authentication authentication, MultipartFile file) {
        User user = jwtService.getUser(authentication);
        Media media = fileStorageManager.saveOne(file, authentication);

        user.setProfileCover(media);
        return assembler.toModel(repository.save(user));
    }

//    public  User registerOrUpdateOAuth2User(OAuth2User oAuth2User){
//        String email = oAuth2User.getAttribute("email");
//        Optional<User> existingUser= repository.findByEmail(email);
//
//        User user;
//
//        if(existingUser.isPresent()){
//            user = existingUser.get();
//        }else {
//            user = new User();
//            user.setEmail(email);
//            user.setFirstName(oAuth2User.getAttribute("given_name"));
//            user.setLastName(oAuth2User.getAttribute("family_name"));
//            user.setUsername(email);
//            user.setPassword("");
//            user.setPosition(Position.ASSISTANT_PROFESSOR);
//            user.setBadge("HEllo");
//            user.setEducation("HEllo");
//            user.setPhoneNumber("0593015525");
//            user.setEnabled(true);
//            user.setLocked(false);
//            user.setRole(Role.ACADEMIC);
//            repository.save(user);
//        }
//
//        return  user;
//    }

    public User registerOrUpdateOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        Optional<User> existingUser = repository.findByEmail(email);

        User user;

        // Check if the user exists
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setUsername(email);
            user.setPassword("");
            user.setPhoneNumber("0593015525");
            user.setEnabled(true);
            user.setLocked(false);

            // Differentiate user data based on OAuth provider
            if (isGitHubUser(oAuth2User)) {
                user = processGitHubUser(user, oAuth2User);
            } else if (isGoogleUser(oAuth2User)) {
                user = processGoogleUser(user, oAuth2User);
            }

            repository.save(user);
        }

        return user;
    }

    private boolean isGitHubUser(OAuth2User oAuth2User) {
        // Check if the user has specific GitHub attributes
        return oAuth2User.getAttribute("login") != null;
    }

    private boolean isGoogleUser(OAuth2User oAuth2User) {
        // Check if the user has specific Google attributes
        return oAuth2User.getAttribute("sub") != null;
    }

    private User processGitHubUser(User user, OAuth2User oAuth2User) {
        // Process GitHub-specific attributes
        user.setFirstName(oAuth2User.getAttribute("name"));
        user.setLastName(oAuth2User.getAttribute("name"));
        user.setPosition(Position.ASSISTANT_PROFESSOR);
        user.setBadge("GitHub Badge");
        user.setEducation("GitHub Education");
        user.setRole(Role.ACADEMIC); // Example: GitHub users are students
        return user;
    }

    private User processGoogleUser(User user, OAuth2User oAuth2User) {
        // Process Google-specific attributes
        user.setFirstName(oAuth2User.getAttribute("given_name"));
        user.setLastName(oAuth2User.getAttribute("family_name"));
        user.setPosition(Position.ASSISTANT_PROFESSOR);
        user.setBadge("Google Badge");
        user.setEducation("Google Education");
        user.setRole(Role.ACADEMIC); // Example: Google users are academics
        return user;
    }


    public EntityModel<User> getUserInfo(Authentication authentication) {
        User user = jwtService.getUser(authentication);
        return assembler.toModel(user);
    }

    public List<PeopleYouMayKnowResponseDTO> getPeopleYouMayKnow(Authentication authentication) {
        Long userId = jwtService.extractId(authentication);
        logger.trace("Extracted user ID: {}", userId);

        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        List<UserLinks> mutualConnections = userLinksRepository.findMutualConnections(userId);
        logger.trace("Got users by mutual connections: {}", mutualConnections);

        List<PeopleYouMayKnowResponseDTO> responseDTOList = new ArrayList<>();
        for (UserLinks userLink : mutualConnections) {
            User otherUser = userLink.getLinksTo().equals(user) ? userLink.getLinksFrom() : userLink.getLinksTo();
            logger.trace("Processing user: {}", otherUser);

            int sharedSkills = calculateSharedSkills(user, otherUser);
            PeopleYouMayKnowResponseDTO dto = PeopleYouMayKnowResponseDTO.builder()
                    .name(otherUser.getFirstName() + " " + otherUser.getLastName())
                    .sharedSkills(sharedSkills)
                    .profilePicture(otherUser.getProfilePicture())
                    .accepted(userLinksRepository.areUsersLinked(userId, otherUser.getId()))
                    .id(otherUser.getId())
                    .build();
            logger.trace("Adding user to response DTO list: {}", dto);
            responseDTOList.add(dto);
        }

        logger.trace("Final response DTO list: {}", responseDTOList);
        return responseDTOList;
    }

    private int calculateSharedSkills(User user, User otherUser) {
        logger.trace("Calculating shared skills between users: {} and {}", user.getId(), otherUser.getId());

        long sharedSkillsCount = user.getSkills().stream()
                .filter(otherUser.getSkills()::contains)
                .count();

        logger.trace("Shared skills count: {}", sharedSkillsCount);
        return (int) sharedSkillsCount;
    }

}
