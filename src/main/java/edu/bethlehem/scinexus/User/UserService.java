package edu.bethlehem.scinexus.User;

import edu.bethlehem.scinexus.MongoRepository.UserMongoRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.JPARepository.UserResearchPaperRequestRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.stream.Collectors;
import java.lang.reflect.Method;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.Notification.NotificationModelAssembler;
import edu.bethlehem.scinexus.JPARepository.NotificationRepository;
import edu.bethlehem.scinexus.Article.ArticleModelAssembler;
import edu.bethlehem.scinexus.File.FileStorageService;
import edu.bethlehem.scinexus.JPARepository.ArticleRepository;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperModelAssembler;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperNotFoundException;
import edu.bethlehem.scinexus.JPARepository.ResearchPaperRepository;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Media.MediaNotFoundException;
import edu.bethlehem.scinexus.JPARepository.MediaRepository;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperController;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperModelAssembler;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.UserResearchPaper.ResearchPaperRequestKey;
import edu.bethlehem.scinexus.UserResearchPaper.UserResearchPaperRequest;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository repository;
    private final UserMongoRepository mongoRepository;
    private final ArticleRepository articleRepository;
    private final ResearchPaperRepository researchPaperRepository;
    private final MediaRepository mediaRepository;
    private final NotificationRepository notificationRepository;
    private final ResearchPaperRepository researchPapersRepository;
    private final ResearchPaperModelAssembler researchPapersAssembler;
    private final NotificationModelAssembler notificationAssembler;
    private final UserModelAssembler assembler;
    private final ArticleModelAssembler articleAssembler;
    @Autowired
    FileStorageService fileStorageManager;
    private final UserResearchPaperRequestRepository userResearchPaperRequestRepository;

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

    CollectionModel<EntityModel<User>> all() {
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

    ResponseEntity<?> updateUserPartially(UserRequestPatchDTO editUser, Authentication auth) {
        User user = jwtService.getUser(auth);
        logger.debug("partially updating user with id: " + user.getId());

        logger.trace("Got user with id: " + user.getId());
        // User Properties

        try {
            for (Method method : UserRequestPatchDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(editUser);

                    if (value != null) {
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

                        }
                        setter = User.class.getMethod("set" + propertyName, method.getReturnType());
                        setter.invoke(user, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.trace("Updated user with id: " + user.getId());
        EntityModel<User> entityModel = assembler.toModel(repository.save(user));
        logger.trace("returning updated user with id: " + user.getId());
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);

    }

    CollectionModel<EntityModel<Article>> getUserArticles(Authentication authentication) {
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

    EntityModel<Article> getUserArticle(Long articleId, Authentication authentication) {
        logger.debug("returning user articles");
        Long userId = jwtService.extractId(authentication);
        logger.trace("Got user with id: " + userId);

        logger.trace("returning user articles");
        return articleAssembler
                .toModel(articleRepository.findByIdAndPublisherId(articleId, userId));

    }

    CollectionModel<EntityModel<ResearchPaper>> getUserResearchPapers(Authentication authentication) {
        logger.debug("returning user ResearchPapers");
        Long userId = jwtService.extractId(authentication);
        logger.trace("Got user with id: " + userId);
        List<EntityModel<ResearchPaper>> researchPapers = researchPapersRepository.findByPublisherId(userId).stream()
                .map(researchPaper -> researchPapersAssembler.toModel(
                        researchPaper))
                .collect(Collectors.toList());

        logger.trace("Got user ResearchPapers");
        logger.trace("returning user ResearchPapers");
        return CollectionModel.of(researchPapers, linkTo(methodOn(UserController.class).all()).withSelfRel());

    }

    EntityModel<ResearchPaper> getUserResearchPaper(Long researchPaperId, Authentication authentication) {
        logger.debug("returning user ResearchPapers");
        Long userId = jwtService.extractId(authentication);
        logger.trace("Got user with id: " + userId);

        logger.trace("returning user ResearchPapers");
        return researchPapersAssembler
                .toModel(researchPapersRepository.findByIdAndPublisherId(researchPaperId, userId));
    }

    public void saveUser(UserDocument user) {
        user.setStatus(Status.ONLINE);
        System.out.println("Saving ths uer from the Service:" + mongoRepository.save(user));
    }

    public void disconnect(UserDocument user) {
        var storedUser = mongoRepository.findById(user.getNickName()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            mongoRepository.save(storedUser);
        }

    }

    public List<UserDocument> findConnectedUser() {
        return mongoRepository.findAllByStatus(Status.ONLINE);

    }

    public void deleteUser(Long userId) {
        logger.debug("deleting user with id: " + userId);
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + userId + " is not found",
                        HttpStatus.NOT_FOUND));
        logger.trace("Got user with id: " + userId);

        repository.delete(user);
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

    EntityModel<User> uploadProfilePicture(Authentication authentication, MultipartFile file) {
        User user = jwtService.getUser(authentication);
        Media media = fileStorageManager.saveOne(file, authentication);

        user.setProfilePicture(media);
        return assembler.toModel(repository.save(user));
    }

    EntityModel<User> uploadCoverPicture(Authentication authentication, MultipartFile file) {
        User user = jwtService.getUser(authentication);
        Media media = fileStorageManager.saveOne(file, authentication);

        user.setProfileCover(media);
        return assembler.toModel(repository.save(user));
    }

}
