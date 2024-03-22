package edu.bethlehem.scinexus.Academic;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/academics")
public class AcademicController {

  private final AcademicRepository repository;
  private final AcademicModelAssembler assembler;

  AcademicController(AcademicRepository repository, AcademicModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  // Gets one Academic by id
  @GetMapping("/{academicId}")
  EntityModel<Academic> one(@PathVariable Long academicId) throws AcademicNotFoundException {
    System.out.println("Academic ID: " + academicId);
    Academic academic = repository.findById(academicId)
        .orElseThrow(() -> new AcademicNotFoundException(academicId, HttpStatus.NOT_FOUND));

    return assembler.toModel(academic);
  }

  @GetMapping()
  CollectionModel<EntityModel<Academic>> all() {
    List<EntityModel<Academic>> academics = repository.findAll().stream().map(academic -> assembler.toModel(academic))
        .collect(Collectors.toList());

    return CollectionModel.of(academics, linkTo(methodOn(AcademicController.class).all()).withSelfRel());
  }

  @PostMapping()
  ResponseEntity<?> newAcademic( @Valid @RequestBody Academic newAcademic) {

    EntityModel<Academic> entityModel = assembler.toModel(repository.save(newAcademic));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/{id}")
  ResponseEntity<?> editAcademic(@Valid @RequestBody Academic newAcademic, @PathVariable Long id) {

    return repository.findById(id)
        .map(academic -> {
          academic.setBadge(newAcademic.getBadge());
          academic.setEducation(newAcademic.getEducation());
          academic.setOrganization(newAcademic.getOrganization());
          academic.setPosition(newAcademic.getPosition());
          // User Properties
          academic.setUsername(newAcademic.getUsername());
          academic.setPassword(newAcademic.getPassword());
          academic.setEmail(newAcademic.getEmail());
          academic.setProfilePicture(newAcademic.getProfilePicture());
          academic.setProfileCover(newAcademic.getProfileCover());
          academic.setBio(newAcademic.getBio());
          academic.setPhoneNumber(newAcademic.getPhoneNumber());
          academic.setFieldOfWork(newAcademic.getFieldOfWork());
          academic.setUserSettings(newAcademic.getUserSettings());
          academic.setFirstName(newAcademic.getFirstName());

          EntityModel<Academic> entityModel = assembler.toModel(repository.save(academic));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newAcademic.setId(id);
          EntityModel<Academic> entityModel = assembler.toModel(repository.save(newAcademic));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }
  // @PutMapping("/{academicId}")
  // ResponseEntity<?> validateAcademic(@PathVariable Long academicId) {

  // return repository.findById(
  // academicId)
  // .map(academic -> {

  // EntityModel<Academic> entityModel =
  // assembler.toModel(repository.save(academic));
  // return
  // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  // })
  // .orElseGet(() -> {
  // newAcademic.setId(id);
  // EntityModel<Academic> entityModel =
  // assembler.toModel(repository.save(newAcademic));
  // return
  // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  // });
  // }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long academicId,
      @RequestBody Academic newAcademic) throws AcademicNotFoundException {
    Academic academic = repository.findById(academicId)
        .orElseThrow(() -> new AcademicNotFoundException(academicId, HttpStatus.NOT_FOUND));

    if (newAcademic.getBadge() != null)
      academic.setBadge(newAcademic.getBadge());
    if (newAcademic.getEducation() != null)
      academic.setEducation(newAcademic.getEducation());
    if (newAcademic.getOrganization() != null)
      academic.setOrganization(newAcademic.getOrganization());
    if (newAcademic.getPosition() != null)
      academic.setPosition(newAcademic.getPosition());
    // User Properties
    if (newAcademic.getUsername() != null)
      academic.setUsername(newAcademic.getUsername());
    if (newAcademic.getPassword() != null)
      academic.setPassword(newAcademic.getPassword());
    if (newAcademic.getEmail() != null)
      academic.setEmail(newAcademic.getEmail());
    if (newAcademic.getProfilePicture() != null)
      academic.setProfilePicture(newAcademic.getProfilePicture());
    if (newAcademic.getProfileCover() != null)
      academic.setProfileCover(newAcademic.getProfileCover());
    if (newAcademic.getBio() != null)
      academic.setBio(newAcademic.getBio());
    if (newAcademic.getPhoneNumber() != null)
      academic.setPhoneNumber(newAcademic.getPhoneNumber());
    if (newAcademic.getFieldOfWork() != null)
      academic.setFieldOfWork(newAcademic.getFieldOfWork());
    if (newAcademic.getUserSettings() != null)
      academic.setUserSettings(newAcademic.getUserSettings());
    if (newAcademic.getFirstName() != null)
      academic.setFirstName(newAcademic.getFirstName());

    EntityModel<Academic> entityModel = assembler.toModel(repository.save(academic));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteAcademic(@PathVariable Long id) throws AcademicNotFoundException {

    Academic academic = repository.findById(id)
        .orElseThrow(() -> new AcademicNotFoundException(id, HttpStatus.NOT_FOUND));

    repository.delete(academic);

    return ResponseEntity.noContent().build();

  }
}
