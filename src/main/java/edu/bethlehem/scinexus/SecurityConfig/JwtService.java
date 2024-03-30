package edu.bethlehem.scinexus.SecurityConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.json.JSONObject;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService  {

    private static final String SECRET_KEY = "491ef82099212d650f7da939a3473c3ae12bb6ca0004a0b635245b9c04e4a94f";


    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }
    public Long extractId(Authentication authentication){
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
    public Instant extractIssuedAt(String jwtToken){return extractClaim(jwtToken,Claims::getIssuedAt).toInstant();}

    public Instant extractExpiresAt(String token){return extractClaim(token,Claims::getExpiration).toInstant();}

    public HashMap<String, Object> extractHeaders(String token){
        Claims claims = extractAllClaims(token);
        HashMap<String,Object> headers=new HashMap<>();
        headers.put("typ",claims.get("typ"));
        headers.put("alg",claims.get("alg"));
        System.out.println(headers.toString() +" SLUT THIS IS Headers");
        return headers;
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
                .expiration(new Date(System.currentTimeMillis() + (1000* 60 * 60 * 24))) // The Jwt Expiry Date Will be 24 Hours After Creation
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

    Claims extractAllClaims(String token) throws SignatureException, MalformedJwtException {

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