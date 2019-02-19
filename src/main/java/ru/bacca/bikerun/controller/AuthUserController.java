package ru.bacca.bikerun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.service.AuthUserService;
import ru.bacca.bikerun.service.RoleService;

@RestController
@RequestMapping("/auth")
public class AuthUserController extends GenericController<AuthUser, AuthUserService>{

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder encoder;

    JwtProvider jwtProvider;

    public AuthUserController(AuthUserService service) {
        super(service);
    }
}
