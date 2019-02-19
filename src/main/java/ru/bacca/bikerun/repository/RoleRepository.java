package ru.bacca.bikerun.repository;

import ru.bacca.bikerun.entity.role.Role;
import ru.bacca.bikerun.entity.role.RoleName;

import java.util.Optional;

public interface RoleRepository extends GenericRepository<Role> {
    Optional<Role> findByName(RoleName roleName);
}
