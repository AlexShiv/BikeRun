package ru.bacca.bikerun.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.service.AuthUserService;

@RestController
@RequestMapping("/auth")
public class AuthUserConroller extends GenericController<AuthUser, AuthUserService> {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthUserConroller(AuthUserService service,
                             BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(service);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody AuthUser authUser) {
        authUser.setPassword(bCryptPasswordEncoder.encode(authUser.getPassword()));

        getService().save(authUser);
    }
}
