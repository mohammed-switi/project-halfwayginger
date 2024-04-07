package edu.bethlehem.scinexus.Organization;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.JPARepository.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
public class OrganizationController {


  private final OrganizationService service;

  @GetMapping("/{organizationId}")
  EntityModel<User> one(@PathVariable Long organizationId) {
    return service.findOrganizationById(organizationId);
  }

  @GetMapping("")
  CollectionModel<EntityModel<User>> all() {
    return service.findAllOrganizations();
  }

  // @PutMapping("/{id}")
  // ResponseEntity<?> editOrganization(@Valid @RequestBody OrganizationRequestDTO
  // newOrganization,
  // @PathVariable Long id) {
  // return ResponseEntity.ok(service.updateOrganization(id, newOrganization));
  // }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateOrganizationPartially(@PathVariable(value = "id") Long organizationId,
       @RequestBody OrganizationRequestPatchDTO newOrganization) {
    return new ResponseEntity<>(service.updateOrganizationPartially(organizationId, newOrganization),HttpStatus.CREATED);
    //return ResponseEntity.ok(service.updateOrganizationPartially(organizationId, newOrganization));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
    service.deleteOrganization(id);

    return ResponseEntity.noContent().build();

  }

  @DeleteMapping("/{academicId}/organization")
  public ResponseEntity<?> removeUserFromOrganization(@PathVariable Long academicId, Authentication auth) {
    service.removeAcademic(academicId, auth);
    return ResponseEntity.noContent().build();

  }

  @PatchMapping("/{academicId}/organization")
  public ResponseEntity<?> addUserToOrganization(@PathVariable Long academicId, Authentication authentication) {

    return ResponseEntity.ok(service.addAcademic(authentication, academicId));
  }
}
