package edu.bethlehem.scinexus.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserRequestPatchDTO {

    @NotBlank(message = "The First Name Shouldn't Be Empty")
    private String firstName;

    @NotBlank(message = "The Last Name Shouldn't Be Empty")
    private String lastName;

    @NotBlank(message = "The Username Shouldn't Be Empty")
    private String username;

    @NotBlank(message = "The Email Shouldn't Be Empty")
    private String email;

    @NotBlank(message = "The Password Shouldn't Be Empty")
    private String password;

    @NotBlank(message = "The Bio Shouldn't Be Empty")
    private String bio;

    @NotBlank(message = "The phone Number Shouldn't Be Empty")
    private String phoneNumber;

    @NotBlank(message = "The Field of Work Shouldn't Be Empty")
    private String fieldOfWork;

    private Long profilePicture;

    private Long coverPicture;

}