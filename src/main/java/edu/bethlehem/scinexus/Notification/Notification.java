package edu.bethlehem.scinexus.Notification;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Notification {
    private @Id @GeneratedValue Long id;
    private Long notificationId;
    private String content;
    private Status status;

    public Notification(Long notificationId, String content, Status status) {
        this.notificationId = notificationId;
        this.content = content;
        this.status = status;
    }

    public Notification() {
    }
}