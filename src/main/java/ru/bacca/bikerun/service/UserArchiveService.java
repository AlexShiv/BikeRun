package ru.bacca.bikerun.service;

import org.springframework.stereotype.Service;
import ru.bacca.bikerun.entity.UserArchive;
import ru.bacca.bikerun.repository.UserArchiveRepository;

@Service
public class UserArchiveService extends GenericServiceImpl<UserArchive, UserArchiveRepository> {

    public UserArchiveService(UserArchiveRepository abstractJpaRepository) {
        super(abstractJpaRepository);
    }
}
