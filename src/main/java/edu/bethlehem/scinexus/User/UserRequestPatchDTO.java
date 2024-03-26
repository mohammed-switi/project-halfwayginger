package edu.bethlehem.scinexus.User;

import com.fasterxml.jackson.databind.util.JSONPObject;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

}