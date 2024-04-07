package edu.bethlehem.scinexus.JPARepository;

import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.Notification.Status;
import edu.bethlehem.scinexus.User.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByStatus(Status status);

    List<Notification> findByUserAndStatus(User user, Status status);

    List<Notification> findByUser(User user);
}
