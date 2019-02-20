package ru.bacca.bikerun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bacca.bikerun.authforms.SingUpForm;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.entity.Role;
import ru.bacca.bikerun.service.AuthUserService;
import ru.bacca.bikerun.service.RoleService;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthUserConroller extends GenericController<AuthUser, AuthUserService> {

    @Autowired
    private final RoleService roleService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthUserConroller(AuthUserService service, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(service);
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody AuthUser authUser) {
        authUser.setPassword(bCryptPasswordEncoder.encode(authUser.getPassword()));

        getService().save(authUser);
    }

    @PostMapping("/signup1")
    public ResponseEntity<String> signUp(@RequestBody SingUpForm singUpForm) {
        if (getService().existsByUsername(singUpForm.getUsername())) {
            return new ResponseEntity<>("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        // Creating auth user from form
        AuthUser authUser = new AuthUser();
        authUser.setUsername(singUpForm.getUsername());
        authUser.setPassword(bCryptPasswordEncoder.encode(singUpForm.getPassword()));

        Set<String> strRole = singUpForm.getRole();
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

        getService().save(authUser);

        return ResponseEntity.ok("User registrered successfully!");
    }
}
