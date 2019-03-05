package ru.bacca.bikerun.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.bacca.bikerun.authforms.LoginForm;
import ru.bacca.bikerun.authforms.SignUpForm;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.service.AuthUserService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthUserController extends GenericController<AuthUser, AuthUserService> {

    public AuthUserController(AuthUserService service) {
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

    @Override
    @GetMapping("/getall")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<AuthUser> getAll() {
        return getService().getAll();
    }
}
