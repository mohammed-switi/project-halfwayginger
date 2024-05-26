package edu.bethlehem.scinexus.Opinion;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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

import edu.bethlehem.scinexus.Interaction.Interaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/opinions")
public class OpinionController {

  private final OpinionService opinionService;

  @GetMapping("/{opinionId}")
  public EntityModel<Opinion> one(@PathVariable Long opinionId) {

    return opinionService.getOneOpinion(opinionId);
  }

  @GetMapping("/{opinionId}/interactions")
  public CollectionModel<EntityModel<Interaction>> interactions(@PathVariable Long opinionId) {

    return opinionService.getInteractions(opinionId);
  }

  @GetMapping()
  CollectionModel<EntityModel<Opinion>> all() {

    return CollectionModel.of(opinionService.getAllOpinions(),
        linkTo(methodOn(OpinionController.class).all()).withSelfRel());
  }

  @PostMapping()
  ResponseEntity<?> newOpinion(@RequestBody @Valid OpinionDTO newOpinion, Authentication auth) {

    EntityModel<Opinion> entityModel = opinionService.postOpinion(newOpinion, auth);

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PostMapping("/subOpinion")
  ResponseEntity<?> newOpinionToOpinion(@RequestBody @Valid OpinionDTO newOpinion, Authentication auth) {

    EntityModel<Opinion> entityModel = opinionService.postOpinionToOpinion(newOpinion, auth);

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  // We Won't need a PUT method
  // @PutMapping("/{id}")
  // public ResponseEntity<?> editOpinion(@RequestBody @Valid OpinionDTO
  // newOpinion,
  // @PathVariable Long id) {
  // EntityModel<Opinion> entityModel = opinionService.updateOpinion(id,
  // newOpinion);
  // return
  // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);

  // }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateOpinionPartially(@PathVariable(value = "id") Long opinionId,
      @RequestBody @Valid OpinionPatchDTO newOpinion) {

    EntityModel<Opinion> entityModel = opinionService.updateOpinionPartially(opinionId, newOpinion);

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOpinion(@PathVariable Long id) {

    opinionService.deleteOpinion(id);
    return ResponseEntity.noContent().build();

  }

}
