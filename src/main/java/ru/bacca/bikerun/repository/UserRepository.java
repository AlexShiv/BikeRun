package ru.bacca.bikerun.repository;

import org.springframework.stereotype.Repository;
import ru.bacca.bikerun.entity.User;

@Repository
public interface UserRepository extends GenericRepository<User> {
}
