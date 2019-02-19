package ru.bacca.bikerun.repository;

import org.springframework.stereotype.Repository;
import ru.bacca.bikerun.entity.AuthUser;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends GenericRepository<AuthUser> {
    Optional<AuthUser> findAuthUserByLogin(String login);
    boolean existsByLogin(String login);
}
