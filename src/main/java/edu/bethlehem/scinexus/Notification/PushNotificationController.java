package edu.bethlehem.scinexus.Notification;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/push-notifications")
@Slf4j
@AllArgsConstructor
public class PushNotificationController {

    private final NotificationService service;

    @GetMapping()
    public Flux<ServerSentEvent<List<Notification>>> streamLastMessage(Authentication auth) {
        return service.getNotificationsByUserToID(auth);
    }

}