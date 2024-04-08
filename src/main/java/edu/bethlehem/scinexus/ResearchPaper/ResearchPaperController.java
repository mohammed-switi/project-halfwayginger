package edu.bethlehem.scinexus.ResearchPaper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/researchpapers")
public class ResearchPaperController {

  private final ResearchPaperService service;

  @GetMapping("/{researchpaperId}")
  public ResponseEntity<EntityModel<ResearchPaper>> one(@PathVariable Long researchpaperId) {

    return ResponseEntity.ok(service.findResearchPaperById(researchpaperId));

  }

  @GetMapping()
  public ResponseEntity<CollectionModel<EntityModel<ResearchPaper>>> all() {

    CollectionModel<EntityModel<ResearchPaper>> researchpapers = service.findAllResearchPapers();

    return ResponseEntity
        .ok(CollectionModel.of(researchpapers, linkTo(methodOn(ResearchPaperController.class).all()).withSelfRel()));
  }

  @PostMapping()
  public ResponseEntity<?> newResearchPaper(@RequestBody @Valid ResearchPaperRequestDTO newResearchPaperDTO,
      Authentication authentication) {

    EntityModel<ResearchPaper> entityModel = service.createResearchPaper(newResearchPaperDTO,
        authentication);
    return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    // return
    // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PostMapping("{researchPaperId}/validate")
  ResponseEntity<?> validateResearchPaper(
      @PathVariable @NotNull Long researchPaperId,
      Authentication authentication) {

    EntityModel<ResearchPaper> entityModel = service.validate(researchPaperId,
        authentication);

    return new ResponseEntity<>(entityModel, HttpStatus.OK);
    // return
    // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PostMapping("{researchPaperId}/access")
  public ResponseEntity<?> requestResearchPaperAccess(@PathVariable @NotNull Long researchPaperId,
      Authentication authentication) {
    EntityModel<ResearchPaper> entityModel = service.requestAccessToResearchPaper(researchPaperId,
        authentication);
    return new ResponseEntity<>(entityModel, HttpStatus.OK);
    // return
    // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
    // .toUri()).body(entityModel);
  }

  @PostMapping("{researchPaperId}/access/{userId}")
  public ResponseEntity<?> respondToRequestResearchPaperAccess(@PathVariable @NotNull Long researchPaperId,
      @PathVariable Long userId, @RequestBody Boolean answer) {
    EntityModel<ResearchPaper> entityModel = service.respondToRequest(answer, researchPaperId,
        userId);
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
        .toUri()).body(entityModel);
  }

  // @PutMapping("/{id}")
  // ResponseEntity<?> editResearchPaper(@RequestBody @Valid
  // ResearchPaperRequestDTO newResearchPaperDTO,
  // @PathVariable @NotNull Long id) {

  // EntityModel<ResearchPaper> entityModel = service.updateResearchPaper(id,
  // newResearchPaperDTO);
  // return
  // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  // }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateResearchPaperPartially(@PathVariable(value = "id") Long researchpaperId,
      @RequestBody @NotNull ResearchPaperRequestPatchDTO newResearchPaperDTO) {
    EntityModel<ResearchPaper> entityModel = service.updateResearchPaperPartially(
        researchpaperId, newResearchPaperDTO);
    return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    // return
    // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);

  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteResearchPaper(@PathVariable Long id) {

    service.deleteResearchPaper(id);

    return ResponseEntity.noContent().build();

  }
}
