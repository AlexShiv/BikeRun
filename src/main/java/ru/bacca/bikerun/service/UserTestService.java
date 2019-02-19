package ru.bacca.bikerun.service;

import ru.bacca.bikerun.entity.UserTest;
import ru.bacca.bikerun.repository.UserTestRepository;
import org.springframework.stereotype.Service;

@Service
public class UserTestService extends GenericServiceImpl<UserTest, UserTestRepository> {

    public UserTestService(UserTestRepository abstractJpaRepository) {
        super(abstractJpaRepository);
    }
}
