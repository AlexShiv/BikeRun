package ru.bacca.bikerun.controller;

import org.apache.log4j.Logger;
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

    public static final Logger LOGGER = Logger.getLogger("serviceAppender");

    public UserArchiveController(UserArchiveService service) {
        super(service);
    }

    @GetMapping("/ga")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserArchive> getAllForCheckRole() {
        for (int i = 0; i < 10000; i++) {
            LOGGER.info("Some Test Log INFO!!!");
            LOGGER.debug("Some Test Log DEBUG!!!");
            LOGGER.error("Some Test ERROR!!!");
            LOGGER.warn("Some Test Log WARN!!!");
            LOGGER.fatal("Some Test Log FATAL!!!");
        }
        return getService().getAll();
    }
}
