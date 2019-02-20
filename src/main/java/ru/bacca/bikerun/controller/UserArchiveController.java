package ru.bacca.bikerun.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bacca.bikerun.entity.UserArchive;
import ru.bacca.bikerun.service.UserArchiveService;

import java.util.List;

@RestController
@RequestMapping("/userarchive")
public class UserArchiveController extends GenericController<UserArchive, UserArchiveService> {

    public UserArchiveController(UserArchiveService service) {
        super(service);
    }

    @GetMapping("/ga")
    @PreAuthorize("hasRole('client')")
    public List<UserArchive> getAlll() {
        return getService().getAll();
    }
}
