package ru.bacca.bikerun.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bacca.bikerun.authforms.LoginForm;
import ru.bacca.bikerun.authforms.SignUpForm;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.entity.Role;
import ru.bacca.bikerun.repository.AuthUserRepository;
import ru.bacca.bikerun.security.jwt.JwtProvider;

import java.util.HashSet;
import java.util.Set;

import static ru.bacca.bikerun.security.SecurityConstantas.TOKEN_PREFIX;

@Service
public class AuthUserService extends GenericServiceImpl<AuthUser, AuthUserRepository> {

    public static final Logger LOGGER = Logger.getLogger("service");

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final RoleService roleService;

    public AuthUserService(AuthUserRepository abstractJpaRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService) {
        super(abstractJpaRepository);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
    }

    public AuthUser findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    public ResponseEntity<String> signUp(SignUpForm signUpForm) {
        if (existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<>("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        // Creating auth user from form
        AuthUser authUser = new AuthUser();
        authUser.setUsername(signUpForm.getUsername());
        authUser.setPassword(bCryptPasswordEncoder.encode(signUpForm.getPassword()));

        Set<String> strRole = signUpForm.getRole();
        Set<Role> roles = new HashSet<>();

        strRole.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleService.findByName("ROLE_ADMIN")
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role ADMIN not find."));
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole = roleService.findByName("ROLE_PM")
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role PM not found"));
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = roleService.findByName("ROLE_USER")
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role USER not found"));
                    roles.add(userRole);

            }
        });

        authUser.setRoles(roles);

        save(authUser);

        LOGGER.info("User registrered successfully!");
        return ResponseEntity.ok("User registrered successfully!");
    }

    public ResponseEntity<String> signIn(LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getUsername(),
                        loginForm.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        LOGGER.info("User authorized successfully!");
        return ResponseEntity.ok(TOKEN_PREFIX + jwt);
    }
}
