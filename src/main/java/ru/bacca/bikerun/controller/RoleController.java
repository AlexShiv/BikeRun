package ru.bacca.bikerun.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    public String userAccess() {
        return ">>> User Contents!";
    }

    public String projectManagementAccess(){
        return ">>> Board Management Project";
    }
}
