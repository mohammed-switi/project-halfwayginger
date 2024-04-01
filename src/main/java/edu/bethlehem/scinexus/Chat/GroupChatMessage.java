package edu.bethlehem.scinexus.Chat;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class GroupChatMessage {


    private String content;
    private String sender;

    @Enumerated(EnumType.STRING)
    private MessageType type;
}
