package edu.bethlehem.scinexus.Academic;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.JPARepository.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/academics")
public class AcademicController {

  private final UserRepository repository;
  private final AcademicModelAssembler assembler;
  private final AcademicService service;

  // Gets one Academic by id
  @GetMapping("/{academicId}")
  EntityModel<User> one(@PathVariable Long academicId) throws AcademicNotFoundException {
    return service.findAcademicById(academicId);
  }

  @GetMapping()
  CollectionModel<EntityModel<User>> all() {
    return service.findAllAcademics();
  }

  // No need for any PUT Method
  // @PutMapping("/{id}")
  // ResponseEntity<?> editAcademic(@Valid @RequestBody AcademicRequestDTO
  // newAcademic, @PathVariable Long id) {

  // return ResponseEntity.ok(service.updateAcademic(id, newAcademic));
  // }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long academicId,
      @RequestBody AcademicRequestPatchDTO newAcademic) {
    return ResponseEntity.ok(service.updateAcademicPartially(academicId, newAcademic));
  }

  // We will be deleteing a user
  // @DeleteMapping("/{id}")
  // ResponseEntity<?> deleteAcademic(@PathVariable Long id) throws
  // AcademicNotFoundException {

  // service.deleteAcademic(id);

  // return ResponseEntity.noContent().build();

  // }
}
