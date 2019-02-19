package ru.bacca.bikerun.service;

import org.springframework.stereotype.Service;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.repository.AuthUserRepository;

import java.util.Optional;

@Service
public class AuthUserService extends GenericServiceImpl<AuthUser, AuthUserRepository>{

    public AuthUserService(AuthUserRepository abstractJpaRepository) {
        super(abstractJpaRepository);
    }

    public Optional<AuthUser> findAuthUserByLogin(String login) {
        return repository.findAuthUserByLogin(login);
    }

    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }
}
