package edu.bethlehem.scinexus.Notification;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Entity
@Builder
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @NotNull(message = "The Notification Content Shouldn't Be Null")
    @NotBlank(message = "The Notification Content cannot Be Empty")
    private String content;

    // @NotBlank(message = "The hyperLinkString cannot Be Empty")
    private String hyperLinkString;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The Notification Content Shouldn't Be Null")
    @Default
    private Status status = Status.UNSEEN;

    @ManyToOne
    @JoinColumn(name = "userId")
    @NotNull(message = "The Notification Meant User Shouldn't Be Null")
    private User user;

    public Notification(String content, Status status) {
        this.content = content;
        this.status = status;
    }

    public Notification() {
    }
}