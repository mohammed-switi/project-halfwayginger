package edu.bethlehem.scinexus.Auth;

import edu.bethlehem.scinexus.User.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthenticationResponse {

    private String jwtToken;
    private Role role;


    public  AuthenticationResponse(String jwtToken){
        this.jwtToken=jwtToken;
    }

}
