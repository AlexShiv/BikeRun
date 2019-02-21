package ru.bacca.bikerun.repository;

import org.springframework.data.repository.CrudRepository;
import ru.bacca.bikerun.entity.AbstractEntity;

public interface GenericRepository<T extends AbstractEntity> extends CrudRepository<T, Long> {
}
