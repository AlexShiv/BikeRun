package ru.bacca.bikerun.repository;

import ru.bacca.bikerun.entity.AuthUser;

public interface AuthUserRepository extends GenericRepository<AuthUser> {

    AuthUser findByUsername(String username);

    boolean existsByUsername(String username);
}
