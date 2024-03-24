package edu.bethlehem.scinexus.ResearchPaper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/researchpapers")
public class ResearchPaperController {

  private final ResearchPaperRepository repository;
  private final UserRepository userRepository;
  private final ResearchPaperModelAssembler assembler;

  @GetMapping("/{researchpaperId}")
  EntityModel<ResearchPaper> one(@PathVariable Long researchpaperId) {

    ResearchPaper researchpaper = repository.findById(researchpaperId)
        .orElseThrow(() -> new ResearchPaperNotFoundException(researchpaperId, HttpStatus.NOT_FOUND));

    return assembler.toModel(researchpaper);
  }

  @GetMapping()
  CollectionModel<EntityModel<ResearchPaper>> all() {
    List<EntityModel<ResearchPaper>> researchpapers = repository.findAll().stream()
        .map(researchpaper -> assembler.toModel(researchpaper))
        .collect(Collectors.toList());

    return CollectionModel.of(researchpapers, linkTo(methodOn(ResearchPaperController.class).all()).withSelfRel());
  }

  @PostMapping()
  ResponseEntity<?> newResearchPaper(@RequestBody ResearchPaper newResearchPaper, Authentication authentication) {

    User userObj = (User) authentication.getPrincipal();
    User user = userRepository.findById(userObj.getId())
        .orElseThrow(() -> new UserNotFoundException(userObj.getId().toString(), HttpStatus.NOT_FOUND));
    newResearchPaper.setPublisher(user);
    EntityModel<ResearchPaper> entityModel = assembler.toModel(repository.save(newResearchPaper));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/{id}")
  ResponseEntity<?> editResearchPaper(@RequestBody ResearchPaper newResearchPaper, @PathVariable Long id) {

    return repository.findById(id)
        .map(researchpaper -> {
          researchpaper.setDescription(newResearchPaper.getDescription());
          researchpaper.setValidatedBy(newResearchPaper.getValidatedBy());
          researchpaper.setDescription(newResearchPaper.getDescription());
          researchpaper.setSubject(newResearchPaper.getSubject());
          researchpaper.setTitle(newResearchPaper.getTitle());
          researchpaper.setLanguage(newResearchPaper.getLanguage());
          researchpaper.setPublisher(newResearchPaper.getPublisher());
          researchpaper.setVisibility(newResearchPaper.getVisibility());
          researchpaper.setContributors(newResearchPaper.getContributors());
          EntityModel<ResearchPaper> entityModel = assembler.toModel(repository.save(researchpaper));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newResearchPaper.setId(id);
          EntityModel<ResearchPaper> entityModel = assembler.toModel(repository.save(newResearchPaper));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long researchpaperId,
      @RequestBody ResearchPaper newResearchPaper) {
    ResearchPaper researchpaper = repository.findById(researchpaperId)
        .orElseThrow(() -> new ResearchPaperNotFoundException(researchpaperId, HttpStatus.NOT_FOUND));

    if (newResearchPaper.getDescription() != null)
      researchpaper.setDescription(newResearchPaper.getDescription());
    if (newResearchPaper.getValidatedBy() != null)
      researchpaper.setValidatedBy(newResearchPaper.getValidatedBy());
    if (newResearchPaper.getDescription() != null)
      researchpaper.setDescription(newResearchPaper.getDescription());
    if (newResearchPaper.getSubject() != null)
      researchpaper.setSubject(newResearchPaper.getSubject());
    if (newResearchPaper.getTitle() != null)
      researchpaper.setTitle(newResearchPaper.getTitle());
    if (newResearchPaper.getLanguage() != null)
      researchpaper.setLanguage(newResearchPaper.getLanguage());
    // if (userRepository.existsById(newResearchPaper.getPublisherId()))
    // researchpaper.setPublisherId(newResearchPaper.getPublisherId());
    if (newResearchPaper.getVisibility() != null)
      researchpaper.setVisibility(newResearchPaper.getVisibility());
    if (newResearchPaper.getContributors() != null)
      researchpaper.setContributors(newResearchPaper.getContributors());

    EntityModel<ResearchPaper> entityModel = assembler.toModel(repository.save(researchpaper));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteResearchPaper(@PathVariable Long id) {

    ResearchPaper researchpaper = repository.findById(id)
        .orElseThrow(() -> new ResearchPaperNotFoundException(id, HttpStatus.NOT_FOUND));

    repository.delete(researchpaper);

    return ResponseEntity.noContent().build();

  }
}
