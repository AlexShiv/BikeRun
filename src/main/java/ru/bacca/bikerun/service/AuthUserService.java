package ru.bacca.bikerun.service;

import org.springframework.stereotype.Service;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.repository.AuthUserRepository;

@Service
public class AuthUserService extends GenericServiceImpl<AuthUser, AuthUserRepository>{

    public AuthUserService(AuthUserRepository abstractJpaRepository) {
        super(abstractJpaRepository);
    }

    public AuthUser findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }
}
