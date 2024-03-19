package edu.bethlehem.scinexus.Auth;


import edu.bethlehem.scinexus.User.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterRequest {


    @NotNull(message = "First Name is mandatory")
    @NotBlank(message = "First Name is mandatory")
    @Size(min = 2, max = 30, message = "First Name must be between 2 and 30 Characters")
    private String firstName;

    @NotNull(message = "Last Name is mandatory")
    @NotBlank(message = "Last Name is mandatory")
    @Size(min = 2, max = 30)
    private String lastName;


    @Column(unique = true)
    @NotNull(message = "Username is mandatory")
    @NotBlank(message = "Username is mandatory")
    @Size(min = 1, max = 50)
    private String username;


    @NotNull(message = "Email is mandatory")
    @NotBlank(message = "Email Name is mandatory")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+[._-]*[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}(?:\\.[a-zA-Z]{2,})?$",
            message = "It must start with alphanumeric characters." +
                    "It may contain dots, hyphens, and underscores." +
                    "It must have exactly one '@' symbol." +
                    "It may contain alphanumeric characters in the domain part."+
                    "It may have a maximum of two dots following the '@' symbol")
    @Column(unique = true)
    @Size(min = 3, max = 320, message = "The Email Address should be Between 3 and 320 Characters")
    private String email;


    // This Regex states :
    // 1. the Password Must Be at least 8 Characters Long
    // 2. It must contain at least one uppercase letter, one lowercase letter, one digit, and one special character
//    @Valid
    @NotNull
    @NotBlank
    @Size(min = 8, max = 30, message = "password must be between 8 and 30 Characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,30}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;

    @Size(min = 0,max = 220, message = "The bio should be in maximum 220 characters")
    private String bio;

    @NotNull(message = "Phone Number is Mandatory")
    @NotBlank(message = "Phone Number is Mandatory")
    //This Regex Status that phone number is valid in any of these formats :
    //(123) 456-7890
    //(123)456-7890
    //123-456-7890
    //123.456.7890
    //1234567890
    @Pattern(regexp = "^\\s*(?:\\+?(\\d{1,3}))?([-. (]*(\\d{3})[-. )]*)?((\\d{3})[-. ]*(\\d{2,4})(?:[-.x ]*(\\d+))?)\\s*$", message = "Phone Number is Not Valid")
    private String phoneNumber;


    @Nullable
    @Size(max = 320)
    private String fieldOfWork;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

}
