package cl.admin.usercrud.services.impl;

import cl.admin.usercrud.dtos.TokenResponseDTO;
import cl.admin.usercrud.models.UserEntity;
import cl.admin.usercrud.services.IJWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JWTServicesImpl implements IJWTService {


    @Value("${env.secret.key}")
    private String SECRET_KEY;

    public ResponseEntity<TokenResponseDTO> generateToken(String uuid){
        return ResponseEntity.ok(TokenResponseDTO.builder().token(Jwts.builder().subject(uuid)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignKey())
                .compact()).build());
    }

    public String extracUUID(String token){
        return extractClaim(token, Claims::getSubject);
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean isTokenValid(String token, UserEntity userResponseDTO){
        final UUID UUID = java.util.UUID.fromString(extracUUID(token));
        System.out.println(userResponseDTO.getUserId() == UUID);

        System.out.println( isTokenExpired());

        return userResponseDTO.getUserId() == UUID && isTokenExpired();
    }

    private boolean isTokenExpired() {
        return true;
    }

    private SecretKey getSignKey() {
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

}
