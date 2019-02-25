package ru.bacca.bikerun.security.jwt;

import io.jsonwebtoken.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import ru.bacca.bikerun.security.detailService.UserPrinciple;

import java.util.Date;

import static ru.bacca.bikerun.security.SecurityConstantas.TOKEN_PREFIX;

public class JwtProvider {

    public static final Logger LOGGER = Logger.getLogger("jwt");

    @Value("${security.jwt.token.secret-key}")
    private String jwtSecret;

    @Value("${security.jwt.token.expire-length}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        token = token.replace("Bearer ", "");
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            authToken = authToken.replace(TOKEN_PREFIX, "");
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            LOGGER.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty -> Message: {}", e);
        }
        for (int i = 0; i < 100000; i++) {
            LOGGER.info("SomeMesage " + i);
        }

        return false;
    }
}
