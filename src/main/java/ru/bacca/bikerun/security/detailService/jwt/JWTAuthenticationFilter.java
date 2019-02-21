package ru.bacca.bikerun.security.detailService.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.bacca.bikerun.entity.AuthUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static ru.bacca.bikerun.security.SecurityConstantas.HEADER_STRING;
import static ru.bacca.bikerun.security.SecurityConstantas.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private JwtProvider tokenProvider;

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            AuthUser user = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthUser.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(), user.getPassword(), new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        /*String token = JWT.create()
                .withSubject( ((UserPrinciple) authResult.getPrincipal()).getUsername()) // ?
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // время жизни токена
                .sign(HMAC512(SECRET.getBytes()));*/ // алгоритм шифрования

        String token = tokenProvider.generateJwtToken(authResult);

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
