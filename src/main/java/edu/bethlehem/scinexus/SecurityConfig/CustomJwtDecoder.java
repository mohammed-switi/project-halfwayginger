package edu.bethlehem.scinexus.SecurityConfig;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomJwtDecoder  implements JwtDecoder {

    private final JwtService jwtService;
    @Override
    public Jwt decode(String token) throws JwtException {
        return  jwtDecoder(token);
    }

    private Jwt jwtDecoder(String token){
        return new Jwt(token, jwtService.extractIssuedAt(token), jwtService.extractExpiresAt(token), jwtService.extractHeaders(token), jwtService.extractAllClaims(token));

    }
}
