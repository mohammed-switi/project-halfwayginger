package edu.bethlehem.scinexus.UserLinks;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.hibernate.mapping.Collection;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import edu.bethlehem.scinexus.Journal.JournalController;
import edu.bethlehem.scinexus.Notification.NotificationService;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserController;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.JPARepository.UserLinksRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserLinksService {
    private final UserLinksRepository ulr;
    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final UserLinksModelAssembler ulAssembler;
    private final NotificationService notificationService;

    public Boolean areTheyLinked(User link1, User link2) {

        return ulr.existsByLinksToAndLinksFrom(link1, link2) || ulr.existsByLinksToAndLinksFrom(link2, link1);
    }

    public Boolean areTheyLinked(Long link1, Long link2) {
        User user1 = userRepository.findById(link1)
                .orElseThrow(() -> new UserNotFoundException("User Is Not Found", HttpStatus.NOT_FOUND));
        User user2 = userRepository.findById(link2)
                .orElseThrow(() -> new UserNotFoundException("User Is Not Found", HttpStatus.NOT_FOUND));

        return ulr.existsByLinksToAndLinksFrom(user1, user2) || ulr.existsByLinksToAndLinksFrom(user1, user2);
    }

    public EntityModel<UserLinks> linkUser(Authentication auth, Long linkToId) {
        User linkFrom = jwtService.getUser(auth);
        User linkTo = jwtService.getUser(linkToId);

        if (areTheyLinked(linkFrom, linkTo))
            throw new UserNotFoundException("the users are already linked", HttpStatus.CONFLICT);
        if (linkFrom.getId() == linkToId)
            throw new UserNotFoundException("users cannot link to themselves", HttpStatus.CONFLICT);
        UserLinks ul = new UserLinks(linkFrom, linkTo);
        ulr.save(ul);
        notificationService.notifyUser(linkTo, linkFrom.getFirstName() + " has requested to Link with you",
                linkTo(methodOn(
                        UserController.class).respondToLinkage(null, linkToId, null)));

        return ulAssembler.toModel(ulr.save(ul));

    }

    public EntityModel<UserLinks> acceptLink(Authentication auth, Long linkFromId, Boolean answer) {
        User linkTo = jwtService.getUser(auth);
        User linkFrom = jwtService.getUser(linkFromId);
        if (!areTheyLinked(linkFrom, linkTo))
            throw new UserNotFoundException("there is no link to accept", HttpStatus.CONFLICT);
        UserLinks ul = ulr.findByLinksFromAndLinksTo(linkFrom, linkTo);
        if (ul == null)
            throw new UserNotFoundException("You are not the one to accept the linkage", HttpStatus.UNAUTHORIZED);
        if (ul.getAccepted())
            throw new UserNotFoundException("The Linkage is already accepted");
        if (!answer) {
            ulr.delete(ul);
            return ulAssembler.toModel(ul);
        }
        ul.setAccepted(true);
        notificationService.notifyUser(
                linkFrom, linkTo.getFirstName() + " has accepted to Link with you",
                linkTo(methodOn(
                        UserController.class).one(linkFromId)));
        return ulAssembler.toModel(ulr.save(ul));
    }

    public void unLink(Authentication auth, Long LinkedUserId) {
        User linked1 = jwtService.getUser(auth);
        User linked2 = jwtService.getUser(LinkedUserId);
        if (!areTheyLinked(linked1, linked2))
            throw new UserNotFoundException("there is no link", HttpStatus.CONFLICT);
        UserLinks ul = ulr.findByLinksFromAndLinksTo(linked1, linked2);
        if (ul == null)
            ul = ulr.findByLinksFromAndLinksTo(linked2, linked1);

        ulr.delete(ul);

    }

    public CollectionModel<EntityModel<UserLinks>> getUserLinks(Authentication auth) {
        User user = jwtService.getUser(auth);
        List<UserLinks> links = ulr.findByLinksFromOrLinksTo(user, user);
        return ulAssembler.toCollectionModel(links);
    }

}
