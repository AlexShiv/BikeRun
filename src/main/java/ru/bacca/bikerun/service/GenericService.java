package ru.bacca.bikerun.service;

import ru.bacca.bikerun.entity.AbstractEntity;

import java.util.List;

public interface GenericService<T extends AbstractEntity> {

    List<T> getAll();

    T save(T obj);

    void delete(long id);

    T getById(long id);
}
