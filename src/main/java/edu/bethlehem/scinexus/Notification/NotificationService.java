package edu.bethlehem.scinexus.Notification;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import edu.bethlehem.scinexus.JPARepository.NotificationRepository;
import edu.bethlehem.scinexus.JPARepository.UserLinksRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.UserLinks.UserLinks;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationModelAssembler assembler;
    private final UserLinksRepository userLinksRepository;
    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public Notification convertNotificationDtoToNotificationEntity(NotificationRequestDTO NotificationRequestDTO) {

        return Notification.builder()
                .content(NotificationRequestDTO.getContent())
                .status(Status.UNSEEN)
                .build();
    }

    public User getUserById(long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

    }

    public User getUserById(Authentication auth) {
        Long id = jwtService.extractId(auth);
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

    }

    public EntityModel<Notification> findNotificationById(Long NotificationId) {
        Notification notification = notificationRepository.findById(
                NotificationId)
                .orElseThrow(() -> new NotificationNotFoundException(NotificationId, HttpStatus.NOT_FOUND));

        return assembler.toModel(notification);
    }

    // We Should Specify An Admin Authority To get All Notifications
    public CollectionModel<EntityModel<Notification>> findAllNotifications() {

        List<EntityModel<Notification>> notifications = notificationRepository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(notifications, linkTo(methodOn(NotificationController.class).all()).withSelfRel());
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    // Notifications are made within the application

    // public EntityModel<Notification> createNotification(String content,
    // Long userId) {
    // Notification notification = new Notification();
    // notification.setContent(content);
    // System.out.println("here");
    // notification.setUser(getUserById(userId));
    // notification.setStatus(Status.UNSEEN);
    // return assembler.toModel(saveNotification(notification));
    // }

    // You cant delete a notification

    // public void deleteNotification(Long notificationId) {
    // Notification notification = notificationRepository.findById(notificationId)
    // .orElseThrow(
    // () -> new NotificationNotFoundException(notificationId,
    // HttpStatus.UNPROCESSABLE_ENTITY));
    // notificationRepository.delete(notification);
    // }

    private List<Notification> getNotifs(Authentication auth) {
        User user = jwtService.getUser(auth);
        var notifs = notificationRepository.findByUserAndStatus(user, Status.UNSEEN);
        notifs.forEach(x -> x.setStatus(Status.SENT));
        notificationRepository.saveAll(notifs);
        return notifs;
    }

    public Flux<ServerSentEvent<List<Notification>>> getNotificationsByUserToID(Authentication auth) {
        Long userID = jwtService.extractId(auth);
        if (userID != null) {
            System.out.println("User ID: " + userID);
            return Flux.interval(Duration.ofSeconds(15))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<List<Notification>>builder().id(String.valueOf(sequence))
                            .event("user-list-event").data(getNotifs(auth))
                            .build());
        }

        return Flux.interval(Duration.ofSeconds(15)).map(sequence -> ServerSentEvent.<List<Notification>>builder()
                .id(String.valueOf(sequence)).event("user").data(getNotifs(auth)).build());
    }

    public void notifyLinks(Long userId, String content, WebMvcLinkBuilder hyperLink) {
        logger.debug("sending notifications to user links with id: " + userId);
        User user = getUserById(userId);
        List<UserLinks> links = userLinksRepository.findByLinksFromOrLinksTo(user, user);
        List<Notification> notifications = new ArrayList<Notification>();
        for (UserLinks ul : links) {
            User notify = ul.getLinksFrom();
            if (ul.getLinksFrom().getId() == user.getId())
                notify = ul.getLinksTo();
            Notification notification = new Notification();
            notification.setContent(content);
            notification.setHyperLinkString(hyperLink.toString());
            notification.setStatus(Status.UNSEEN);
            notification.setUser(notify);
            logger.debug("sending notifications to user with id: " + notify.getId());
            // notify(notification, notify.getUsername());
            notifications.add(notification);

        }

        notificationRepository.saveAll(notifications);
    }

    public void notifyUser(User user, String content, WebMvcLinkBuilder hyperLink) {
        logger.trace("sending notification to user with id: " + user.getId());
        Notification notification = new Notification(content, Status.UNSEEN);
        notification.setUser(user);
        notification.setHyperLinkString(hyperLink.toString());

        notificationRepository.save(notification);
    }

    // The SimpMessagingTemplate is used to send Stomp over WebSocket messages.

    /**
     * Send notification to users subscribed on channel "/user/queue/notify".
     * <p>
     * The message will be sent only to the user with the given username.
     *
     * @param notification The notification message.
     * @param username     The username for the user to send notification.
     */
    public void notify(Notification notification, String username) {
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notify",
                notification);
    }

}
