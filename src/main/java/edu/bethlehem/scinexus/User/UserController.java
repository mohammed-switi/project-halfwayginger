package edu.bethlehem.scinexus.User;

import org.springframework.context.annotation.Import;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.UserLinks.UserLinks;
import edu.bethlehem.scinexus.UserLinks.UserLinksService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Import({ UserModelAssembler.class })

public class UserController {

    private final UserService service;
    private final UserLinksService ulService;

    @GetMapping()
    ResponseEntity<CollectionModel<EntityModel<User>>> all() {

        return ResponseEntity.ok(service.all());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> one(@PathVariable Long id) {

        return ResponseEntity.ok(service.one(id));
    }

    @PatchMapping()
    public ResponseEntity<?> updateUserPartially(Authentication authentication,
            @RequestBody UserRequestPatchDTO newUser) {
        EntityModel<User> entityModel = service.updateUserPartially(newUser, authentication);
        return ResponseEntity.ok(entityModel);

    }

    @PostMapping("/profilePicture")
    public ResponseEntity<?> changeProfilePicture(Authentication auth,
            @RequestParam("file") MultipartFile file) throws UserNotFoundException {
        return ResponseEntity.ok(service.uploadProfilePicture(auth, file));
    }

    @PostMapping("/coverPicture")
    public ResponseEntity<?> changeCoverPicture(Authentication auth,
            @RequestParam("file") MultipartFile file) throws UserNotFoundException {
        return ResponseEntity.ok(service.uploadCoverPicture(auth, file));
    }

    @GetMapping("/links")
    CollectionModel<EntityModel<UserLinks>> getUserLinks(Authentication authentication)
            throws UserNotFoundException {
        return ulService.getUserLinks(authentication);
    }

    @PutMapping("/links/{userLinkTo}")
    ResponseEntity<?> linkUser(Authentication authentication, @PathVariable Long userLinkTo) {
        return ResponseEntity.ok(ulService.linkUser(authentication, userLinkTo));
    }

    @PutMapping("/links/{userLinkTo}/response")
    public ResponseEntity<EntityModel<UserLinks>> respondToLinkage(Authentication authentication,
            @PathVariable Long userLinkTo,
            @RequestBody Boolean answer) {
        return ResponseEntity.ok(ulService.acceptLink(authentication, userLinkTo, answer));
    }

    @DeleteMapping("/links/{userLinkTo}")
    ResponseEntity<?> unlinkUser(Authentication authentication, @PathVariable Long userLinkTo) {
        ulService.unLink(authentication, userLinkTo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/articles")
    public ResponseEntity<CollectionModel<EntityModel<Article>>> getUserArticles(Authentication authentication)
            throws UserNotFoundException {
        return ResponseEntity.ok(service.getUserArticles(authentication));
    }

    @GetMapping("/articles/{articleId}")
    public EntityModel<Article> getUserArticle(@PathVariable Long articleId, Authentication authentication)
            throws UserNotFoundException {
        return service.getUserArticle(articleId, authentication);
    }

    @GetMapping("/researchpapers")
    public ResponseEntity<CollectionModel<EntityModel<ResearchPaper>>> getUserResearchPapers(
            Authentication authentication)
            throws UserNotFoundException {
        return ResponseEntity.ok(service.getUserResearchPapers(authentication));
    }

    @GetMapping("/researchpapers/{researchPaperId}")
    public ResponseEntity<EntityModel<ResearchPaper>> getUserResearchPaper(@PathVariable Long researchPaperId,
            Authentication authentication)
            throws UserNotFoundException {
        return ResponseEntity.ok(service.getUserResearchPaper(researchPaperId, authentication));
    }

    @GetMapping("/notifications")
    public ResponseEntity<CollectionModel<EntityModel<Notification>>> getUserNotifications(
            Authentication authentication)
            throws UserNotFoundException {
        return ResponseEntity.ok(service.getUserNotifications(authentication));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) throws UserNotFoundException {

        return ResponseEntity.noContent().build();

    }

}
