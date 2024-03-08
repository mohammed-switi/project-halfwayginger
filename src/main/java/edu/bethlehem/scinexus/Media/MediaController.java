package edu.bethlehem.scinexus.Media;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class MediaController {

  private final MediaRepository repository;
  private final MediaModelAssembler assembler;

  MediaController(MediaRepository repository, MediaModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/medias/{mediaId}")
  EntityModel<Media> one(@PathVariable Long mediaId) {

    Media media = repository.findById(mediaId)
        .orElseThrow(() -> new MediaNotFoundException(mediaId));

    return assembler.toModel(media);
  }

  @GetMapping("/medias")
  CollectionModel<EntityModel<Media>> all() {
    List<EntityModel<Media>> medias = repository.findAll().stream().map(media -> assembler.toModel(media))
        .collect(Collectors.toList());

    return CollectionModel.of(medias, linkTo(methodOn(MediaController.class).all()).withSelfRel());
  }

  @PostMapping("/medias")
  ResponseEntity<?> newMedia(@RequestBody Media newMedia) {

    EntityModel<Media> entityModel = assembler.toModel(repository.save(newMedia));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/medias/{id}")
  ResponseEntity<?> editMedia(@RequestBody Media newMedia, @PathVariable Long id) {

    return repository.findById(id)
        .map(media -> {
          media.setMediaId(newMedia.getMediaId());
          media.setType(newMedia.getType());
          media.setPath(newMedia.getPath());
          EntityModel<Media> entityModel = assembler.toModel(repository.save(media));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newMedia.setMediaId(id);
          EntityModel<Media> entityModel = assembler.toModel(repository.save(newMedia));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }

  @DeleteMapping("/medias/{id}")
  ResponseEntity<?> deleteMedia(@PathVariable Long id) {

    Media media = repository.findById(id).orElseThrow(() -> new MediaNotFoundException(id));

    repository.delete(media);

    return ResponseEntity.noContent().build();

  }
}
