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


    //USING Java Persistence Query Language (JPQL), (Tested: Works Well!)
    public boolean areUsersLinked(User user1, User user2) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u JOIN u.links l " +
                                "WHERE u.id = :userId1 AND l.id = :userId2", Long.class)
                .setParameter("userId1", user1.getId())
                .setParameter("userId2", user2.getId())
                .getSingleResult();

        return count > 0;
    }

    //Using Native MySQL Query Language (Not Tested Yet!)
//    public boolean areUsersLinked(User user1, User user2) {
//        Query query = entityManager.createNativeQuery(
//                "SELECT COUNT(*) FROM user_links " +
//                        "WHERE user_id = :userId1 AND linked_user_id = :userId2");
//
//        query.setParameter("userId1", user1.getId());
//        query.setParameter("userId2", user2.getId());
//
//        Long count = ((Number) query.getSingleResult()).longValue();
//
//        return count > 0;
//    }

}
