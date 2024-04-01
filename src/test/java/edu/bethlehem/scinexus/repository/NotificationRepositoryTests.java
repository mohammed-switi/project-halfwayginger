package edu.bethlehem.scinexus.repository;


import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Interaction.InteractionType;
import edu.bethlehem.scinexus.JPARepository.ArticleRepository;
import edu.bethlehem.scinexus.JPARepository.NotificationRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.Notification.Status;
import edu.bethlehem.scinexus.User.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class NotificationRepositoryTests {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Article article;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .firstName("Mohammed")
                .lastName("Sowaity")
                .username("mohammed")
                .email("mohammed@example.com")
                .password("MustEncrypt")
                .role(Role.ACADEMIC)
                .position(Position.ASSISTANT_PROFESSOR)
                .education("Bethlehem University")
                .phoneNumber("0593333333")
                .bio("NOTHING")
                .fieldOfWork("Software")
                .build();
        userRepository.save(user);
        article = Article.builder()
                .content("Content 1")
                .visibility(Visibility.PUBLIC)
                .title("Title 1")
                .subject("Subject 1")
                .publisher(user)
                .build();



        articleRepository.save(article);
    }

    @Test
    public void NotificationRepository_SaveAll_ReturnNotification() {
        Notification notification = Notification.builder()
                .content("Content")  // Assuming opinion is null for this test
                .status(Status.RECEIVED)  // Assuming journal is null for this test
                .user(user)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        Assertions.assertThat(savedNotification).isNotNull();
        Assertions.assertThat(savedNotification.getId()).isGreaterThan(0);
    }

    @Test
    public void NotificationRepository_GetAll_ReturnMoreThanOneNotification() {
        Notification notification = Notification.builder()
                .content("Content")  // Assuming opinion is null for this test
                .status(Status.RECEIVED)  // Assuming journal is null for this test
                .user(user)
                .build();

        notificationRepository.save(notification);

        List<Notification> getNotifications = notificationRepository.findAll();

        Assertions.assertThat(getNotifications).isNotNull();
        Assertions.assertThat(getNotifications.size()).isGreaterThan(0);
    }

    @Test
    public void NotificationRepository_FindByID_GetById() {
        Notification notification = Notification.builder()
                .content("Content")  // Assuming opinion is null for this test
                .status(Status.RECEIVED)  // Assuming journal is null for this test
                .user(user)
                .build();

        notificationRepository.save(notification);

        Notification findByIdNotification = notificationRepository.findById(notification.getId()).get();

        Assertions.assertThat(findByIdNotification).isNotNull();
    }

    @Test
    public void NotificationRepository_FindByStatus_ReturnNotificationNotNull() {
        Notification notification = Notification.builder()
                .content("Content")  // Assuming opinion is null for this test
                .status(Status.RECEIVED)  // Assuming journal is null for this test
                .user(user)
                .build();

        notificationRepository.save(notification);

        Notification foundNotification = notificationRepository.findByStatus(Status.RECEIVED).get();

        Assertions.assertThat(foundNotification).isNotNull();
        Assertions.assertThat(foundNotification.getStatus()).isEqualTo(Status.RECEIVED);
    }

    @Test
    public void NotificationRepository_UpdatedUser_ReturnNotificationNotNull() {
        Notification notification = Notification.builder()
                .content("Content")  // Assuming opinion is null for this test
                .status(Status.RECEIVED)  // Assuming journal is null for this test
                .user(user)
                .build();


        Notification savedNotification =  notificationRepository.save(notification);
            savedNotification.setStatus(Status.SEEN);
            savedNotification.setContent("UPDATED");

        Notification updatedNotification = notificationRepository.save(savedNotification);

        Assertions.assertThat(updatedNotification.getStatus()).isEqualTo(Status.SEEN);
        Assertions.assertThat(updatedNotification.getContent()).isEqualTo("UPDATED");
    }

    @Test
    public void NotificationRepository_DeleteUser_ReturnNotificationNotNull() {
        Notification notification = Notification.builder()
                .content("Content")  // Assuming opinion is null for this test
                .status(Status.RECEIVED)  // Assuming journal is null for this test
                .user(user)
                .build();

        notificationRepository.save(notification);

        notificationRepository.deleteById(notification.getId());

        Optional<Notification> deletedNotification = notificationRepository.findById(notification.getId());

        Assertions.assertThat(deletedNotification).isEmpty();
    }
}
