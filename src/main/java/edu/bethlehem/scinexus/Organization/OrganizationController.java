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
        .orElseThrow(() -> new OrganizationNotFoundException(organizationId));

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
          EntityModel<Organization> entityModel = assembler.toModel(repository.save(organization));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newOrganization.setId(id);
          EntityModel<Organization> entityModel = assembler.toModel(repository.save(newOrganization));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }

  @DeleteMapping("/organizations/{id}")
  ResponseEntity<?> deleteOrganization(@PathVariable Long id) {

    Organization organization = repository.findById(id).orElseThrow(() -> new OrganizationNotFoundException(id));

    repository.delete(organization);

    return ResponseEntity.noContent().build();

  }
}
