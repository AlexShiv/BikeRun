package ru.bacca.bikerun.repository;

import ru.bacca.bikerun.entity.AuthUser;

public interface AuthRepository extends GenericRepository<AuthUser> {
    AuthUser findAuthUserByLogin(String login);
}
