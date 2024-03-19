package edu.bethlehem.scinexus.Notification;

import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Notification {
    private @Id @GeneratedValue Long id;
    private String content;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "notifications")
    private User user;

    public Notification(String content, Status status) {
        this.content = content;
        this.status = status;
    }

    public Notification() {
    }
}