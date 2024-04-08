package edu.bethlehem.scinexus.Notification;

import java.util.List;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/push-notifications")
@AllArgsConstructor
public class PushNotificationController {

    private final NotificationService service;

    @GetMapping()
    public Flux<ServerSentEvent<List<Notification>>> streamLastMessage(Authentication auth) {
        return service.getNotificationsByUserToID(auth);
    }

}