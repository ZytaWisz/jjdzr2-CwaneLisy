package com.infoshare.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.UUID;

@Transactional
public class GenericDao<T> implements Dao<T> {

    @Autowired
    protected EntityManager entityManager;

    private Class<T> type;

    public GenericDao() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }

    @Override
    public T find(UUID id) {
       return entityManager.find(type, id);
    }

    @Override
    public Collection<T> findAll() {
        final Query query = entityManager.createQuery("SELECT u FROM " + type.getCanonicalName() + " u", type);
        return query.getResultList();
    }

    @Override
    public void save(T t) {
        this.entityManager.persist(t);
    }

    @Override
    public T update(T t) {
        return this.entityManager.merge(t);
    }

    @Override
    public void delete(T t) {
        this.entityManager.remove(t);
    }
}
