package edu.bethlehem.scinexus.Post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

  private final PostRepository repository;
  private final PostModelAssembler assembler;

  PostController(PostRepository repository, PostModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/posts/{postId}")
  EntityModel<Post> one(@PathVariable Long postId) {

    Post post = repository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException(postId,HttpStatus.NOT_FOUND));

    return assembler.toModel(post);
  }

  @GetMapping("/posts")
  CollectionModel<EntityModel<Post>> all() {
    List<EntityModel<Post>> posts = repository.findAll().stream().map(post -> assembler.toModel(post))
        .collect(Collectors.toList());

    return CollectionModel.of(posts, linkTo(methodOn(PostController.class).all()).withSelfRel());
  }

  @PostMapping("/posts")
  ResponseEntity<?> newPost(@RequestBody Post newPost) {

    EntityModel<Post> entityModel = assembler.toModel(repository.save(newPost));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/posts/{id}")
  ResponseEntity<?> editPost(@RequestBody Post newPost, @PathVariable Long id) {

    return repository.findById(id)
        .map(post -> {
          post.setContent(newPost.getContent());
          post.setVisibility(newPost.getVisibility());
          // post.setPublisher(newPost.getPublisher());
          post.setInteractionCount(newPost.getInteractionCount());
          post.setOpinionsCount(newPost.getOpinionsCount());
          post.setReShare(newPost.getReShare());
          EntityModel<Post> entityModel = assembler.toModel(repository.save(post));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newPost.setId(id);
          EntityModel<Post> entityModel = assembler.toModel(repository.save(newPost));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }

  @PatchMapping("/posts/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long postId,
      @RequestBody Post newPost) {
    Post post = repository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException(postId,HttpStatus.NOT_FOUND));
    if (newPost.getContent() != null)
        post.setContent(newPost.getContent());
    if (newPost.getVisibility() != null)
      post.setVisibility(newPost.getVisibility());

    // post.setPublisher(newPost.getPublisher());

    if (newPost.getInteractionCount() != null)
      post.setInteractionCount(newPost.getInteractionCount());
    if (newPost.getOpinionsCount() != null)
      post.setOpinionsCount(newPost.getOpinionsCount());
    if (newPost.getReShare() != null)
      post.setReShare(newPost.getReShare());

    EntityModel<Post> entityModel = assembler.toModel(repository.save(post));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/posts/{id}")
  ResponseEntity<?> deletePost(@PathVariable Long id) {

    Post post = repository.findById(id).orElseThrow(() -> new PostNotFoundException(id,HttpStatus.NOT_FOUND));

    repository.delete(post);

    return ResponseEntity.noContent().build();

  }
}
