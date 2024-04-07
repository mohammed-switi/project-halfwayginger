package edu.bethlehem.scinexus.Article;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")

public class ArticleController {


  private final ArticleService service;

  @GetMapping("/{articleId}")
  ResponseEntity<EntityModel<Article>> one(@PathVariable Long articleId) {

    return ResponseEntity.ok(service.findArticleById(articleId));
  }

  @GetMapping()
  public ResponseEntity<CollectionModel<EntityModel<Article>>> all() {
    return ResponseEntity.ok(service.findAllArticles());
  }

  @PostMapping()
  ResponseEntity<?> newArticle(Authentication authentication,@RequestBody ArticleRequestDTO newArticle) {

   return new ResponseEntity<>(service.createArticle(newArticle,authentication),HttpStatus.CREATED);
   // return ResponseEntity.ok(service.createArticle(newArticle, authentication));
  }


  @PatchMapping("/{id}")
  public ResponseEntity<?> updateArticlePartially(@PathVariable(value = "id") Long id,
      @RequestBody @Valid ArticleRequestPatchDTO newArticle) {
    return new ResponseEntity<>(service.updateArticlePartially(id, newArticle),HttpStatus.CREATED);

  }

  // will be deleting from journal
  @DeleteMapping("/{articleId}")
  ResponseEntity<?> deleteArticle(@PathVariable Long articleId) {
    service.deleteArticle(articleId);
    return ResponseEntity.noContent().build();
  }
}
