package edu.bethlehem.scinexus.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean areUsersLinked(User user1, User user2) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u JOIN u.links l " +
                                "WHERE u.id = :userId1 AND l.id = :userId2", Long.class)
                .setParameter("userId1", user1.getId())
                .setParameter("userId2", user2.getId())
                .getSingleResult();

        return count > 0;
    }

}
