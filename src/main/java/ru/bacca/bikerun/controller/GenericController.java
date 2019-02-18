package ru.bacca.bikerun.controller;

import ru.bacca.bikerun.entity.AbstractEntity;
import ru.bacca.bikerun.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class GenericController<T extends AbstractEntity, S extends GenericService<T>> {

    private final S service;

    @Autowired
    public GenericController(S service) {
        this.service = service;
    }

    @GetMapping(value = "/getall")
    public List<T> getAll() {
        return service.getAll();
    }

    @GetMapping(value = "/get/{id}")
    public T get(@PathVariable(name = "id") long id) {
        return service.getById(id);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void save(@RequestBody T entity) {
        service.save(entity);
    }

    @DeleteMapping(value = "delete/{id}")
    public void delete(@PathVariable(name = "id") long id) {
        service.delete(id);
    }

    public S getService() {
        return service;
    }
}
