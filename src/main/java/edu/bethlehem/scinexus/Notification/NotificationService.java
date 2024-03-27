package edu.bethlehem.scinexus.Notification;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import edu.bethlehem.scinexus.Organization.OrganizationNotFoundException;
import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.Notification.NotificationNotFoundException;
import edu.bethlehem.scinexus.Notification.NotificationRepository;
import edu.bethlehem.scinexus.Notification.NotificationRequestDTO;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationModelAssembler assembler;

    public Notification convertNotificationDtoToNotificationEntity(NotificationRequestDTO NotificationRequestDTO) {

        return Notification.builder()
                .content(NotificationRequestDTO.getContent())
                .status(NotificationRequestDTO.getStatus())
                .build();
    }

    public User getUserById(long id) {

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

    public EntityModel<Notification> createNotification(NotificationRequestDTO newNotificationRequestDTO,
            Long userId) {
        Notification notification = convertNotificationDtoToNotificationEntity(newNotificationRequestDTO);
        notification.setUser(getUserById(userId));
        return assembler.toModel(saveNotification(notification));
    }

    public EntityModel<Notification> updateNotification(Long notificationId,
            NotificationRequestDTO newNotificationRequestDTO) {

        return notificationRepository.findById(
                notificationId)
                .map(notification -> {
                    notification.setContent(newNotificationRequestDTO.getContent());
                    notification.setStatus(newNotificationRequestDTO.getStatus());
                    return assembler.toModel(notificationRepository.save(notification));
                })
                .orElseThrow(() -> new NotificationNotFoundException(
                        notificationId, HttpStatus.UNPROCESSABLE_ENTITY));
    }

    public EntityModel<Notification> updateNotificationPartially(Long notificationId,
            NotificationRequestDTO newNotificationRequestDTO) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(
                        () -> new NotificationNotFoundException(notificationId, HttpStatus.UNPROCESSABLE_ENTITY));

        try {
            for (Method method : NotificationRequestDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(newNotificationRequestDTO);
                    if (value != null) {
                        String propertyName = method.getName().substring(3); // remove "get"
                        Method setter = Notification.class.getMethod("set" + propertyName, method.getReturnType());
                        setter.invoke(notification, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assembler.toModel(notificationRepository.save(notification));
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(
                        () -> new NotificationNotFoundException(notificationId, HttpStatus.UNPROCESSABLE_ENTITY));
        notificationRepository.delete(notification);
    }

}
