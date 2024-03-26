package edu.bethlehem.scinexus.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.stream.Collectors;
import java.lang.reflect.Method;
import java.util.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Article.ArticleModelAssembler;
import edu.bethlehem.scinexus.Article.ArticleRepository;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;

@Service
@RequiredArgsConstructor
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository repository;
    private final ArticleRepository articleRepository;
    private final UserModelAssembler assembler;
    private final ArticleModelAssembler articleAssembler;
    private final JwtService jwtService;

    // USING Java Persistence Query Language (JPQL), (Tested: Works Well!)
    public boolean areUsersLinked(User user1, User user2) {
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

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    EntityModel<User> one(Long userId) {
        User user = entityManager.find(User.class, userId);
        return assembler.toModel(user);
    }

    ResponseEntity<?> updateUserPartially(UserRequestPatchDTO editUser, Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + userId + " is not found",
                        HttpStatus.NOT_FOUND));
        // User Properties

        try {
            for (Method method : UserRequestDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(editUser);
                    if (value != null) {
                        String propertyName = method.getName().substring(3); // remove "get"
                        Method setter = User.class.getMethod("set" + propertyName, method.getReturnType());
                        setter.invoke(user, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityModel<User> entityModel = assembler.toModel(repository.save(user));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);

    }

    CollectionModel<EntityModel<User>> getLinks(Authentication authentication) {
        Long userId = ((User) (authentication.getPrincipal())).getId();

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + userId + " is not found",
                        HttpStatus.NOT_FOUND));

        List<EntityModel<User>> links = user.getLinks().stream()
                .map(userLinked -> assembler.toModel(
                        userLinked))
                .collect(Collectors.toList());

        return CollectionModel.of(links, linkTo(methodOn(UserController.class).all()).withSelfRel());

    }

    ResponseEntity<?> linkUser(Authentication authentication, Long userLinkToId) {
        Long linkFromId = ((User) (authentication.getPrincipal())).getId();
        User userLinkTo = repository.findById(
                userLinkToId).orElseThrow(
                        () -> new UserNotFoundException("The user with id:" +
                                userLinkToId + " is not found", HttpStatus.NOT_FOUND));
        User user = repository.findById(linkFromId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + linkFromId + " is not found",
                        HttpStatus.NOT_FOUND));

        if (user.getLinks().contains(userLinkTo))
            return ResponseEntity.badRequest().body("The user with id:" + linkFromId
                    + " is already linked to the user with id:" + userLinkToId);
        if (user.getId() == userLinkTo.getId())
            return ResponseEntity.badRequest().body("The user with id:" + linkFromId
                    + " cannot link to itself");
        user.getLinks().add(userLinkTo);

        EntityModel<User> entityModel = assembler.toModel(repository.save(user));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }

    CollectionModel<EntityModel<Article>> getUserArticles(Authentication authentication) {
        Long userId = ((User) (authentication.getPrincipal())).getId();

        List<EntityModel<Article>> articles = articleRepository.findByPublisherId(userId).stream()
                .map(article -> articleAssembler.toModel(article))
                .collect(Collectors.toList());
        return CollectionModel.of(articles, linkTo(methodOn(UserController.class).all()).withSelfRel());

    }

    ResponseEntity<?> deleteUser(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + userId + " is not found",
                        HttpStatus.NOT_FOUND));

        repository.delete(user);

        return ResponseEntity.noContent().build();

    }

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
