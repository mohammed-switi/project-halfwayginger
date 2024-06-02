package edu.bethlehem.scinexus.Notification;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")

public class NotificationController {

  private final NotificationService service;

  @GetMapping("/{notificationId}")
  EntityModel<Notification> one(@PathVariable Long notificationId) {

    return service.findNotificationById(notificationId);
  }

  @GetMapping()
  CollectionModel<EntityModel<Notification>> all() {
    return service.findAllNotifications();
  }

  @GetMapping("/mine")
  CollectionModel<EntityModel<Notification>> mine(Authentication authentication) {
    return service.findMyNotifications(authentication);
  }

  // @PostMapping("/{userId}")
  // ResponseEntity<?> newNotification(@RequestBody String content, @PathVariable
  // Long userId) {

  // return ResponseEntity.ok(service.createNotification(content, userId));
  // }

  // @DeleteMapping("/{notificationId}")
  // ResponseEntity<?> deleteNotification(@PathVariable Long notificationId) {

  // service.deleteNotification(notificationId);

  // return ResponseEntity.noContent().build();

  // }
}
