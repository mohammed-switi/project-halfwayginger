package edu.bethlehem.scinexus.Academic;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.User.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/academics")
public class AcademicController {

  private final AcademicService service;

  // Gets one Academic by id
  @GetMapping("/{academicId}")
  public ResponseEntity<EntityModel<User>> one(@PathVariable Long academicId) throws AcademicNotFoundException {
    return ResponseEntity.ok(service.findAcademicById(academicId));
  }

  public @GetMapping() ResponseEntity<CollectionModel<EntityModel<User>>> all() {

    return ResponseEntity.ok(service.findAllAcademics());
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
    return new ResponseEntity<>(service.updateAcademicPartially(academicId, newAcademic), HttpStatus.CREATED);
  }

  // We will be deleteing a user
  // @DeleteMapping("/{id}")
  // ResponseEntity<?> deleteAcademic(@PathVariable Long id) throws
  // AcademicNotFoundException {

  // service.deleteAcademic(id);

  // return ResponseEntity.noContent().build();

  // }
}
