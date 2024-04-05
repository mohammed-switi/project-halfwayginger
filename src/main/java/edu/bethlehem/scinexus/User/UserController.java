package edu.bethlehem.scinexus.User;

import java.util.List;

import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Import({ UserModelAssembler.class })

public class UserController {


    private final UserService service;




    @GetMapping()
    ResponseEntity<CollectionModel<EntityModel<User>>> all() {

        return ResponseEntity.ok(service.all());
    }

    @GetMapping("/{id}")
   ResponseEntity< EntityModel<User>> one(@PathVariable Long id) {

        return ResponseEntity.ok(service.one(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long userId,
            @RequestBody UserRequestPatchDTO newUser) throws UserNotFoundException {
        return service.updateUserPartially(newUser, userId);
    }


    @GetMapping("/links")
    public CollectionModel<EntityModel<User>> getUserLinks(Authentication authentication)
            throws UserNotFoundException {
        return service.getLinks(authentication);
    }

    @PutMapping("/links/{userLinkTo}")
     public ResponseEntity<?> linkUser(Authentication authentication, @PathVariable Long userLinkTo) {
        return service.linkUser(authentication, userLinkTo);
    }

    @GetMapping("/articles")
    public CollectionModel<EntityModel<Article>> getUserArticles(Authentication authentication)
            throws UserNotFoundException {
        return service.getUserArticles(authentication);
    }

    @GetMapping("/articles/{articleId}")
   public EntityModel<Article> getUserArticles(@PathVariable Long articleId, Authentication authentication)
            throws UserNotFoundException {
        return service.getUserArticle(articleId, authentication);
    }

    @GetMapping("/researchpapers")
   public CollectionModel<EntityModel<ResearchPaper>> getUserResearchPapers(Authentication authentication)
            throws UserNotFoundException {
        return service.getUserResearchPapers(authentication);
    }

    @GetMapping("/researchpapers/{researchPaperId}")
     public EntityModel<ResearchPaper> getUserResearchPaper(@PathVariable Long researchPaperId,
            Authentication authentication)
            throws UserNotFoundException {
        return service.getUserResearchPaper(researchPaperId, authentication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws UserNotFoundException {

        service.deleteUser(id);

        return ResponseEntity.noContent().build();

    }

}
