package edu.bethlehem.scinexus.Opinion;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/opinions")
public class OpinionController {

  private final OpinionService opinionService;
  private final OpinionRepository repository;
  private final OpinionModelAssembler assembler;

  @GetMapping("/{opinionId}")
  public EntityModel<Opinion> one(@PathVariable Long opinionId) {

    return opinionService.getOneOpinion(opinionId);
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

  @PatchMapping("{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long opinionId,
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
