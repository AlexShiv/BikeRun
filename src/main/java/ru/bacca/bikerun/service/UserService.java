package ru.bacca.bikerun.service;

import ru.bacca.bikerun.entity.User;
import ru.bacca.bikerun.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericServiceImpl<User, UserRepository> {

    public UserService(UserRepository abstractJpaRepository) {
        super(abstractJpaRepository);
    }
}
