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
    @NotBlank(message = "The Notification Content cannot Be Empty")
    private String content;

    @NotBlank(message = "The hyperLinkString cannot Be Empty")
    private String hyperLinkString;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The Notification Content Shouldn't Be Null")
    @NotBlank(message = "The Notification Status Should Be Specified")
    private Status status = Status.UNSEEN;

    @ManyToOne
    @JoinColumn(name = "notifications")
    @NotNull(message = "The Notification Meant User Shouldn't Be Null")
    @NotBlank(message = "The Notification Meant User cannot Empty")
    private User user;

    public Notification(String content, Status status) {
        this.content = content;
        this.status = status;
    }

    public Notification() {
    }
}