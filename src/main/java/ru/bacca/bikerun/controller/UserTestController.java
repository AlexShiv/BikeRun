package ru.bacca.bikerun.controller;

import ru.bacca.bikerun.entity.UserTest;
import ru.bacca.bikerun.service.UserTestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usertest")
public class UserTestController extends GenericController<UserTest, UserTestService> {
    public UserTestController(UserTestService service) {
        super(service);
    }
}
