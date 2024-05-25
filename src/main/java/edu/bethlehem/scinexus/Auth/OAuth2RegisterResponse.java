package edu.bethlehem.scinexus.Auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OAuth2RegisterResponse {

    private String jwtToken;

}
