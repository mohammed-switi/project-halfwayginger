package edu.bethlehem.scinexus.Post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  @GetMapping("/{postId}")
  public ResponseEntity<EntityModel<Post>> one(@PathVariable long postId) {
    return ResponseEntity.ok(postService.findPostById(postId));
  }

  @GetMapping()
  public ResponseEntity<CollectionModel<EntityModel<Post>>> all() {
    CollectionModel<EntityModel<Post>> posts = postService.findAllPosts();

    return ResponseEntity.ok(CollectionModel.of(posts, linkTo(methodOn(PostController.class).all()).withSelfRel()));
  }

  @PostMapping()
  public ResponseEntity<EntityModel<Post>> createNewPost(Authentication authentication,
      @Valid @RequestBody @NotNull PostRequestDTO newPostRequestDTO) {

    EntityModel<Post> entityModel = postService.createPost(authentication, newPostRequestDTO);
    return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    // This Line Is commented Because of an Issue in testing, but Fixed it with the
    // previous line and both provides same result
    // return
    // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PostMapping("/{journalId}/reshare")
  public ResponseEntity<?> createNewPostReshare(Authentication authentication,
      @Valid @RequestBody @NotNull PostRequestDTO newPostRequestDTO, @PathVariable Long journalId) {

    EntityModel<Post> entityModel = postService.createResharePost(authentication, newPostRequestDTO, journalId);
    return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    // This Line Is commented Because of an Issue in testing, but Fixed it with the
    // previous line and both provides same result
    // return
    // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  // @PutMapping("/{id}")
  // public ResponseEntity<?> editPost(@PathVariable @NotNull Long id,
  // @Valid @RequestBody @NotNull PostRequestDTO newPostRequestDTO) {

  // EntityModel<Post> entityModel = postService.updatePost(id,
  // newPostRequestDTO);

  // return
  // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  // }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updatePostPartially(@PathVariable(value = "id") Long postId,

      @RequestBody @NotNull PostRequestPatchDTO newPostRequestDTO) {

    EntityModel<Post> entityModel = postService.updatePostPartially(postId, newPostRequestDTO);
    return new ResponseEntity<>(entityModel, HttpStatus.CREATED);

    // return
    // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deletePost(@PathVariable Long id) {
    postService.deletePost(id);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("count/{id}")
  public ResponseEntity<?> getResearchPaperCount(
          @PathVariable Long id
          , Authentication authentication
  ) {

    HashMap<String, Long> response = new HashMap<>();
    response.put("count", postService.getPostsCount(id));
    return ResponseEntity.ok(response);
  }

}
