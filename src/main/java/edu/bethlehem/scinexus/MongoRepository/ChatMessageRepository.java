package edu.bethlehem.scinexus.MongoRepository;

import edu.bethlehem.scinexus.Chat.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository  extends MongoRepository<ChatMessage,String> {

    List<ChatMessage> findByChatId(String s);
}
