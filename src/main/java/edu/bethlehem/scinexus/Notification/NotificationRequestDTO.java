package edu.bethlehem.scinexus.Notification;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder.Default;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationRequestDTO {

    // @NotNull(message = "The Notification Content Shouldn't Be Null")
    @NotBlank(message = "The Notification Content Be Empty")
    private String content;

    @Enumerated(EnumType.STRING)
    // @NotNull(message = "The Notification Content Shouldn't Be Null")
    @NotBlank(message = "The Notification Status Should Be Specified")
    private Status status = Status.UNSEEN;
}
