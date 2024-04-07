package edu.bethlehem.scinexus.Auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotEmpty(message = "Email shoulldn't be empty")
    private String email;

    @NotEmpty(message = "Password Should'nt Be empty")
    private String password;

}
