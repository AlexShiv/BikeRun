package ru.bacca.bikerun.repository;

import org.springframework.stereotype.Repository;
import ru.bacca.bikerun.entity.AuthUser;

@Repository
public interface AuthUserRepository extends GenericRepository<AuthUser> {

    AuthUser findByUsername(String username);

    boolean existsByUsername(String username);
}
