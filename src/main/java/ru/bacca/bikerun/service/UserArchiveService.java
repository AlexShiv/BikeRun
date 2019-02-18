package ru.bacca.bikerun.service;

import ru.bacca.bikerun.entity.UserArchive;
import ru.bacca.bikerun.repository.UserArchiveRepository;
import org.springframework.stereotype.Service;

@Service
public class UserArchiveService extends GenericServiceImpl<UserArchive, UserArchiveRepository> {

    public UserArchiveService(UserArchiveRepository abstractJpaRepository) {
        super(abstractJpaRepository);
    }
}
