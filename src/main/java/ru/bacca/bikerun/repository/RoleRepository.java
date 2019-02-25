package ru.bacca.bikerun.repository;

import org.springframework.stereotype.Repository;
import ru.bacca.bikerun.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends GenericRepository<Role> {

    Optional<Role> findByName(String name);
}
