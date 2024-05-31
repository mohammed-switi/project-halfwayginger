package edu.bethlehem.scinexus.User;

import edu.bethlehem.scinexus.MongoRepository.UserMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDocumentService {

    @Autowired
    private UserMongoRepository userDocumentRepository;

    public void saveUser(UserDocument userDocument) {
        userDocument.setStatus(Status.ONLINE);
        userDocumentRepository.save(userDocument);
    }

    public void updateUserStatus(String nickName, Status status) {
        UserDocument userDocument = userDocumentRepository.findById(nickName).orElse(null);
        if (userDocument != null) {
            userDocument.setStatus(status);
            userDocumentRepository.save(userDocument);
        }
    }


    public List<UserDocument> findConnectedUsers() {
        return userDocumentRepository.findAllByStatus(Status.ONLINE);
    }
}
