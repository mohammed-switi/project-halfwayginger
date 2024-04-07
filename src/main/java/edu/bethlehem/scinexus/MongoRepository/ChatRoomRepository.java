package edu.bethlehem.scinexus.MongoRepository;

import edu.bethlehem.scinexus.ChatRoom.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomRepository  extends MongoRepository<ChatRoom,String> {

    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId,String recipientId);
}
