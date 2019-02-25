package ru.bacca.bikerun.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.bacca.bikerun.security.detailService.UserDetailServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.bacca.bikerun.security.SecurityConstantas.HEADER_STRING;
import static ru.bacca.bikerun.security.SecurityConstantas.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtProvider tokenProvider;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // получаем токен из хедера
        String tokenFromHeader = request.getHeader(HEADER_STRING);

        // проверка токена на корректный префикс или на null
        if (tokenFromHeader == null || !tokenFromHeader.startsWith(TOKEN_PREFIX) || !tokenProvider.validateJwtToken(tokenFromHeader)) {
            chain.doFilter(request, response);
            return;
        }

        String username = tokenProvider.getUserNameFromToken(tokenFromHeader);

        // получаем пользователя по его логину
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
