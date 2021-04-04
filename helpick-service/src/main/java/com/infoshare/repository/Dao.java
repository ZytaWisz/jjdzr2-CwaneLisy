package com.infoshare.repository;

import java.util.Collection;
import java.util.UUID;

public interface Dao<T> {

    T find(UUID id);

    Collection<T> findAll();

    void save(T t);

    T update(T t);

    void delete(T t);
}
