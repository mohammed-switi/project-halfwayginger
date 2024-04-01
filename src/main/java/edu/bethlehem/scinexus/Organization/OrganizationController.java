package edu.bethlehem.scinexus.Organization;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.JPARepository.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
public class OrganizationController {

  private final UserRepository repository;
  private final OrganizationModelAssembler assembler;
  private final OrganizationService service;

  @GetMapping("/{organizationId}")
  EntityModel<User> one(@PathVariable Long organizationId) {
    return service.findOrganizationById(organizationId);
  }

  @GetMapping("")
  CollectionModel<EntityModel<User>> all() {
    return service.findAllOrganizations();
  }

  @PutMapping("/{id}")
  ResponseEntity<?> editOrganization(@Valid @RequestBody OrganizationRequestDTO newOrganization,
      @PathVariable Long id) {
    return ResponseEntity.ok(service.updateOrganization(id, newOrganization));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long organizationId,
      @Valid @RequestBody OrganizationRequestPatchDTO newOrganization) {
    return ResponseEntity.ok(service.updateOrganizationPartially(organizationId, newOrganization));
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
    service.deleteOrganization(id);

    return ResponseEntity.noContent().build();

  }
}
