package edu.bethlehem.scinexus.MongoRepository;

import edu.bethlehem.scinexus.User.UserDocument;
import edu.bethlehem.scinexus.User.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMongoRepository extends MongoRepository<UserDocument,String> {



    List<UserDocument> findAllByStatus(Status status);

}
