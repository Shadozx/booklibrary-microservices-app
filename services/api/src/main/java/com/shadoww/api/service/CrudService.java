package com.shadoww.api.service;

import java.util.List;

public interface CrudService<T, ID> {

    T create(T entity);

    T readById(ID id);

    boolean existsById(ID id);

    T update(T updatedEntity);

    void deleteById(ID id);

    long count();

    List<T> getAll();
}
