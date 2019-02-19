package ru.bacca.bikerun.service;

import org.springframework.stereotype.Service;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.repository.AuthRepository;

@Service
public class AuthService extends GenericServiceImpl<AuthUser, AuthRepository>{

    public AuthService(AuthRepository abstractJpaRepository) {
        super(abstractJpaRepository);
    }
}
