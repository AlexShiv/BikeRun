package ru.bacca.bikerun.repository;

import ru.bacca.bikerun.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User> {
}
