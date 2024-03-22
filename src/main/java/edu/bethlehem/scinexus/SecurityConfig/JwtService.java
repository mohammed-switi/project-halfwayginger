package edu.bethlehem.scinexus.SecurityConfig;

import edu.bethlehem.scinexus.SecurityConfig.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.json.JSONObject;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "491ef82099212d650f7da939a3473c3ae12bb6ca0004a0b635245b9c04e4a94f";

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public Long extractId(String jwtToken) {
        Claims claim = extractClaim(jwtToken.substring(7), claims -> {
            return claims;
        });
        JSONObject json = new JSONObject(claim);
        return Long.parseLong(json.get("id").toString());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetailsImpl userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", userDetails.getId().toString());
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetailsImpl userDetails) {

        return Jwts
                .builder()
                .claims()
                .add(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (10000 * 60 * 24)))
                .and()
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();

    }

    public boolean isTokenValid(String token, UserDetailsImpl userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) throws SignatureException, MalformedJwtException {

        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }

}