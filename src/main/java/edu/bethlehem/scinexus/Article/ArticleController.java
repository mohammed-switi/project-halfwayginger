package edu.bethlehem.scinexus.Article;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController {

  private final ArticleRepository repository;
  private final ArticleModelAssembler assembler;

  ArticleController(ArticleRepository repository, ArticleModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/articles/{articleId}")
  EntityModel<Article> one(@PathVariable Long articleId) {

    Article article = repository.findById(articleId)
        .orElseThrow(() -> new ArticleNotFoundException(articleId));

    return assembler.toModel(article);
  }

  @GetMapping("/articles")
  CollectionModel<EntityModel<Article>> all() {
    List<EntityModel<Article>> articles = repository.findAll().stream().map(article -> assembler.toModel(article))
        .collect(Collectors.toList());

    return CollectionModel.of(articles, linkTo(methodOn(ArticleController.class).all()).withSelfRel());
  }

  @PostMapping("/articles")
  ResponseEntity<?> newArticle(@RequestBody Article newArticle) {

    EntityModel<Article> entityModel = assembler.toModel(repository.save(newArticle));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/articles/{id}")
  ResponseEntity<?> editArticle(@RequestBody Article newArticle, @PathVariable Long id) {

    return repository.findById(id)
        .map(article -> {
          article.setContent(newArticle.getContent());
          EntityModel<Article> entityModel = assembler.toModel(repository.save(article));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newArticle.setId(id);
          EntityModel<Article> entityModel = assembler.toModel(repository.save(newArticle));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }

  @DeleteMapping("/articles/{id}")
  ResponseEntity<?> deleteArticle(@PathVariable Long id) {

    Article article = repository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));

    repository.delete(article);

    return ResponseEntity.noContent().build();

  }
}
