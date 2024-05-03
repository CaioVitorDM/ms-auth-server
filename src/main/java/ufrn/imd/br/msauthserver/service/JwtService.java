package ufrn.imd.br.msauthserver.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msauthserver.model.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${secret.key}")
    private String SECRET_KEY;

    public boolean isValid(String token, UserDetails user){
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user){
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", String.valueOf(user.getId()));
        extraClaims.put("patient", user.getPatientId());
        extraClaims.put("doctor", user.getDoctorId());
        extraClaims.put("login", user.getLogin());
        extraClaims.put("imgId", user.getImgId());
        extraClaims.put(
                "role",
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","))
        );
        String token = Jwts
                .builder()
                .claims(extraClaims)
                .subject(user.getLogin())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()  + 60*60*1000))
                .signWith(getSigninKey())
                .compact();

        return token;
    }

    private SecretKey getSigninKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
