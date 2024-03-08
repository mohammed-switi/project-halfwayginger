package edu.bethlehem.scinexus.ResearchPaper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class ResearchPaperController {

  private final ResearchPaperRepository repository;
  private final ResearchPaperModelAssembler assembler;

  ResearchPaperController(ResearchPaperRepository repository, ResearchPaperModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/researchpapers/{researchpaperId}")
  EntityModel<ResearchPaper> one(@PathVariable Long researchpaperId) {

    ResearchPaper researchpaper = repository.findById(researchpaperId)
        .orElseThrow(() -> new ResearchPaperNotFoundException(researchpaperId));

    return assembler.toModel(researchpaper);
  }

  @GetMapping("/researchpapers")
  CollectionModel<EntityModel<ResearchPaper>> all() {
    List<EntityModel<ResearchPaper>> researchpapers = repository.findAll().stream()
        .map(researchpaper -> assembler.toModel(researchpaper))
        .collect(Collectors.toList());

    return CollectionModel.of(researchpapers, linkTo(methodOn(ResearchPaperController.class).all()).withSelfRel());
  }

  @PostMapping("/researchpapers")
  ResponseEntity<?> newResearchPaper(@RequestBody ResearchPaper newResearchPaper) {

    EntityModel<ResearchPaper> entityModel = assembler.toModel(repository.save(newResearchPaper));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/researchpapers/{id}")
  ResponseEntity<?> editResearchPaper(@RequestBody ResearchPaper newResearchPaper, @PathVariable Long id) {

    return repository.findById(id)
        .map(researchpaper -> {
          researchpaper.setDescription(newResearchPaper.getDescription());
          researchpaper.setValidatedBy(newResearchPaper.getValidatedBy());
          researchpaper.setSubject(newResearchPaper.getSubject());
          researchpaper.setLanguage(newResearchPaper.getLanguage());
          researchpaper.setNoOfPages(newResearchPaper.getNoOfPages());
          EntityModel<ResearchPaper> entityModel = assembler.toModel(repository.save(researchpaper));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newResearchPaper.setId(id);
          EntityModel<ResearchPaper> entityModel = assembler.toModel(repository.save(newResearchPaper));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }

  @DeleteMapping("/researchpapers/{id}")
  ResponseEntity<?> deleteResearchPaper(@PathVariable Long id) {

    ResearchPaper researchpaper = repository.findById(id).orElseThrow(() -> new ResearchPaperNotFoundException(id));

    repository.delete(researchpaper);

    return ResponseEntity.noContent().build();

  }
}
