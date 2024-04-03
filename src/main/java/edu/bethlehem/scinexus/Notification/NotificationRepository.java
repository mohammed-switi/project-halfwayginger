package edu.bethlehem.scinexus.Notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.bethlehem.scinexus.User.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserAndStatus(User userTo, Status status);

    List<Notification> findByUser(User userTo);
}
