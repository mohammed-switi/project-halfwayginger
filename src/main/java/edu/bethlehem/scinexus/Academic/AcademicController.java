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

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long academicId,
      @RequestBody AcademicRequestPatchDTO newAcademic) {
    return new ResponseEntity<>(service.updateAcademicPartially(academicId, newAcademic), HttpStatus.CREATED);
  }

}
