package edu.bethlehem.scinexus.Organization;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrganizationController {

  private final OrganizationRepository repository;
  private final OrganizationModelAssembler assembler;

  OrganizationController(OrganizationRepository repository, OrganizationModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/organizations/{organizationId}")
  EntityModel<Organization> one(@PathVariable Long organizationId) {

    Organization organization = repository.findById(organizationId)
        .orElseThrow(() -> new OrganizationNotFoundException(organizationId,HttpStatus.NOT_FOUND));

    return assembler.toModel(organization);
  }

  @GetMapping("/organizations")
  CollectionModel<EntityModel<Organization>> all() {
    List<EntityModel<Organization>> organizations = repository.findAll().stream()
        .map(organization -> assembler.toModel(organization))
        .collect(Collectors.toList());

    return CollectionModel.of(organizations, linkTo(methodOn(OrganizationController.class).all()).withSelfRel());
  }

  @PostMapping("/organizations")
  ResponseEntity<?> newOrganization(@RequestBody Organization newOrganization) {

    EntityModel<Organization> entityModel = assembler.toModel(repository.save(newOrganization));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/organizations/{id}")
  ResponseEntity<?> editOrganization(@RequestBody Organization newOrganization, @PathVariable Long id) {

    return repository.findById(id)
        .map(organization -> {
          organization.setType(newOrganization.getType());
          organization.setVerified(newOrganization.getVerified());

          // User Properties
          organization.setUsername(newOrganization.getUsername());
          organization.setPassword(newOrganization.getPassword());
          organization.setEmail(newOrganization.getEmail());
          organization.setProfilePicture(newOrganization.getProfilePicture());
          organization.setProfileCover(newOrganization.getProfileCover());
          organization.setBio(newOrganization.getBio());
          organization.setPhoneNumber(newOrganization.getPhoneNumber());
          organization.setFieldOfWork(newOrganization.getFieldOfWork());
          organization.setUserSettings(newOrganization.getUserSettings());
          organization.setName(newOrganization.getName());

          EntityModel<Organization> entityModel = assembler.toModel(repository.save(organization));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newOrganization.setId(id);
          EntityModel<Organization> entityModel = assembler.toModel(repository.save(newOrganization));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }

  @PatchMapping("/organizations/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long organizationId,
      @RequestBody Organization newOrganization) {
    Organization organization = repository.findById(organizationId)
        .orElseThrow(() -> new OrganizationNotFoundException(organizationId,HttpStatus.NOT_FOUND));
    if (newOrganization.getType() != null)
      organization.setType(newOrganization.getType());
    if (newOrganization.getVerified() != null)
      organization.setVerified(newOrganization.getVerified());

    // User Properties
    if (newOrganization.getUsername() != null)
      organization.setUsername(newOrganization.getUsername());
    if (newOrganization.getPassword() != null)
      organization.setPassword(newOrganization.getPassword());
    if (newOrganization.getEmail() != null)
      organization.setEmail(newOrganization.getEmail());
    if (newOrganization.getProfilePicture() != null)
      organization.setProfilePicture(newOrganization.getProfilePicture());
    if (newOrganization.getProfileCover() != null)
      organization.setProfileCover(newOrganization.getProfileCover());
    if (newOrganization.getBio() != null)
      organization.setBio(newOrganization.getBio());
    if (newOrganization.getPhoneNumber() != null)
      organization.setPhoneNumber(newOrganization.getPhoneNumber());
    if (newOrganization.getFieldOfWork() != null)
      organization.setFieldOfWork(newOrganization.getFieldOfWork());
    if (newOrganization.getUserSettings() != null)
      organization.setUserSettings(newOrganization.getUserSettings());
    if (newOrganization.getName() != null)
      organization.setName(newOrganization.getName());

    EntityModel<Organization> entityModel = assembler.toModel(repository.save(organization));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/organizations/{id}")
  ResponseEntity<?> deleteOrganization(@PathVariable Long id) {

    Organization organization = repository.findById(id).orElseThrow(() -> new OrganizationNotFoundException(id,HttpStatus.NOT_FOUND));

    repository.delete(organization);

    return ResponseEntity.noContent().build();

  }
}
