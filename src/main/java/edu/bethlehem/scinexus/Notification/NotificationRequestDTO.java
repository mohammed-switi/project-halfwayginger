package edu.bethlehem.scinexus.Notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationRequestDTO {

    // @NotNull(message = "The Notification Content Shouldn't Be Null")
    @NotBlank(message = "The Notification Content Be Empty")
    private String content;

}
