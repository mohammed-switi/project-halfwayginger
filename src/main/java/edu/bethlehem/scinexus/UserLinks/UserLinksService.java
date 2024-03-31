package edu.bethlehem.scinexus.UserLinks;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserLinksService {
    private final UserLinksRepository ulr;
    private final UserRepository userRepository;
    private final UserLinksModelAssembler ulAssembler;

    public Boolean areTheyLinked(User link1, User link2) {

        return ulr.existsByLinksToAndLinksFrom(link1, link2) || ulr.existsByLinksToAndLinksFrom(link2, link1);
    }

    private User getUser(Authentication auth) {
        Long userId = ((User) auth.getPrincipal()).getId();
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    }

    public EntityModel<UserLinks> linkUser(Authentication auth, Long linkToId) {
        User linkFrom = getUser(auth);
        User linkTo = getUser(linkToId);
        if (areTheyLinked(linkFrom, linkTo))
            throw new UserNotFoundException("the users are already linked", HttpStatus.CONFLICT);
        if (linkFrom.getId() == linkToId)
            throw new UserNotFoundException("users cannot link to themselves", HttpStatus.CONFLICT);
        UserLinks ul = new UserLinks(linkFrom, linkTo);
        ulr.save(ul);
        return ulAssembler.toModel(ulr.save(ul));

    }

    public ResponseEntity<EntityModel<UserLinks>> acceptLink(Authentication auth, Long linkFromId, Boolean answer) {
        User linkTo = getUser(auth);
        User linkFrom = getUser(linkFromId);
        if (!areTheyLinked(linkFrom, linkTo))
            throw new UserNotFoundException("there is no link to accept", HttpStatus.CONFLICT);
        UserLinks ul = ulr.findByLinksFromAndLinksTo(linkFrom, linkTo);
        if (ul == null)
            throw new UserNotFoundException("You are not the one to accept the linkage", HttpStatus.UNAUTHORIZED);
        if (ul.getAccepted())
            throw new UserNotFoundException("The Linkage is already accepted");
        if (answer == false) {
            ulr.delete(ul);
            return ResponseEntity.noContent().build();
        }
        ul.setAccepted(true);
        return ResponseEntity.ok(ulAssembler.toModel(ulr.save(ul)));
    }

    public ResponseEntity<EntityModel<UserLinks>> unLink(Authentication auth, Long LinkedUserId) {
        User linked1 = getUser(auth);
        User linked2 = getUser(LinkedUserId);
        if (!areTheyLinked(linked1, linked2))
            throw new UserNotFoundException("there is no link", HttpStatus.CONFLICT);
        UserLinks ul = ulr.findByLinksFromAndLinksTo(linked1, linked2);
        if (ul == null)
            ul = ulr.findByLinksFromAndLinksTo(linked2, linked1);

        ulr.delete(ul);
        return ResponseEntity.noContent().build();
    }

}
