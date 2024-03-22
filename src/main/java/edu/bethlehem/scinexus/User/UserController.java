package edu.bethlehem.scinexus.User;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.Academic.AcademicNotFoundException;
import edu.bethlehem.scinexus.Config.JwtService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;
    private final UserModelAssembler assembler;
    private final JwtService jwtService;

    @GetMapping("/{userId}")
    EntityModel<User> one(@PathVariable Long userId) throws UserNotFoundException {

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + userId + " is not found",
                        HttpStatus.NOT_FOUND));

        return assembler.toModel(user);
    }

    @GetMapping("/links")
    CollectionModel<EntityModel<User>> userLinks(@RequestHeader(name = "Authorization") String token)
            throws UserNotFoundException {
        Long userId = jwtService.extractId(token);

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + userId + " is not found",
                        HttpStatus.NOT_FOUND));

        List<EntityModel<User>> links = user.getLinks().stream()
                .map(userLinked -> assembler.toModel(
                        userLinked))
                .collect(Collectors.toList());

        return CollectionModel.of(links, linkTo(methodOn(UserController.class).all()).withSelfRel());

    }

    @GetMapping()
    CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> users = repository.findAll().stream()
                .map(user -> assembler.toModel(user))
                .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @PostMapping()
    ResponseEntity<?> createNewUser(@RequestBody User newUser) {

        EntityModel<User> entityModel = assembler.toModel(repository.save(newUser));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping("/linkTo/{userLinkTo}")
    ResponseEntity<?> linkUser(@RequestHeader(name = "Authorization") String token, @PathVariable Long userLinkTo) {
        Long linkFrom = jwtService.extractId(token);

        User userTo = repository.findById(
                userLinkTo).orElseThrow(
                        () -> new UserNotFoundException("The user with id:" +
                                userLinkTo + " is not found", HttpStatus.NOT_FOUND));
        return repository.findById(
                linkFrom)
                .map(user -> {
                    if (user.getLinks().contains(userTo))
                        return ResponseEntity.badRequest().body("The user with id:" + linkFrom
                                + " is already linked to the user with id:" + userLinkTo);

                    user.getLinks().add(userTo);

                    EntityModel<User> entityModel = assembler.toModel(repository.save(user));
                    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                            .body(entityModel);
                })
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + linkFrom + " is not found",
                        HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> linkUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {

                    // User Properties
                    user.setUsername(newUser.getUsername());
                    user.setPassword(newUser.getPassword());
                    user.setEmail(newUser.getEmail());
                    user.setProfilePicture(newUser.getProfilePicture());
                    user.setProfileCover(newUser.getProfileCover());
                    user.setBio(newUser.getBio());
                    user.setPhoneNumber(newUser.getPhoneNumber());
                    user.setFieldOfWork(newUser.getFieldOfWork());
                    user.setUserSettings(newUser.getUserSettings());

                    EntityModel<User> entityModel = assembler.toModel(repository.save(user));
                    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                            .body(entityModel);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    EntityModel<User> entityModel = assembler.toModel(repository.save(newUser));
                    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                            .body(entityModel);
                });
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long userId,
            @RequestBody User newUser) throws UserNotFoundException {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + userId + " is not found",
                        HttpStatus.NOT_FOUND));
        // User Properties
        if (newUser.getUsername() != null)
            user.setUsername(newUser.getUsername());
        if (newUser.getPassword() != null)
            user.setPassword(newUser.getPassword());
        if (newUser.getEmail() != null)
            user.setEmail(newUser.getEmail());
        if (newUser.getProfilePicture() != null)
            user.setProfilePicture(newUser.getProfilePicture());
        if (newUser.getProfileCover() != null)
            user.setProfileCover(newUser.getProfileCover());
        if (newUser.getBio() != null)
            user.setBio(newUser.getBio());
        if (newUser.getPhoneNumber() != null)
            user.setPhoneNumber(newUser.getPhoneNumber());
        if (newUser.getFieldOfWork() != null)
            user.setFieldOfWork(newUser.getFieldOfWork());
        if (newUser.getUserSettings() != null)
            user.setUserSettings(newUser.getUserSettings());

        EntityModel<User> entityModel = assembler.toModel(repository.save(user));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) throws UserNotFoundException {

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("The user with id:" + id + " is not found",
                        HttpStatus.NOT_FOUND));

        repository.delete(user);

        return ResponseEntity.noContent().build();

    }
}
