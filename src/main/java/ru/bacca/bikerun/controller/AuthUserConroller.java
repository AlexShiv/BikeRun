package ru.bacca.bikerun.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bacca.bikerun.authforms.LoginForm;
import ru.bacca.bikerun.authforms.SignUpForm;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.service.AuthUserService;

@RestController
@RequestMapping("/auth")
public class AuthUserConroller extends GenericController<AuthUser, AuthUserService> {

    public AuthUserConroller(AuthUserService service) {
        super(service);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpForm signUpForm) {
        return getService().signUp(signUpForm);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody LoginForm loginForm) {
        return getService().signIn(loginForm);
    }
}
