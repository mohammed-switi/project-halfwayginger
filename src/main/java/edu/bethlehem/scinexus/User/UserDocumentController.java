package edu.bethlehem.scinexus.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.UserLinks.UserLinks;
import edu.bethlehem.scinexus.UserLinks.UserLinksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserDocumentController {

    private final UserDocumentService userDocumentService;
    private final UserService userService;
    private final UserLinksService userLinksService;
    private  final JwtService service;
    Logger logger = LoggerFactory.getLogger(UserService.class);


    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public UserDocument addUser(@Payload UserDocument user) {
        logger.info("We are saving the user " + user.toString());
        userDocumentService.saveUser(user);

        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public UserDocument disconnect(@Payload UserDocument user) {
        userService.disconnectUser(user.getUsername());
        return user;
    }

    @GetMapping("/connected-users")
    ResponseEntity<List<UserDocument>> findConnectedUsers() {

        List<UserDocument> userDocuments = userDocumentService.findConnectedUsers();
        System.out.println("all connected Users : " + userDocuments.toString());
        return ResponseEntity.ok(userDocuments);
    }

    @GetMapping("/connected-accepted-users")
    ResponseEntity<List<UserDocument>> findConnected(Authentication authentication) {
        long userId = service.extractId(authentication);
        CollectionModel<EntityModel<UserLinks>> userLinks = userLinksService.getUserLinks(authentication);
        List<UserDocument> userDocuments = userDocumentService.findConnectedUsers();

        List<UserDocument> connectedUsers = userLinks.getContent().stream()
                .map(EntityModel::getContent)
                .filter(link -> link.getAccepted() &&
                        (link.getLinksFrom().getId() == userId || link.getLinksTo().getId() == userId))
                .map(link -> {
                        return userDocuments.stream()
                                .filter(user -> user.getUserId() == link.getLinksTo().getId() || user.getUserId() == link.getLinksFrom().getId())
                                .findFirst()
                                .orElse(null);

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("All connected users: " + connectedUsers.toString());
        return ResponseEntity.ok(connectedUsers);
    }



}
