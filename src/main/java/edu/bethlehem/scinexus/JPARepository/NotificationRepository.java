package edu.bethlehem.scinexus.JPARepository;

import edu.bethlehem.scinexus.Notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
