package edu.bethlehem.scinexus.User;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserDocumentController {

    private final UserService service;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public UserDocument addUser(@Payload UserDocument user) {
        System.out.println("Now we are saving th user from controller : " + user.toString());
        service.saveUser(user);

        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public UserDocument disconnect(@Payload UserDocument user) {
        service.disconnect(user);
        return user;
    }

    @GetMapping("/connected-users")
    ResponseEntity<List<UserDocument>> findConnectedUsers() {

        List<UserDocument> userDocuments = service.findConnectedUser();
        System.out.println("all connected Users : " + userDocuments.toString());
        return ResponseEntity.ok(userDocuments);
    }

}
