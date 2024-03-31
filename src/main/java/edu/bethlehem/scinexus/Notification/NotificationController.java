package edu.bethlehem.scinexus.Notification;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")

public class NotificationController {

  private final NotificationRepository repository;
  private final NotificationModelAssembler assembler;
  private final NotificationService service;

  @GetMapping("/{notificationId}")
  EntityModel<Notification> one(@PathVariable Long notificationId) {

    return service.findNotificationById(notificationId);
  }

  @GetMapping()
  CollectionModel<EntityModel<Notification>> all() {
    return service.findAllNotifications();
  }

  @PostMapping("/{userId}")
  ResponseEntity<?> newNotification(@RequestBody String content, @PathVariable Long userId) {

    return ResponseEntity.ok(service.createNotification(content, userId));
  }

  @DeleteMapping("/{notificationId}")
  ResponseEntity<?> deleteNotification(@PathVariable Long notificationId) {

    service.deleteNotification(notificationId);

    return ResponseEntity.noContent().build();

  }
}
