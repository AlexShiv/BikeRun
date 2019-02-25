package ru.bacca.bikerun.service;

import org.springframework.stereotype.Service;
import ru.bacca.bikerun.entity.User;
import ru.bacca.bikerun.repository.UserRepository;

@Service
public class UserService extends GenericServiceImpl<User, UserRepository> {

    public UserService(UserRepository abstractJpaRepository) {
        super(abstractJpaRepository);
    }
}
