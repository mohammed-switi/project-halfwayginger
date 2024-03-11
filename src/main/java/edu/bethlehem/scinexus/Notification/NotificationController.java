package edu.bethlehem.scinexus.Notification;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationController {

  private final NotificationRepository repository;
  private final NotificationModelAssembler assembler;

  NotificationController(NotificationRepository repository, NotificationModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/notifications/{notificationId}")
  EntityModel<Notification> one(@PathVariable Long notificationId) {

    Notification notification = repository.findById(notificationId)
        .orElseThrow(() -> new NotificationNotFoundException(notificationId));

    return assembler.toModel(notification);
  }

  @GetMapping("/notifications")
  CollectionModel<EntityModel<Notification>> all() {
    List<EntityModel<Notification>> notifications = repository.findAll().stream()
        .map(notification -> assembler.toModel(notification))
        .collect(Collectors.toList());

    return CollectionModel.of(notifications, linkTo(methodOn(NotificationController.class).all()).withSelfRel());
  }

  @PostMapping("/notifications")
  ResponseEntity<?> newNotification(@RequestBody Notification newNotification) {

    EntityModel<Notification> entityModel = assembler.toModel(repository.save(newNotification));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PutMapping("/notifications/{id}")
  ResponseEntity<?> editNotification(@RequestBody Notification newNotification, @PathVariable Long id) {

    return repository.findById(id)
        .map(notification -> {
          notification.setNotificationId(newNotification.getNotificationId());
          notification.setContent(newNotification.getContent());
          notification.setStatus(newNotification.getStatus());
          EntityModel<Notification> entityModel = assembler.toModel(repository.save(notification));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        })
        .orElseGet(() -> {
          newNotification.setId(id);
          EntityModel<Notification> entityModel = assembler.toModel(repository.save(newNotification));
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        });
  }

  @PatchMapping("/notifications/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long notificationId,
      @RequestBody Notification newNotification) {
    Notification notification = repository.findById(notificationId)
        .orElseThrow(() -> new NotificationNotFoundException(notificationId));
    if (newNotification.getContent() != null)
      notification.setNotificationId(newNotification.getNotificationId());
    if (newNotification.getContent() != null)
      notification.setContent(newNotification.getContent());
    if (newNotification.getStatus() != null)
      notification.setStatus(newNotification.getStatus());

    EntityModel<Notification> entityModel = assembler.toModel(repository.save(notification));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/notifications/{id}")
  ResponseEntity<?> deleteNotification(@PathVariable Long id) {

    Notification notification = repository.findById(id).orElseThrow(() -> new NotificationNotFoundException(id));

    repository.delete(notification);

    return ResponseEntity.noContent().build();

  }
}
