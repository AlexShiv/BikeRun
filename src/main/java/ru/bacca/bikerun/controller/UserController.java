package ru.bacca.bikerun.controller;

import ru.bacca.bikerun.entity.User;
import ru.bacca.bikerun.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends GenericController<User, UserService> {

    public UserController(UserService service) {
        super(service);
    }
}
