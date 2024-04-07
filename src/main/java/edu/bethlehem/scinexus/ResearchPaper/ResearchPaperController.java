package edu.bethlehem.scinexus.ResearchPaper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.UserResearchPaper.UserResearchPaperRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/researchpapers")
public class ResearchPaperController {

  private final ResearchPaperService service;

  @GetMapping("/{researchpaperId}")
  ResponseEntity<EntityModel<ResearchPaper>> one(@PathVariable Long researchpaperId) {

    return ResponseEntity.ok(service.findResearchPaperById(researchpaperId));

  }

  @GetMapping()
  public CollectionModel<EntityModel<ResearchPaper>> all() {

    List<EntityModel<ResearchPaper>> researchpapers = service.findAllResearchPapers();

    return CollectionModel.of(researchpapers, linkTo(methodOn(ResearchPaperController.class).all()).withSelfRel());
  }

  @PostMapping()
  ResponseEntity<?> newResearchPaper(@RequestBody @Valid ResearchPaperRequestDTO newResearchPaperDTO,
      Authentication authentication) {

    EntityModel<ResearchPaper> entityModel = service.createResearchPaper(newResearchPaperDTO,
        authentication);

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PostMapping("{researchPaperId}/validate")
  ResponseEntity<?> validateResearchPaper(
      @PathVariable @NotNull Long researchPaperId,
      Authentication authentication) {

    EntityModel<ResearchPaper> entityModel = service.validate(researchPaperId,
        authentication);

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PostMapping("{researchPaperId}/access")
  public ResponseEntity<?> requestResearchPaperAccess(@PathVariable @NotNull Long researchPaperId,
      Authentication authentication) {
    EntityModel<ResearchPaper> entityModel = service.requestAccessToResearchPaper(researchPaperId,
        authentication);
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
        .toUri()).body(entityModel);
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
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);

  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteResearchPaper(@PathVariable Long id) {

    service.deleteResearchPaper(id);

    return ResponseEntity.noContent().build();

  }
}
