package edu.bethlehem.scinexus.Article;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.SecurityConfig.UserDetailsImpl;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

  private final ArticleRepository repository;
  private final UserRepository userRepository;
  private final ArticleModelAssembler assembler;

  @GetMapping("/{articleId}")
  EntityModel<Article> one(@PathVariable Long articleId) {

    Article article = repository.findById(articleId)
        .orElseThrow(() -> new ArticleNotFoundException(articleId, HttpStatus.NOT_FOUND));

    return assembler.toModel(article);
  }

  @GetMapping()
  CollectionModel<EntityModel<Article>> all() {
    List<EntityModel<Article>> articles = repository.findAll().stream().map(article -> assembler.toModel(article))
        .collect(Collectors.toList());

    return CollectionModel.of(articles, linkTo(methodOn(ArticleController.class).all()).withSelfRel());
  }

  @PostMapping()
  ResponseEntity<?> newArticle(@RequestBody Article newArticle,
      Authentication authentication) {

    User userObj = (User) authentication.getPrincipal();
    User user = userRepository.findById(userObj.getId())
        .orElseThrow(() -> new UserNotFoundException(userObj.getId().toString(), HttpStatus.NOT_FOUND));
    newArticle.setPublisher(user);

    EntityModel<Article> entityModel = assembler.toModel(repository.save(newArticle));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/{id}")
  ResponseEntity<?> editArticle(@RequestBody Article newArticle, @PathVariable Long id,
      Authentication authentication) {

    User user = (User) authentication.getPrincipal();

    Article article = repository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id, HttpStatus.NOT_FOUND));
    if (!article.getContributors().contains(user) && !article.getPublisher().equals(user))
      // return forbidden
      throw new ArticleNotFoundException(id, HttpStatus.NOT_FOUND);

    article.setContent(newArticle.getContent());
    article.setDescription(newArticle.getDescription());
    article.setSubject(newArticle.getSubject());
    article.setTitle(newArticle.getTitle());
    article.setPublisher(newArticle.getPublisher());
    article.setVisibility(newArticle.getVisibility());
    article.setContributors(newArticle.getContributors());

    EntityModel<Article> entityModel = assembler.toModel(repository.save(article));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);

  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long id,
      @RequestBody Article newArticle,
      Authentication authentication) {

    // this section checks if the user have access to the article
    User user = (User) authentication.getPrincipal();
    Article article = repository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id, HttpStatus.NOT_FOUND));
    if (!article.getContributors().contains(user) && !article.getPublisher().equals(user))
      // return forbidden
      throw new ArticleNotFoundException(id, HttpStatus.NOT_FOUND);
    // //////////////////////////////////////////////////////////////////////
    if (newArticle.getContent() != null)
      article.setContent(newArticle.getContent());
    if (newArticle.getDescription() != null)
      article.setDescription(newArticle.getDescription());
    if (newArticle.getSubject() != null)
      article.setSubject(newArticle.getSubject());
    if (newArticle.getTitle() != null)
      article.setTitle(newArticle.getTitle());

    // if (userRepository.existsById(newArticle.getPublisherId()))
    // article.setPublisherId(newArticle.getPublisherId());
    if (newArticle.getVisibility() != null)
      article.setVisibility(newArticle.getVisibility());
    if (newArticle.getContributors() != null)
      article.setContributors(newArticle.getContributors());

    EntityModel<Article> entityModel = assembler.toModel(repository.save(article));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteArticle(@PathVariable Long id,
      Authentication authentication) {

    // this section checks if the user have access to the article
    User user = (User) authentication.getPrincipal();
    Article article = repository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id, HttpStatus.NOT_FOUND));
    if (!article.getContributors().contains(user) && !article.getPublisher().equals(user))
      // return forbidden
      throw new ArticleNotFoundException(id, HttpStatus.NOT_FOUND);
    // //////////////////////////////////////////////////////////////////////

    repository.delete(article);

    return ResponseEntity.noContent().build();

  }
}
