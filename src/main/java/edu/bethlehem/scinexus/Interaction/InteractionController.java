package edu.bethlehem.scinexus.Interaction;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class InteractionController {

  private final InteractionRepository repository;
  private final InteractionModelAssembler assembler;

  InteractionController(InteractionRepository repository, InteractionModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/interactions/{interactionId}")
  EntityModel<Interaction> one(@PathVariable Long interactionId) {

    Interaction interaction = repository.findById(interactionId)
        .orElseThrow(() -> new InteractionNotFoundException(interactionId,HttpStatus.NOT_FOUND));

    return assembler.toModel(interaction);
  }

  @GetMapping("/interactions")
  CollectionModel<EntityModel<Interaction>> all() {
    List<EntityModel<Interaction>> interactions = repository.findAll().stream()
        .map(interaction -> assembler.toModel(interaction))
        .collect(Collectors.toList());

    return CollectionModel.of(interactions, linkTo(methodOn(InteractionController.class).all()).withSelfRel());
  }

  @PostMapping("/interactions")
  ResponseEntity<?> newInteraction(@RequestBody Interaction newInteraction) {

    EntityModel<Interaction> entityModel = assembler.toModel(repository.save(newInteraction));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/interactions/{id}")
  ResponseEntity<?> editInteraction(@RequestBody Interaction newInteraction, @PathVariable Long id) {

    return repository.findById(id)
        .map(interaction -> {
          interaction.setInteractionId(newInteraction.getInteractionId());
          interaction.setType(newInteraction.getType());
          EntityModel<Interaction> entityModel = assembler.toModel(repository.save(interaction));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newInteraction.setId(id);
          EntityModel<Interaction> entityModel = assembler.toModel(repository.save(newInteraction));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }

  @PatchMapping("/interactions/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long interactionId,
      @RequestBody Interaction newInteraction) {
    Interaction interaction = repository.findById(interactionId)
        .orElseThrow(() -> new InteractionNotFoundException(interactionId,HttpStatus.NOT_FOUND));

    if (newInteraction.getInteractionId() != null)
      interaction.setInteractionId(newInteraction.getInteractionId());
    if (newInteraction.getType() != null)
      interaction.setType(newInteraction.getType());

    EntityModel<Interaction> entityModel = assembler.toModel(repository.save(interaction));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/interactions/{id}")
  ResponseEntity<?> deleteInteraction(@PathVariable Long id) {

    Interaction interaction = repository.findById(id).orElseThrow(() -> new InteractionNotFoundException(id,HttpStatus.NOT_FOUND));

    repository.delete(interaction);

    return ResponseEntity.noContent().build();

  }
}
