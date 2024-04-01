package edu.bethlehem.scinexus.User;

import edu.bethlehem.scinexus.MongoRepository.UserMongoRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.stream.Collectors;
import java.lang.reflect.Method;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Article.ArticleModelAssembler;
import edu.bethlehem.scinexus.JPARepository.ArticleRepository;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperModelAssembler;
import edu.bethlehem.scinexus.JPARepository.ResearchPaperRepository;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository repository;
    private final UserMongoRepository mongoRepository;
    private final ArticleRepository articleRepository;
    private final ResearchPaperRepository researchPapersRepository;
    private final ResearchPaperModelAssembler researchPapersAssembler;
    private final UserModelAssembler assembler;
    private final ArticleModelAssembler articleAssembler;
    private final JwtService jwtService;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    // USING Java Persistence Query Language (JPQL), (Tested: Works Well!)
    public boolean areUsersLinked(User user1, User user2) {
        logger.debug("Checking if the users are linked");
        Long count = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u JOIN u.links l " +
                        "WHERE u.id = :userId1 AND l.id = :userId2",
                Long.class)
                .setParameter("userId1", user1.getId())
                .setParameter("userId2", user2.getId())
                .getSingleResult();

        return count > 0;
    }

    CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> users = repository.findAll().stream()
                .map(user -> assembler.toModel(user))
                .collect(Collectors.toList());
        logger.trace("Got all users");
        logger.debug("returning all users");
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    EntityModel<User> one(Long userId) {
        logger.debug("returning user with id: " + userId);
        User user = entityManager.find(User.class, userId);
        logger.trace("Got user with id: " + userId);
        return assembler.toModel(user);
    }

    ResponseEntity<?> updateUserPartially(UserRequestPatchDTO editUser, Long userId) {
        logger.debug("partially updating user with id: " + userId);
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + userId + " is not found",
                        HttpStatus.NOT_FOUND));
        logger.trace("Got user with id: " + userId);
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
                        Method setter = User.class.getMethod("set" + propertyName, method.getReturnType());
                        setter.invoke(user, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.trace("Updated user with id: " + userId);
        EntityModel<User> entityModel = assembler.toModel(repository.save(user));
        logger.trace("returning updated user with id: " + userId);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);

    }

    CollectionModel<EntityModel<User>> getLinks(Authentication authentication) {
        logger.debug("returning user links");
        Long userId = ((User) (authentication.getPrincipal())).getId();

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + userId + " is not found",
                        HttpStatus.NOT_FOUND));
        logger.trace("Got user with id: " + userId);

        List<EntityModel<User>> links = user.getLinks().stream()
                .map(userLinked -> assembler.toModel(
                        userLinked))
                .collect(Collectors.toList());
        logger.trace("Got user links");
        logger.trace("returning user links");
        return CollectionModel.of(links, linkTo(methodOn(UserController.class).all()).withSelfRel());

    }

    ResponseEntity<?> linkUser(Authentication authentication, Long userLinkToId) {

        logger.debug("linking user with id: " + userLinkToId);

        Long linkFromId = ((User) (authentication.getPrincipal())).getId();

        logger.trace("Got user with id: " + linkFromId);

        User userLinkTo = repository.findById(
                userLinkToId).orElseThrow(
                        () -> new UserNotFoundException("The user with id:" +
                                userLinkToId + " is not found", HttpStatus.NOT_FOUND));

        logger.trace("Got user with id: " + userLinkToId);

        User user = repository.findById(linkFromId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + linkFromId + " is not found",
                        HttpStatus.NOT_FOUND));

        logger.trace("Got user with id: " + linkFromId);

        logger.trace("Checking if the users are already linked");
        if (user.getLinks().contains(userLinkTo))
            return ResponseEntity.badRequest().body("The user with id:" + linkFromId
                    + " is already linked to the user with id:" + userLinkToId);
        logger.trace("Checking if the user is trying to link to itself");
        if (user.getId() == userLinkTo.getId())

            return ResponseEntity.badRequest().body("The user with id:" + linkFromId
                    + " cannot link to itself");

        user.getLinks().add(userLinkTo);
        userLinkTo.getLinks().add(user);
        repository.save(userLinkTo);
        logger.trace("Linked user with id: " + userLinkToId);
        EntityModel<User> entityModel = assembler.toModel(repository.save(user));
        logger.trace("returning linked user with id: " + userLinkToId);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }

    CollectionModel<EntityModel<Article>> getUserArticles(Authentication authentication) {
        logger.debug("returning user articles");
        Long userId = ((User) (authentication.getPrincipal())).getId();
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
        Long userId = ((User) (authentication.getPrincipal())).getId();
        logger.trace("Got user with id: " + userId);

        logger.trace("returning user articles");
        return articleAssembler
                .toModel(articleRepository.findByIdAndPublisherId(articleId, userId));

    }

    CollectionModel<EntityModel<ResearchPaper>> getUserResearchPapers(Authentication authentication) {
        logger.debug("returning user ResearchPapers");
        Long userId = ((User) (authentication.getPrincipal())).getId();
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
        Long userId = ((User) (authentication.getPrincipal())).getId();
        logger.trace("Got user with id: " + userId);

        logger.trace("returning user ResearchPapers");
        return researchPapersAssembler
                .toModel(researchPapersRepository.findByIdAndPublisherId(researchPaperId, userId));
    }


    public void saveUser(UserDocument user){
        user.setStatus(Status.ONLINE);
        System.out.println("Saving ths uer from the Service:" +mongoRepository.save(user));
    }

    public void disconnect(UserDocument user){
        var storedUser = mongoRepository.findById(user.getNickName()).orElse(null);
        if (storedUser!=null){
        storedUser.setStatus(Status.OFFLINE);
            mongoRepository.save(storedUser);
        }

    }

    public List<UserDocument> findConnectedUser(){
        return mongoRepository.findAllByStatus(Status.ONLINE);

    }
    ResponseEntity<?> deleteUser(Long userId) {
        logger.debug("deleting user with id: " + userId);
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + userId + " is not found",
                        HttpStatus.NOT_FOUND));
        logger.trace("Got user with id: " + userId);

        repository.delete(user);
        logger.trace("Deleted user with id: " + userId);
        return ResponseEntity.noContent().build();

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return   repository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("User with username "+ email +" is not found"));
    }

    public int enableUser(String email) {
        return repository.enableAppUser(email);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//       String userName = null, password=null;
//       List<GrantedAuthority> authorities=null;
//       User user=repository.findByEmail(username).orElseThrow(() -> new UserNotFoundException());
//
//       userName=user.getEmail();
//       password=user.getPassword();
//       authorities=new ArrayList<>();
//       authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
//
//        return  new org.springframework.security.core.userdetails.User(username,password,authorities);
//    }

    // if (editUser.getUsername() != null)
    // user.setUsername(editUser.getUsername());
    // if (editUser.getPassword() != null)
    // user.setPassword(editUser.getPassword());
    // if (editUser.getEmail() != null)
    // user.setEmail(editUser.getEmail());
    // if (editUser.getBio() != null)
    // user.setBio(editUser.getBio());
    // if (editUser.getPhoneNumber() != null)
    // user.setPhoneNumber(editUser.getPhoneNumber());
    // if (editUser.getFieldOfWork() != null)
    // user.setFieldOfWork(editUser.getFieldOfWork());
    // if (editUser.getFirstName() != null)
    // user.setFirstName(editUser.getFirstName());

    // Using Native MySQL Query Language (Not Tested Yet!)
    // public boolean areUsersLinked(User user1, User user2) {
    // Query query = entityManager.createNativeQuery(
    // "SELECT COUNT(*) FROM user_links " +
    // "WHERE user_id = :userId1 AND linked_user_id = :userId2");
    //
    // query.setParameter("userId1", user1.getId());
    // query.setParameter("userId2", user2.getId());
    //
    // Long count = ((Number) query.getSingleResult()).longValue();
    //
    // return count > 0;
    // }

}
