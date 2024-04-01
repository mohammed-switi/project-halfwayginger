package edu.bethlehem.scinexus.Notification;

import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "The Notification Content Shouldn't Be Null")
    @NotBlank(message = "The Notification Content Be Empty")
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The Notification Content Shouldn't Be Null")
    private Status status = Status.UNSEEN;

    @ManyToOne
    @JoinColumn(name = "notifications")
    @NotNull(message = "The Notification Meant User Shouldn't Be Null")
    private User user;

    public Notification(String content, Status status) {
        this.content = content;
        this.status = status;
    }

    public Notification() {
    }
}