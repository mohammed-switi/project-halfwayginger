package edu.bethlehem.scinexus.Article;

import edu.bethlehem.scinexus.JPARepository.ArticleRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.JPARepository.UserRepository;
import jakarta.validation.Valid;

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

  // No need for any PUT Method
  // @PutMapping("/{id}")
  // ResponseEntity<?> editArticle(@RequestBody @Valid ArticleRequestDTO
  // newArticle, @PathVariable Long id,
  // Authentication authentication) {
  // return ResponseEntity.ok(service.updateArticle(id, newArticle));
  // }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long id,
      @RequestBody @Valid ArtilceRequestPatchDTO newArticle) {
    return ResponseEntity.ok(service.updateArticlePartially(id, newArticle));

  }

  // will be deleting from journal
  @DeleteMapping("/{articleId}")
  ResponseEntity<?> deleteArticle(@PathVariable Long articleId) {
    service.deleteArticle(articleId);
    return ResponseEntity.noContent().build();
  }
}
