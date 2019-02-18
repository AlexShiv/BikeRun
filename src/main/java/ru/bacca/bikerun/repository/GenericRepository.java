package ru.bacca.bikerun.repository;

import ru.bacca.bikerun.entity.AbstractEntity;
import org.springframework.data.repository.CrudRepository;

public interface GenericRepository<T extends AbstractEntity> extends CrudRepository<T, Long> {
}
