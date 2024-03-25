package edu.bethlehem.scinexus.User;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Article.ArticleRepository;
import edu.bethlehem.scinexus.Article.ArticleModelAssembler;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;
    private final ArticleRepository articleRepository;
    private final UserModelAssembler assembler;
    private final ArticleModelAssembler articleAssembler;
    private final JwtService jwtService;
    private final UserService service;

    @GetMapping()
    CollectionModel<EntityModel<User>> all() {

        return service.all();
    }

    @GetMapping("/{userId}")
    EntityModel<User> one(@PathVariable Long userId) {

        return service.one(userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long userId,
            @RequestBody UserResponsDTO newUser) throws UserNotFoundException {
        return service.updateUserPartially(newUser, userId);
    }

    // @PutMapping("/{id}")
    // ResponseEntity<?> linkUser(@RequestBody User newUser, @PathVariable Long id)
    // {

    // return repository.findById(id)
    // .map(user -> {
    // user.setUsername(newUser.getUsername());
    // user.setPassword(newUser.getPassword());
    // user.setEmail(newUser.getEmail());
    // user.setProfilePicture(newUser.getProfilePicture());
    // user.setProfileCover(newUser.getProfileCover());
    // user.setBio(newUser.getBio());
    // user.setPhoneNumber(newUser.getPhoneNumber());
    // user.setFieldOfWork(newUser.getFieldOfWork());
    // user.setUserSettings(newUser.getUserSettings());
    // user.setFirstName(newUser.getFirstName());

    // EntityModel<User> entityModel = assembler.toModel(repository.save(user));
    // return
    // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
    // .body(entityModel);
    // })
    // .orElseThrow(() -> {
    // return new UserNotFoundException("The user with id:" + id + " is not found",
    // HttpStatus.NOT_FOUND);
    // });
    // }

    @GetMapping("/links")
    CollectionModel<EntityModel<User>> getUserLinks(Authentication authentication)
            throws UserNotFoundException {
        return service.getLinks(authentication);
    }

    @PutMapping("/links/{userLinkTo}")
    ResponseEntity<?> linkUser(Authentication authentication, @PathVariable Long userLinkTo) {
        return service.linkUser(authentication, userLinkTo);
    }

    @GetMapping("/articles")
    CollectionModel<EntityModel<Article>> getUserArticles(Authentication authentication)
            throws UserNotFoundException {
        return service.getUserArticles(authentication);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) throws UserNotFoundException {

        return service.deleteUser(id);

    }

}
