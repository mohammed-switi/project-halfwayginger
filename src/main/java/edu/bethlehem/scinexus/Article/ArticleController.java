package edu.bethlehem.scinexus.Article;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.SecurityConfig.UserDetailsImpl;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")

public class ArticleController {

  private final ArticleRepository repository;
  private final UserRepository userRepository;
  private final ArticleModelAssembler assembler;
  private final ArticleService service;

  @GetMapping("/{articleId}")
  EntityModel<Article> one(@PathVariable Long articleId) {

    return service.findArticleById(articleId);
  }

  @GetMapping()
  CollectionModel<EntityModel<Article>> all() {
    return service.findAllArticles();
  }

  @PostMapping()
  ResponseEntity<?> newArticle(@RequestBody ArticleRequestDTO newArticle,
      Authentication authentication) {
    return ResponseEntity.ok(service.createArticle(newArticle, authentication));
  }

  @PutMapping("/{id}")
  ResponseEntity<?> editArticle(@RequestBody @Valid ArticleRequestDTO newArticle, @PathVariable Long id,
      Authentication authentication) {
    return ResponseEntity.ok(service.updateArticle(id, newArticle));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long id,
      @RequestBody @Valid ArtilceRequestPatchDTO newArticle) {
    return ResponseEntity.ok(service.updateArticlePartially(id, newArticle));

  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteArticle(@PathVariable Long id) {
    service.deleteArticle(id);
    return ResponseEntity.noContent().build();
  }
}
