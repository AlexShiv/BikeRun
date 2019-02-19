package ru.bacca.bikerun.service;

import org.springframework.stereotype.Service;
import ru.bacca.bikerun.entity.role.Role;
import ru.bacca.bikerun.entity.role.RoleName;
import ru.bacca.bikerun.repository.RoleRepository;

import java.util.Optional;

@Service
public class RoleService extends GenericServiceImpl<Role, RoleRepository> {

    public RoleService(RoleRepository abstractJpaRepository) {
        super(abstractJpaRepository);
    }

    public Optional<Role> findByName(RoleName roleName) {
        return repository.findByName(roleName);
    }

}
