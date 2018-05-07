package com.jahia.poc.model.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

/**
 * This common data access interface for standard CRUD operations
 */
@NoRepositoryBean
public interface ICommonRepository<T, ID extends Serializable> extends ElasticsearchRepository<T, ID> {

    /**
     *
     * @param entity
     * @param <S>
     * @return
     */
    <S extends T> S save(S entity);

    /**
     *
     * @param primaryKey
     * @return
     */
    Optional<T> findById(ID primaryKey);

    /**
     *
     * @return
     */
    Iterable<T> findAll();

    /**
     *
     * @param entity
     */
    void delete(T entity);

}
