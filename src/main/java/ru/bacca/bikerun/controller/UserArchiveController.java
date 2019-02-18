package ru.bacca.bikerun.controller;

import ru.bacca.bikerun.entity.UserArchive;
import ru.bacca.bikerun.service.UserArchiveService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userarchive")
public class UserArchiveController extends GenericController<UserArchive, UserArchiveService> {

    public UserArchiveController(UserArchiveService service) {
        super(service);
    }

    @Override
    public List<UserArchive> getAll() {

        return super.getAll();
    }
}
