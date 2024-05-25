package edu.bethlehem.scinexus.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class UserRequestPatchDTO {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;

    private String bio;
    private Position position;
    private String phoneNumber;

    private String fieldOfWork;

    private Long profilePicture;

    private Long coverPicture;

    private String education;

    private String contactEmail;
    private Set<String> skills;
    private String contactPhoneNumber;
    private Set<String> languages;

}