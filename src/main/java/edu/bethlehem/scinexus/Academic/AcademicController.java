package edu.bethlehem.scinexus.Academic;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class AcademicController {

  private final AcademicRepository repository;
  private final AcademicModelAssembler assembler;

  AcademicController(AcademicRepository repository, AcademicModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/academics/{academicId}")
  EntityModel<Academic> one(@PathVariable Long academicId) {

    Academic academic = repository.findById(academicId)
        .orElseThrow(() -> new AcademicNotFoundException(academicId));

    return assembler.toModel(academic);
  }

  @GetMapping("/academics")
  CollectionModel<EntityModel<Academic>> all() {
    List<EntityModel<Academic>> academics = repository.findAll().stream().map(academic -> assembler.toModel(academic))
        .collect(Collectors.toList());

    return CollectionModel.of(academics, linkTo(methodOn(AcademicController.class).all()).withSelfRel());
  }

  @PostMapping("/academics")
  ResponseEntity<?> newAcademic(@RequestBody Academic newAcademic) {

    EntityModel<Academic> entityModel = assembler.toModel(repository.save(newAcademic));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/academics/{id}")
  ResponseEntity<?> editAcademic(@RequestBody Academic newAcademic, @PathVariable Long id) {

    return repository.findById(id)
        .map(academic -> {
          academic.setBadge(newAcademic.getBadge());
          academic.setEducation(newAcademic.getEducation());
          academic.setOrganizationId(newAcademic.getOrganizationId());
          academic.setPosition(newAcademic.getPosition());
          EntityModel<Academic> entityModel = assembler.toModel(repository.save(academic));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newAcademic.setId(id);
          EntityModel<Academic> entityModel = assembler.toModel(repository.save(newAcademic));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }

  @DeleteMapping("/academics/{id}")
  ResponseEntity<?> deleteAcademic(@PathVariable Long id) {

    Academic academic = repository.findById(id).orElseThrow(() -> new AcademicNotFoundException(id));

    repository.delete(academic);

    return ResponseEntity.noContent().build();

  }
}
