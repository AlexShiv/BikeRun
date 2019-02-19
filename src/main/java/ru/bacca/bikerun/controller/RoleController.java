package ru.bacca.bikerun.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bacca.bikerun.entity.role.Role;
import ru.bacca.bikerun.service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController extends GenericController<Role, RoleService> {

    public RoleController(RoleService service) {
        super(service);
    }
}
