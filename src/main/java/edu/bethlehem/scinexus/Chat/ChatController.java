package edu.bethlehem.scinexus.Chat;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {


    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;



    @MessageMapping("/chat")
    public void processMessage(
            @Payload ChatMessage chatMessage
    ){
        ChatMessage savedMsg=chatMessageService.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),
                "/queue/messages",
                ChatMessage.builder()
                        .id(savedMsg.getId())
                        .senderId(savedMsg.getSenderId())
                        .recipientId(savedMsg.getRecipientId())
                        .content(savedMsg.getContent())
                        .build()

        );
    }
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable("senderId") String senderId,
            @PathVariable("recipientId") String recipientId
    ){
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId,recipientId));
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public GroupChatMessage sendMessage(@Payload GroupChatMessage groupChatMessage){
        return groupChatMessage;
    }


    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")

    public GroupChatMessage addUser(@Payload GroupChatMessage groupChatMessage,
                                    SimpMessageHeaderAccessor headerAccessor){
        //Adds username in websocket session
                headerAccessor.getSessionAttributes().put("username", groupChatMessage.getSender());
                return groupChatMessage;
    }

}
