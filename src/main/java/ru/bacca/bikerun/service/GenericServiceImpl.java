package ru.bacca.bikerun.service;

import ru.bacca.bikerun.entity.AbstractEntity;
import ru.bacca.bikerun.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class GenericServiceImpl<T extends AbstractEntity, R extends GenericRepository<T>> implements GenericService<T> {

    protected final R repository;

    @Autowired
    public GenericServiceImpl(R abstractJpaRepository) {
        this.repository = abstractJpaRepository;
    }

    @Override
    public List<T> getAll() {
        return (List<T>) repository.findAll();
    }

    @Override
    public T save(T obj) {
        return repository.save(obj);
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Override
    public T getById(long id) {
        return repository.findById(id).get();
    }
}
