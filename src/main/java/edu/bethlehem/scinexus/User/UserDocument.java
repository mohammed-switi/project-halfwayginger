package edu.bethlehem.scinexus.User;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document
public class UserDocument {

    @Id
    private String username;
    private String fullName;
    @Enumerated(EnumType.STRING)
    private Status status;

    private Long userId;
}
