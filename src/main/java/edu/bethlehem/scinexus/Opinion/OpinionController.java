package edu.bethlehem.scinexus.Opinion;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class OpinionController {

  private final OpinionRepository repository;
  private final OpinionModelAssembler assembler;

  OpinionController(OpinionRepository repository, OpinionModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/opinions/{opinionId}")
  EntityModel<Opinion> one(@PathVariable Long opinionId) {

    Opinion opinion = repository.findById(opinionId)
        .orElseThrow(() -> new OpinionNotFoundException(opinionId));

    return assembler.toModel(opinion);
  }

  @GetMapping("/opinions")
  CollectionModel<EntityModel<Opinion>> all() {
    List<EntityModel<Opinion>> opinions = repository.findAll().stream().map(opinion -> assembler.toModel(opinion))
        .collect(Collectors.toList());

    return CollectionModel.of(opinions, linkTo(methodOn(OpinionController.class).all()).withSelfRel());
  }

  @PostMapping("/opinions")
  ResponseEntity<?> newOpinion(@RequestBody Opinion newOpinion) {

    EntityModel<Opinion> entityModel = assembler.toModel(repository.save(newOpinion));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/opinions/{id}")
  ResponseEntity<?> editOpinion(@RequestBody Opinion newOpinion, @PathVariable Long id) {

    return repository.findById(id)
        .map(opinion -> {
          opinion.setOpinionId(newOpinion.getOpinionId());
          opinion.setContent(newOpinion.getContent());
          EntityModel<Opinion> entityModel = assembler.toModel(repository.save(opinion));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newOpinion.setId(id);
          EntityModel<Opinion> entityModel = assembler.toModel(repository.save(newOpinion));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }

  @PatchMapping("/opinions/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long opinionId,
      @RequestBody Opinion newOpinion) {
    Opinion opinion = repository.findById(opinionId)
        .orElseThrow(() -> new OpinionNotFoundException(opinionId));
    if (newOpinion.getContent() != null)
      opinion.setOpinionId(newOpinion.getOpinionId());
    if (newOpinion.getContent() != null)
      opinion.setContent(newOpinion.getContent());

    EntityModel<Opinion> entityModel = assembler.toModel(repository.save(opinion));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/opinions/{id}")
  ResponseEntity<?> deleteOpinion(@PathVariable Long id) {

    Opinion opinion = repository.findById(id).orElseThrow(() -> new OpinionNotFoundException(id));

    repository.delete(opinion);

    return ResponseEntity.noContent().build();

  }
}
